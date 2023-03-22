package com.ds.screw.wapi.core.service;

import com.ds.screw.wapi.core.properties.WapiProperties;
import com.ds.screw.wapi.core.xml.bean.WapiFlow;
import com.ds.screw.wapi.core.xml.bean.WapiResource;
import com.ds.screw.wapi.core.xml.bean.WapiRoot;
import com.ds.screw.wapi.core.xml.bean.WapiUnit;
import com.ds.screw.wapi.core.xml.utils.JAXBXmlUtil;
import com.ds.screw.wapi.core.xml.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author dongsheng
 */
public class WapiResourceService implements ApplicationListener<ContextRefreshedEvent> {
    private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final Logger logger = LoggerFactory.getLogger(WapiResourceService.class);
    private Map<String, WapiResource> resourceMap;
    @Autowired
    private WapiProperties wapiProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        load();
    }

    /**
     * 重新加载配置
     */
    public void refresh() {
        this.resourceMap.clear();
        load();
    }

    /**
     * 获取wapi资源配置
     *
     * @param objectName 资源名称
     * @return WapiResource
     */
    public WapiResource getResource(String objectName) {
        return this.resourceMap.get(objectName);
    }

    /**
     * 获取wapi某个资源下的流程配置
     *
     * @param objectName 资源名称
     * @param flowType   流程类型
     * @return WapiFlow
     */
    public WapiFlow getFlow(String objectName, String flowType) {
        WapiResource wapiResource = this.resourceMap.get(objectName);
        if (ObjectUtils.isEmpty(wapiResource)) {
            return null;
        }
        return wapiResource.getFlow(flowType);
    }

    /**
     * 获取资源路径
     *
     * @return String 资源路径
     */
    private String getResourcePath() {
        return "/mysql/**/*.xml";
    }


    /**
     * 根据路径查找资源，并执行
     *
     * @param resourcePath 资源路径
     * @param pathSets     去重比对对象
     * @return List<Resource>
     * @throws IOException 读取资源文件异常
     */
    private List<Resource> searchResources(String resourcePath, Set<String> pathSets) throws IOException {
        Resource[] resourcesArr = resolver.getResources(resourcePath);
        List<Resource> resources = Arrays.stream(resourcesArr).filter(localResource -> {
            try {
                return !pathSets.contains(localResource.getURI().getPath());
            } catch (IOException e) {
                return true;
            }
        }).collect(Collectors.toList());

        for (Resource jarResource : resources) {
            pathSets.add(jarResource.getURL().getPath());
        }

        return resources;
    }

    /**
     * 加载配置内容
     */
    private void load() {
        try {
            logger.debug(">>>>> WapiResource资源加载：开始...");

            String resourcePath = getResourcePath();
            Map<String, WapiResource> jarMap = new HashMap<>();
            // 用于去重的pathSets
            Set<String> pathSets = new HashSet<>();
            logger.debug(">>>>> WapiResource资源加载，加载预置资源...！");

            // 读取wapi配置加载顺序
            String loadOrder = wapiProperties.getWapiConfigLoadOrder();
            String[] modules = loadOrder.split(",");
            for (String module : modules) {
                if (ObjectUtils.isEmpty(module)) {
                    continue;
                }
                // 获取框架中的xml文件定义的流程，引入的所有jar中不允许有重复的key
                List<Resource> resources = searchResources("classpath*:" + module + resourcePath, pathSets);
                // 解析资源文件，获取WapiResource，以map结构返回 后 执行混合
                mergeResource(jarMap, parseResourceMap(resources));
            }

            logger.debug(">>>>> WapiResource资源加载，加载本地资源...！");
            List<Resource> localResources = searchResources("classpath*:" + "wapi" + resourcePath, pathSets);

            if (localResources.isEmpty()) {
                autoCreateXmlConfig();
                logger.warn(">>>>> WapiResource资源加载，疑似系统资源目录下不存在wapi配置，系统已自动为您创建，请重新打包后重启！;");

                // 直接退出，强制用户重启
                System.exit(1);
                return;
            } else {
                // 将本地的wapi配置补充至当前的jarMap中
                mergeResource(jarMap, parseResourceMap(localResources));
            }

            // 合并相同的流程，本地资源根据相同的key覆盖jar中资源
            this.resourceMap = jarMap;
            this.validateResourceMap();
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        } finally {
            logger.debug(">>>>> WapiResource资源加载：完成！");
        }
    }

    private void autoCreateXmlConfig() {
        File realConfigFile = new File("src/main/resources/wapi/mysql/data-resource-config.xml");
        FileOutputStream out1 = null;
        try {
            out1 = new FileOutputStream(realConfigFile.getPath());
            boolean result = realConfigFile.createNewFile();// 创建目标文件
            if (!result) {
                logger.error(">>>>>WapiResource资源加载，异常：创建临时文件失败！");
            }
            String dbXml = JAXBXmlUtil.toXML(new WapiRoot());
            out1.write(dbXml.getBytes(StandardCharsets.UTF_8));
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out1) {
                try {
                    out1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 验证资源类是否存在，不存在则在控制台提示
     */
    private void validateResourceMap() {
        this.resourceMap.forEach((key, item) -> {
            List<WapiFlow> flows = item.getFlows();
            flows.forEach((flow) -> {
                List<WapiUnit> units = flow.getUnits();
                units.forEach((unit) -> {
                    String impl = unit.getImpl();
                    try {
                        SpringContextUtil.getBean(impl);
                    } catch (Exception e) {
                        logger.warn(">>>>> WapiResource资源加载，异常：" +
                                        "实现类不存在：Resource Name: {}, Flow Type: {}, Unit Impl: {};",
                                getLengthStr(key, 40), getLengthStr(flow.getType(), 10),
                                getLengthStr(impl, 50));
                    }
                });
            });
        });
    }

    /**
     * 用于控制台打印对齐
     *
     * @param key 原始字符
     * @param len 限制长度
     * @return 对齐后的字符串
     */
    private String getLengthStr(String key, int len) {
        if (key.length() < len) {
            int leftLen = len - key.length();
            StringBuilder keyBuilder = new StringBuilder(key);
            for (int i = 0; i < leftLen; i++) {
                keyBuilder.append(" ");
            }
            key = keyBuilder.toString();
        }
        return key;
    }

    /**
     * 1. 判断是否有相同的resourceName,否则按照key+请求类型覆盖
     * 2. 将流程添加到map中返回
     *
     * @param resources 资源
     * @return Map<String, WapiResource>
     * @throws JAXBException 序列化异常
     * @throws IOException   IO异常
     */
    private Map<String, WapiResource> parseResourceMap(List<Resource> resources) throws IOException, JAXBException {
        Map<String, WapiResource> map = new HashMap<>();
        for (Resource resource : resources) {
            WapiRoot root = JAXBXmlUtil.fromXML(resource.getInputStream(), WapiRoot.class);
            if (root == null || CollectionUtils.isEmpty(root.getResources())) {
                continue;
            }
            root.getResources().forEach(newResource -> {
                if (!newResource.getKey().equals("standard")) {
                    // 按照key+请求类型覆盖
                    if (map.containsKey(newResource.getKey())) {
                        WapiResource existResource = map.get(newResource.getKey());
                        // 检查是否可以覆盖资源自动
                        boolean isOverwrite = checkIsOverwriteResource(existResource, newResource);

                        if (isOverwrite) {
                            try {
                                logger.warn(">>>>> WapiResource资源加载，覆盖资源：" +
                                                "Overwrite resource key '{}' defined in URL [{}]",
                                        existResource.getKey(), resource.getURL());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 获取已经存在的流程
                            // 如果当前resource中包含相同的流程
                            // 将已经存在的流程剔除
                            existResource.getFlows().removeIf(next ->
                                    !ObjectUtils.isEmpty(newResource.getFlow(next.getType())));
                            // 添加新的流程
                            newResource.getFlows().forEach(e -> existResource.getFlows().add(e));
                            map.put(newResource.getKey(), existResource);
                        } else {
                            // 如果不可以覆盖
                            // 抛出提示异常
                            try {
                                logger.error(">>>>> WapiResource资源加载，覆盖失败：" +
                                                "Overwrite resource key '{}' defined in URL [{}]",
                                        existResource.getKey(), resource.getURL());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        map.put(newResource.getKey(), newResource);
                    }
                }
            });
        }
        return map;
    }

    /**
     * 检查是否可以进行资源的覆盖
     *
     * @return Boolean
     */
    private Boolean checkIsOverwriteResource(WapiResource resource, WapiResource checkResource) {
        // protect 默认为false
        boolean protectedAttrOfExistResource = resource.getProtect() != null && resource.getProtect();
        // overwrite 默认为false
        boolean overWriteAttrOfCurrentResource = checkResource.getOverwrite() != null && checkResource.getOverwrite();

        return !protectedAttrOfExistResource || overWriteAttrOfCurrentResource;
    }

    /**
     * 合并jar的wapi配置和本地的wapi配置
     *
     * @author mahe
     */
    private void mergeResource(Map<String, WapiResource> jarMap, Map<String, WapiResource> localMap) {
        localMap.forEach((localKey, localResource) -> {
            jarMap.merge(localKey, localResource, (jarValue, localValue) -> {
                // 检查是否可以资源覆盖
                boolean isOverWrite = checkIsOverwriteResource(jarValue, localResource);
                if (isOverWrite) {
                    try {
                        logger.warn(">>>>> WapiResource资源加载，覆盖资源：Overwrite resource key '{}'", localResource.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 如果可以覆盖，开始处理将本地和jar包的wapi流程合并，并且本地的将覆盖jar包的
                    Map<String, WapiFlow> jvMap = jarValue.getFlows().stream().collect(Collectors.toMap(WapiFlow::getType, Function.identity()));
                    Map<String, WapiFlow> lvMap = localValue.getFlows().stream().collect(Collectors.toMap(WapiFlow::getType, Function.identity()));
                    lvMap.forEach((lk1, lv1) -> jvMap.merge(lk1, lv1, (jkv1, lkv1) -> lkv1));
                    List<WapiFlow> flows = new ArrayList<>(jvMap.values());
                    return new WapiResource(localValue.getKey(), localValue.getName(), localValue.getDesc(), flows);
                } else {
                    // 如果不可以覆盖
                    // 抛出提示异常
                    try {

                        logger.error(">>>>> WapiResource资源加载，覆盖失败：Overwrite resource key '{}'", localResource.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 继续使用jar包里的wapi流程配置
                    return jarValue;
                }

            });
        });
    }
}
