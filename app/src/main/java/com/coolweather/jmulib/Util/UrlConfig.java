package com.coolweather.jmulib.Util;

/**
 * Created by ZongJie on 2016/8/29.
 */
public class UrlConfig {
    public static String root="http://api.diviniti.cn/jmu/library/";//根接口
    public static String login=root+"login?user=";//登录接口
    public static String infoes=root+"user/info?cookie=";//个人信息接口
    public static String borrowed=root+"borrowed?cookie=";//查询当前借阅
    public static String expired=root+"expired?cookie=";//查询催还图书
    public static String booksList=root+"search/";//关键词搜索
    public static String bookInfoes=root+"book/";//指定id搜素
}
