package com.coolweather.jmulib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.SharedPreferenceUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.ui.LoginDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView user_bg;
    private TextView username;
    private EditText edit_search;
    private Button btn_search;
    private ImageView gifImage;
    private String address;

    //定义一个变量，来标识是否退出
    private static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(option);
        init();
    }

    private void init() {
        user_bg = (ImageView) findViewById(R.id.user_bg);
        user_bg.setOnClickListener(this);
        username = (TextView) findViewById(R.id.user_name);
        edit_search = (EditText) findViewById(R.id.edit_search);


        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        gifImage = (ImageView) findViewById(R.id.gifImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPreferenceUtil.getStringValue(MainActivity.this, SharedPreferenceUtil.USERNAME, "JMU").equals("")) {
            username.setText(SharedPreferenceUtil.getStringValue(MainActivity.this, SharedPreferenceUtil.USERNAME, "JMU"));
        }
    }

    LoginDialog dialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_bg:
                if (!SharedPreferenceUtil.getStringValue(MainActivity.this, SharedPreferenceUtil.COOKIE, "").equals("")) {
                    Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                    startActivity(intent);
                } else {
                    dialog = new LoginDialog(MainActivity.this, new LoginDialog.IcustomDialogEventListener() {
                        @Override
                        public void CustomDialogEvent(String text1, String text2) {
                            //网络请求 开启子线程
                            UserLogin(text1, text2);
                        }
                    }, R.style.Dialog);
                    dialog.show();
                }
                break;
            case R.id.btn_search:

                //URL中含中文，使用正则表达式编码中文字符串
                address = edit_search.getText().toString();
                if (!address.equals("")) {
                    Glide.with(this).load(R.drawable.chicken).crossFade(1000).into(gifImage);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, SearchResActivity.class);
                            intent.putExtra("address", address);
                            startActivity(intent);
                        }
                    };
                    timer.schedule(task, 1000);
                } else {
                    Toast.makeText(MainActivity.this, "输入不为空", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        edit_search.setText("");
    }

    //用户登录
    private void UserLogin(String user_account, String pw) {
        HttpUtil.sendHttpRequestWithHttpURLConnection(UrlConfig.login + user_account + "&pwd=" + pw, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        try {
                            Log.e("response==", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String cookie = jsonObject.getString("cookie");
                            SharedPreferenceUtil.setStringValue(MainActivity.this, SharedPreferenceUtil.COOKIE, cookie);
                            if (status.equals("success")) {
                                //     getInfoes(SharedPreferenceUtil.getStringValue(MainActivity.this,SharedPreferenceUtil.COOKIE,""));
                                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }else{
                                Log.e("请输入正确的学号或密码 外面", "请输入正确的学号或密码");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("请输入正确的学号或密码", "请输入正确的学号或密码");
                                        Toast.makeText(MainActivity.this, "请输入正确的学号或密码", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                });
//            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "可能网络炸了...请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //点击两次返回键强制退出
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                exit();
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;

    }
}
