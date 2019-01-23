package com.songheng.dsp.utils;

import com.google.common.base.Strings;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * @Title:  StringUtils
 * @Package com.songheng.dsp.utils
 * @Description: 字符串工具类
 * @author: zhangshuai@021.com
 * @date:   2019-01-22 22:24
 * @version V1.0
 */
public final class StringUtils {

    private StringUtils(){}


    /**
     * @Fields NUM_MAX_LENGTH : 数值类字符串的最大长度
     */
    private final static int NUM_MAX_LENGTH = 9;

    /**
     * @Fields DEFAULT_PAD_CHAR : 默认填充的字符
     */
    private final static char DEFAULT_PAD_CHAR = '0';

    /**
     * @Fields DEFAULT_PAD_LENGTH : 默认填充后的字符串长度
     */
    private final static int DEFAULT_PAD_LENGTH = 4;

    /**
     *
     * @Description: 判断是否无效字符串(null,"","null","NULL","nan","NaN","NAN"...)
     * @param string 需要验空的参数
     * @return
     *        true:无效字符串
     *        false:有效字符串
     * */
    public static boolean isInvalidString(@Nullable String string) {
        return ( string == null || "".equals(string.trim()) || "null".equalsIgnoreCase(string)
                || "nan".equalsIgnoreCase(string)
        );
    }

    /***
     * @Description: 将 (null,"","null","NULL","nan","NaN","NAN"...) 无效字符串替换成 replaceString
     * @param oldString 原始字符串
     * @param replaceString 需要替换的字符串
     * @return 如果参数是无效字符串则返回replaceString,否则返回oldString
     * */
    public static String replaceNullOrEmpty(@Nullable String oldString, @NotNull String replaceString){
        return isInvalidString(oldString) ? replaceString : oldString;
    }

    /**
     * @Description: 判断这个字符是否是数值类型的
     * @param string 需要验证的字符串
     * @return  如果参数是数字则返回true,否则返回false
     *
     * */
    public static boolean isNumeric(String string) {
        if(isInvalidString(string) || string.length()>=NUM_MAX_LENGTH){
            return false;
        }
        for(int i=string.length();--i>=0;) {
            int chr = string.charAt(i);
            if(chr<48 || chr>57) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description: 将数值类字符串转化为Integer
     * @param string 需要转换的字符串
     * @return 如果字符串是有效数值则返回对应数值 否则返回 -1
     * **/
    public static Integer parseInteger(@NotNull String string){
        return isNumeric(string) ? Integer.parseInt(string) : -1;
    }
    /**
     * @Description: 将字符串前面补充 <code>padChar</code> 字符 , 使整个字符长度为 <code>strLength</code>
     * @param oldStr 原始字符串
     * @param strLength 需要的字符串长度
     * @param padChar 需要填充的字符
     * @return 填充后的字符串，若原始字符串长度大于<code>strLength</code> 则返回原始字符串
     * **/

    public static String padStart(String oldStr,Integer strLength,char padChar){
        return Strings.padStart(oldStr,strLength,padChar);
    }
    /**
     * @Description: 将字符串前面补充 <code>DEFAULT_PAD_CHAR</code> 字符 , 使整个字符长度为 <code>DEFAULT_PAD_LENGTH</code>
     * @param oldStr 原始字符串
     * @return 填充后的字符串，若原始字符串长度大于<code>strLength</code> 则返回原始字符串
     * **/

    public static String padDefaultStart(String oldStr){
        return padStart(oldStr,DEFAULT_PAD_LENGTH,DEFAULT_PAD_CHAR);
    }
    /**
     * @Description: 将字符串后面补充 <code>padChar</code> 字符 , 使整个字符长度为 <code>strLength</code>
     * @param oldStr 原始字符串
     * @param strLength 需要的字符串长度
     * @param padChar 需要填充的字符
     * @return 填充后的字符串，若原始字符串长度大于<code>strLength</code> 则返回原始字符串
     * **/

    public static String padEnd(String oldStr,Integer strLength,char padChar){
        return Strings.padEnd(oldStr,strLength,padChar);
    }
    /**
     * @Description: 将字符串前面补充 <code>DEFAULT_PAD_CHAR</code> 字符 , 使整个字符长度为 <code>DEFAULT_PAD_LENGTH</code>
     * @param oldStr 原始字符串
     * @return 填充后的字符串，若原始字符串长度大于<code>strLength</code> 则返回原始字符串
     * **/

    public static String padDefaultEnd(String oldStr){
        return padEnd(oldStr,DEFAULT_PAD_LENGTH,DEFAULT_PAD_CHAR);
    }
    public static void main(String[] args) {
        //判断字符串是否为空
        System.out.println("isEmpty:"+StringUtils.isInvalidString(null));
        //将 null 或者 "" 字符串 使用默认值替换
        System.out.println("replaceNullOrEmpty:"+StringUtils.replaceNullOrEmpty("NaN","1"));
        System.out.println("isNumeric:"+isNumeric(null));
        System.out.println("parseInteger:"+StringUtils.parseInteger("1298888"));
        System.out.println("padDefaultStart："+StringUtils.padDefaultStart("11"));
        System.out.println("padStart:"+StringUtils.padStart("121",4,'0'));
        System.out.println("padDefaultEnd:"+StringUtils.padDefaultEnd("11"));
        System.out.println("padEnd:"+StringUtils.padEnd("121",4,'0'));


    }
}
