package com.coolweather.jmulib.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ZongJie on 2016/6/2.
 */
public class HttpUtil {
    public static void sendHttpRequestWithHttpURLConnection(final String address,
                                                      final HttpCallBackListener httpCallBackListener){
        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream=connection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=bufferedReader.readLine())!=null){
                        response.append(line);
                    }if(httpCallBackListener!=null){
                        httpCallBackListener.onFinish(response.toString());
                    }
                }catch(Exception e){
                    httpCallBackListener.onError(e);
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
