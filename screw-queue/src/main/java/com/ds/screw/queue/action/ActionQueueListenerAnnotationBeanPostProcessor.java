package com.ds.screw.queue.action;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QueueActionBean 注解注册工具
 * 设计用于在分布式环境下通过远程队列或者其它方式调用本地方法或者执行批量操作
 * 主要用于执行提交至异步队列中的数据库批量操作等
 * 实现：将被注解的方法以Bean类形式注入SpringBean列表中，所有注册类默认构建apply方法，实现代理调用
 *
 * @see BeanDefinitionBuilder Bean类定义工具
 * @see BatchQueueActionWrapper 用于实现批量操作的包装容器
 * @author dongsheng
 */
public class ActionQueueListenerAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware {
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
    private DefaultListableBeanFactory register;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.register = ((DefaultListableBeanFactory) beanFactory);
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            MethodIntrospector.MetadataLookup<Set<ActionQueueListener>> metadataLookup = (Method method) -> {
                Set<ActionQueueListener> listenerMethods = findQueueActionListenerAnnotations(method);
                return (!listenerMethods.isEmpty() ? listenerMethods : null);
            };

            Map<Method, Set<ActionQueueListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, metadataLookup);
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(bean.getClass());
            } else {
                for (Map.Entry<Method, Set<ActionQueueListener>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    for (ActionQueueListener queueActionListener : entry.getValue()) {
                        processQueueActionListener(queueActionListener, method, bean);
                    }
                }
            }
        }
        return bean;
    }

    private Set<ActionQueueListener> findQueueActionListenerAnnotations(Method method) {
        Set<ActionQueueListener> actions = new HashSet<>();
        ActionQueueListener ann = AnnotatedElementUtils.findMergedAnnotation(method, ActionQueueListener.class);
        if (ann != null) {
            actions.add(ann);
        }
        return actions;
    }

    /**
     * @param queueActionListener QueueActionBean 对象
     * @param method              被 QueueActionListener 注解的方法
     * @param bean                被 QueueActionListener 注解所在Bean类
     */
    protected void processQueueActionListener(ActionQueueListener queueActionListener, Method method, Object bean) {
        Method methodToUse = checkProxy(method, bean);
        if (this.register != null && !this.register.containsBeanDefinition(method.getName())) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(QueueActionBeanFactoryBean.class);
            invokeAction(queueActionListener, methodToUse, bean, builder);
            this.register.registerBeanDefinition(resolveBeanName(queueActionListener, methodToUse), builder.getBeanDefinition());
        }
    }


    /**
     * 解析需要注入的Bean名称
     */
    private String resolveBeanName(ActionQueueListener queueActionListener, Method methodToUse) {
        String newBeanName = methodToUse.getName();
        if (!StringUtils.isEmpty(queueActionListener.value())) {
            newBeanName = queueActionListener.value();
        } else if (!StringUtils.isEmpty(queueActionListener.name())) {
            newBeanName = queueActionListener.name();
        }
        return newBeanName;
    }


    private <T> void invokeAction(ActionQueueListener queueActionListener, Method methodToUse, Object bean, BeanDefinitionBuilder builder) {
        builder.addConstructorArgValue(new BatchQueueActionWrapper<T>(queueActionListener) {
            @Override
            public void process(List<T> list) {
                try {
                    methodToUse.invoke(bean, list);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());

            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(ex);

            }
            Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
            for (Class<?> iface : proxiedInterfaces) {
                try {
                    method = iface.getMethod(method.getName(), method.getParameterTypes());
                    break;
                } catch (@SuppressWarnings("unused") NoSuchMethodException noMethod) {
                    // NOSONAR
                }
            }
        }
        return method;
    }

    /**
     * 仅起标识作用
     */
    public interface QueueActionWrapper {
    }

    public abstract static class BatchQueueActionWrapper<T> implements QueueActionWrapper {

        private final ActionQueue<T> queue;

        protected BatchQueueActionWrapper(ActionQueueListener listener) {
            this.queue = new ActionQueue<>(listener.name(),
                    listener.zoneSize(),
                    listener.pollSize(),
                    listener.threadSize(),
                    listener.maxWait(),
                    listener.isDelay(),
                    this::process);
        }

        abstract void process(List<T> list);

        public final ActionQueue<T> getQueue() {
            return this.queue;
        }
    }

    /**
     * 定义自定义生成一个ActionQueue bean的方式
     */
    private static class QueueActionBeanFactoryBean implements FactoryBean<QueueActionWrapper> {

        private final QueueActionWrapper queueActionWrapper;

        public QueueActionBeanFactoryBean(QueueActionWrapper queueActionWrapper) {
            this.queueActionWrapper = queueActionWrapper;
        }

        @Override
        public QueueActionWrapper getObject() {
            return queueActionWrapper;
        }

        @Override
        public Class<QueueActionWrapper> getObjectType() {
            return QueueActionWrapper.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }
}
