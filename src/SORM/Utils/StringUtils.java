package SORM.Utils;

import java.util.Locale;

/**
 * 封装了字符串的常用操作
 * @author 李浪波
 */
public class StringUtils {
    /**
     * 将子一个字符串的首字母转换成大写并返回该字符串
     * @param str 待转换的字符串
     * @return 返回转换好的字符串
     */
    public static String firstChar2UpperCase(String str){
        /**
         * 示例：apple-->Apple
         */
        return str.toUpperCase().substring(0,1)+str.substring(1);
    }
}
