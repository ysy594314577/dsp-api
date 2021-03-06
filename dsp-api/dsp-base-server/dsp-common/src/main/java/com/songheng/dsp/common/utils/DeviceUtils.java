package com.songheng.dsp.common.utils;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * @description: 获取设备信息工具类
 * @author: zhangshuai@021.com
 * @date: 2019-01-23 13:32
 */
public final class DeviceUtils {
    /**
     * 无效的 ua_id
     * */
    public final static int INVALID_UA_ID = 16843022;

    private DeviceUtils(){}
    /**
     * @description: 获取操作系统名称
     * @param ua UserAgent
     * @return android,ios,mac os x,windows,unknown
     */

    public static String getOsName(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getOperatingSystem().getGroup().getName().toLowerCase();
    }
    /**
     * @description: 获取操作系统名称
     * @param ua UserAgent
     * @return android 5.x,mac os x (iphone),mac os x,windows,unknown
     */
    public static String getOs(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getOperatingSystem().getName().toLowerCase();
    }
    /**
     * @description: 获取设备类型
     * @param ua UserAgent
     * @return mobile,computer,unknown
     */
    public static String getDeviceType(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getOperatingSystem().getDeviceType().getName().toLowerCase();
    }

    /**
     * @description: 获取浏览器名称
     * @param ua UserAgent
     * @return Chrome,Outlook,Firefox,Safari,Opera,Unknown
     */
    public static String getBrowserName(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getBrowser().getGroup().getName();
    }
    /**
     * @description: 获取浏览器版本
     * @param ua UserAgent
     * @return 71.0.3578.98,10.0,Unknown
     */
    public static String getBrowserVersion(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getBrowserVersion().getVersion();
    }
    /**
     * @description: 获取UserAgentId
     * @param ua UserAgent
     * @return 251989860,Unknown
     */
    public static int getUserAgentId(String ua){
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        return userAgent.getId();
    }
    /**
     * @description: 获取手机品牌
     * @param ua UserAgent
     * @return 251989860,Unknown
     */
    public static String getPhoneModel(String ua){
        if(DeviceType.MOBILE.getName().equalsIgnoreCase(getDeviceType(ua))){
            if(OperatingSystem.IOS.getName().equalsIgnoreCase(getOsName(ua))){
                return "iPhone";
            }else{
                String rex="[()]+";
                String[] str=ua.split(rex);
                str = str[1].split("[;]");
                String[] res=str[str.length-1].split("Build/");
                return res[0].trim();
            }
        }
        return  "other";
    }
    public static void main(String[] args) {
        String macua =     "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
        String androidua = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36";
        String iosua     = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";
        String winua     = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";
        System.out.println(getOsName(iosua));
        System.out.println(getOs(winua));
        System.out.println(getDeviceType(macua));
        System.out.println(getBrowserName(androidua));
        System.out.println(getBrowserVersion(androidua));
        System.out.println(getUserAgentId(iosua));
        System.out.println(getOsName(iosua));
        System.out.println(getOs(iosua));
        System.out.println("=="+getPhoneModel(androidua)+"==");
        System.out.println(getPhoneModel(iosua));
        System.out.println(getPhoneModel(winua));
    }
}
