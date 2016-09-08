package com.coolweather.jmulib.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZongJie on 2016/8/29.
 */
public class SharedPreferenceUtil {
    /** 数据存储的XML名称 **/
    public final static String SETTING = "SharedPrefsStrList";
    public static String COOKIE="cookie";
    public static String USERINFO="userinfo";
    public static String USERNAME="username";
    public static String UserDepartment="userDepartment";
    public static String UserRemarks="userRemarks";
    public static String BooksTotal="booksTotal";
    public static String ExpiredTotal="expiredTotal";
    public static String getStringValue(Context context, String key, String default_value) {
        SharedPreferences data = context.getSharedPreferences(SETTING, 0);
        return data.getString(key, default_value);
    }

    public static boolean setStringValue(Context context, String key,String value) {
        SharedPreferences.Editor data = context.getSharedPreferences(SETTING,0).edit();
        data.putString(key, value);
        return data.commit();
    }

    public static boolean getBooleanValue(Context context, String key,boolean default_value) {
        SharedPreferences data = context.getSharedPreferences(SETTING, 0);
        return data.getBoolean(key, default_value);
    }

    public static boolean setBooleanValue(Context context, String key,boolean value) {
        SharedPreferences.Editor data = context.getSharedPreferences(SETTING,0).edit();
        data.putBoolean(key, value);
        return data.commit();
    }

    public static int getIntValue(Context context, String key, int default_value) {
        SharedPreferences data = context.getSharedPreferences(SETTING, 0);
        return data.getInt(key, default_value);
    }

    public static boolean setIntValue(Context context, String key, int value) {
        SharedPreferences.Editor data = context.getSharedPreferences(SETTING,0).edit();
        data.putInt(key, value);
        return data.commit();
    }


    public static long getLongValue(Context context, String key,long default_value) {
        SharedPreferences data = context.getSharedPreferences(SETTING, 0);
        return data.getLong(key, default_value);
    }

    public static boolean setLongValue(Context context, String key, long value) {
        SharedPreferences.Editor data = context.getSharedPreferences(SETTING, 0).edit();
        data.putLong(key, value);
        return data.commit();
    }

    public static float getfloatValue(Context context, String key,float default_value) {
        SharedPreferences data = context.getSharedPreferences(SETTING, 0);
        return data.getFloat(key, default_value);
    }

    public static boolean setfloatValue(Context context, String key, float value) {
        SharedPreferences.Editor data = context.getSharedPreferences(SETTING, 0).edit();
        data.putFloat(key, value);
        return data.commit();
    }
    /**
     * 清空所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
                .edit();
        sp.clear();
        sp.commit();
    }
}
