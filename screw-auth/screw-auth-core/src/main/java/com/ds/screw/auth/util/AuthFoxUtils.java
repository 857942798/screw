package com.ds.screw.auth.util;

import com.ds.screw.auth.exception.AuthException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * <p>
 *       常用校验工具类
 * </p>
 *
 * @author dongsheng
 */
public class AuthFoxUtils {

    private AuthFoxUtils() {
    }

    /**
     * 验证URL的正则表达式
     */
    public static final String URL_REGEX = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 随机数生成对象
     */
    private static final Random random = new Random();

    /**
     * 生成指定长度的随机字符串
     *
     * @param length 字符串的长度
     * @return 一个随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 指定元素是否为null或者空字符串
     *
     * @param str 指定元素
     * @return 是否为null或者空字符串
     */
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 指定元素是否不为 (null或者空字符串)
     *
     * @param str 指定元素
     * @return 是否为null或者空字符串
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * 将日期格式化 （yyyy-MM-dd HH:mm:ss）
     *
     * @param date 日期
     * @return 格式化后的时间
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 将指定值转化为指定类型
     *
     * @param <T> 泛型
     * @param obj 值
     * @param cs  类型
     * @return 转换后的值
     */
    @SuppressWarnings({"java:S3776"})
    public static <T> T getValueByType(Object obj, Class<T> cs) {
        // 如果 obj 为 null 或者本来就是 cs 类型
        if (obj == null || obj.getClass().equals(cs)) {
            return (T) obj;
        }
        // 开始转换
        String obj2 = String.valueOf(obj);
        Object obj3 = null;
        if (cs.equals(String.class)) {
            obj3 = obj2;
        } else if (cs.equals(int.class) || cs.equals(Integer.class)) {
            obj3 = Integer.valueOf(obj2);
        } else if (cs.equals(long.class) || cs.equals(Long.class)) {
            obj3 = Long.valueOf(obj2);
        } else if (cs.equals(short.class) || cs.equals(Short.class)) {
            obj3 = Short.valueOf(obj2);
        } else if (cs.equals(byte.class) || cs.equals(Byte.class)) {
            obj3 = Byte.valueOf(obj2);
        } else if (cs.equals(float.class) || cs.equals(Float.class)) {
            obj3 = Float.valueOf(obj2);
        } else if (cs.equals(double.class) || cs.equals(Double.class)) {
            obj3 = Double.valueOf(obj2);
        } else if (cs.equals(boolean.class) || cs.equals(Boolean.class)) {
            obj3 = Boolean.valueOf(obj2);
        } else {
            obj3 = obj;
        }
        return (T) obj3;
    }

    /**
     * 根据默认值来获取值
     *
     * @param <T>          泛型
     * @param value        值
     * @param defaultValue 默认值
     * @return 转换后的值
     */
    public static <T> T getValueByDefaultValue(Object value, T defaultValue) {

        // 如果 obj 为 null，则直接返回默认值
        if (AuthFoxUtils.isEmpty(value)) {
            return defaultValue;
        }

        // 开始转换
        Class<T> cs = (Class<T>) defaultValue.getClass();
        return AuthFoxUtils.getValueByType(value, cs);
    }

    /**
     * 在url上拼接上kv参数并返回
     *
     * @param url      url
     * @param paramStr 参数, 例如 id=1001
     * @return 拼接后的url字符串
     */
    public static String joinParam(String url, String paramStr) {
        // 如果参数为空, 直接返回
        if (paramStr == null || paramStr.length() == 0) {
            return url;
        }
        if (url == null) {
            url = "";
        }
        int index = url.lastIndexOf('?');
        // ? 不存在
        if (index == -1) {
            return url + '?' + paramStr;
        }
        // ? 是最后一位
        if (index == url.length() - 1) {
            return url + paramStr;
        }
        // ? 是其中一位
        if (index < url.length() - 1) {
            String separatorChar = "&";
            // 如果最后一位是 不是&, 且 parameStr 第一位不是 &, 就增送一个 &
            if (url.lastIndexOf(separatorChar) != url.length() - 1 && paramStr.indexOf(separatorChar) != 0) {
                return url + separatorChar + paramStr;
            } else {
                return url + paramStr;
            }
        }
        // 正常情况下, 代码不可能执行到此
        return url;
    }

    /**
     * 在url上拼接上kv参数并返回
     *
     * @param url   url
     * @param key   参数名称
     * @param value 参数值
     * @return 拼接后的url字符串
     */
    public static String joinParam(String url, String key, Object value) {
        // 如果参数为空, 直接返回
        if (isEmpty(url) || isEmpty(key) || isEmpty(value)) {
            return url;
        }
        return joinParam(url, key + "=" + value);
    }

    /**
     * 在url上拼接锚参数
     *
     * @param url       url
     * @param parameStr 参数, 例如 id=1001
     * @return 拼接后的url字符串
     */
    public static String joinSharpParam(String url, String parameStr) {
        // 如果参数为空, 直接返回
        if (parameStr == null || parameStr.length() == 0) {
            return url;
        }
        if (url == null) {
            url = "";
        }
        int index = url.lastIndexOf('#');
        // ? 不存在
        if (index == -1) {
            return url + '#' + parameStr;
        }
        // ? 是最后一位
        if (index == url.length() - 1) {
            return url + parameStr;
        }
        // ? 是其中一位
        if (index < url.length() - 1) {
            String separatorChar = "&";
            // 如果最后一位是 不是&, 且 parameStr 第一位不是 &, 就增送一个 &
            if (url.lastIndexOf(separatorChar) != url.length() - 1 && parameStr.indexOf(separatorChar) != 0) {
                return url + separatorChar + parameStr;
            } else {
                return url + parameStr;
            }
        }
        // 正常情况下, 代码不可能执行到此
        return url;
    }

    /**
     * 在url上拼接锚参数
     *
     * @param url   url
     * @param key   参数名称
     * @param value 参数值
     * @return 拼接后的url字符串
     */
    public static String joinSharpParam(String url, String key, Object value) {
        // 如果参数为空, 直接返回
        if (isEmpty(url) || isEmpty(key) || isEmpty(value)) {
            return url;
        }
        return joinSharpParam(url, key + "=" + value);
    }

    /**
     * 将数组的所有元素使用逗号拼接在一起
     *
     * @param arr 数组
     * @return 字符串，例: a,b,c
     */
    public static String arrayJoin(String[] arr) {
        if (arr == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            str.append(arr[i]);
            if (i != arr.length - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }

    /**
     * 使用正则表达式判断一个字符串是否为URL
     *
     * @param str 字符串
     * @return 拼接后的url字符串
     */
    public static boolean isUrl(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase().matches(URL_REGEX);
    }

    /**
     * URL编码
     *
     * @param url see note
     * @return see note
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AuthException(e);
        }
    }

    /**
     * URL解码
     *
     * @param url see note
     * @return see note
     */
    public static String decoderUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AuthException(e);
        }
    }

    /**
     * 将指定字符串按照逗号分隔符转化为字符串集合
     *
     * @param str 字符串
     * @return 分割后的字符串集合
     */
    public static List<String> convertStringToList(String str) {
        List<String> list = new ArrayList<>();
        if (isEmpty(str)) {
            return list;
        }
        String[] arr = str.split(",");
        for (String s : arr) {
            s = s.trim();
            if (!isEmpty(s)) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 将指定集合按照逗号连接成一个字符串
     *
     * @param list 集合
     * @return 字符串
     */
    public static String convertListToString(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i));
            if (i != list.size() - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }

    /**
     * String 转 Array，按照逗号切割
     *
     * @param str 字符串
     * @return 数组
     */
    public static String[] convertStringToArray(String str) {
        List<String> list = convertStringToList(str);
        return list.toArray(new String[0]);
    }

    /**
     * Array 转 String，按照逗号切割
     *
     * @param arr 数组
     * @return 字符串
     */
    public static String convertArrayToString(String[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        return String.join(",", arr);
    }

    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     * <p> 参数 [集合, 元素]
     */
    public static BiFunction<List<String>, String, Boolean> hasElement = (list, element) -> {
        // 空集合直接返回false
        if(list == null || list.size() == 0) {
            return false;
        }
        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (list.contains(element)) {
            return true;
        }
        // 开始模糊匹配
        for (String patt : list) {
            if(vagueMatch(patt, element)) {
                return true;
            }
        }
        // 走出for循环说明没有一个元素可以匹配成功
        return false;
    };

    /**
     * 字符串模糊匹配
     * <p>example:
     * <p> user* user-add   --  true
     * <p> user* art-add    --  false
     * @param patt 表达式
     * @param str 待匹配的字符串
     * @return 是否可以匹配
     */
    public static boolean vagueMatch(String patt, String str) {
        // 如果表达式不带有*号，则只需简单equals即可 (速度提升200倍)
        if(patt.indexOf("*") == -1) {
            return patt.equals(str);
        }
        return Pattern.matches(patt.replaceAll("\\*", ".*"), str);
    }

}
