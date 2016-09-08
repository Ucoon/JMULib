package com.coolweather.jmulib.Util;

/**
 * Created by ZongJie on 2016/6/2.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
