package com.coolweather.jmulib;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.jmulib.Util.BookInfoesAdapter;
import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.ui.ListViewForScrollview;
import com.coolweather.jmulib.ui.Model;
import com.coolweather.jmulib.ui.ShareDialog;
import com.coolweather.jmulib.ui.ShareModel;
import com.mob.tools.utils.UIHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ZongJie on 2016/9/4.
 */
public class BookInfoesActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    private String name, author, publisher, bookid;
    private TextView book_name_content;
    private TextView book_author;
    private TextView publisher_text;

    private LinearLayout project_back_ll;
    private LinearLayout share_ll;

    private TextView infoes_text;
    private TextView callNumber;
    private TextView location;
    private List<Model> bookModelList = new ArrayList<>();
    private ListViewForScrollview listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_content);
        ShareSDK.initSDK(BookInfoesActivity.this);
        //沉浸式状态栏
        View view = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(option);
        initdata();

    }

    private void initdata() {
        Intent intent = this.getIntent();
        name = intent.getStringExtra("name");
        author = intent.getStringExtra("author");
        publisher = intent.getStringExtra("publisher");
        bookid = intent.getStringExtra("bookid");
        book_name_content = (TextView) findViewById(R.id.book_name_content);
        book_name_content.setText(name);
        book_author = (TextView) findViewById(R.id.book_author);
        book_author.setText(author);
        publisher_text = (TextView) findViewById(R.id.publisher);
        publisher_text.setText(publisher);

        //返回按钮和分享按钮
        project_back_ll = (LinearLayout) findViewById(R.id.project_back_ll);
        project_back_ll.setOnClickListener(this);
        share_ll = (LinearLayout) findViewById(R.id.share_ll);
        share_ll.setOnClickListener(this);

        listView = (ListViewForScrollview) findViewById(R.id.book_listview);
        infoes_text = (TextView) findViewById(R.id.infoes);
        location = (TextView) findViewById(R.id.location);
        callNumber = (TextView) findViewById(R.id.callNumber);

        SearchResActivity.loading();
        //网络请求 开启子线程
        bookInfoes(UrlConfig.bookInfoes + bookid);

    }

    //指定id搜索书籍
    private void bookInfoes(String address) {

        HttpUtil.sendHttpRequestWithHttpURLConnection(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                Log.e("response", response);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                //子线程切换回UI线程
                handleResponse(response);
            }
//                });
//
//            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SearchResActivity.mLoading.dismiss();
                        Toast.makeText(BookInfoesActivity.this, "可能网络炸了...请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });
                BookInfoesActivity.this.finish();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    SearchResActivity.mLoading.dismiss();
                    location.setText(location_text);
                    infoes_text.setText(intro);
                    callNumber.setText(callNumber_text);
                    BookInfoesAdapter bookInfoesAdapter = new BookInfoesAdapter(BookInfoesActivity.this, R.layout.book_list_item, bookModelList);
                    listView.setAdapter(bookInfoesAdapter);
                    break;
            }
        }
    };

    String intro, location_text, callNumber_text;

    private void handleResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("success")) {
                intro = jsonObject.getString("intro");
                JSONArray infoes = jsonObject.getJSONArray("infoes");
                for (int i = 0; i < infoes.length(); i++) {
                    JSONObject object = infoes.getJSONObject(i);
                    location_text = object.getString("location");
                    callNumber_text = object.getString("callNumber");
                    String bookStatus = object.getString("bookStatus");
                    Model bookModel = new Model("图书" + String.valueOf(i + 1), bookStatus);
                    bookModelList.add(bookModel);
                }

                handler.sendEmptyMessage(2);
            } else {
                //status为误，finish()并Toast提醒
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //截屏分享
    public static Bitmap bmp;
    private String imageurl;

    private void screenshot() {
        // 获取屏幕
        View dView = BookInfoesActivity.this.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                imageurl = sdCardPath + File.separator + "screenshot.png";
                if (imageurl.equals("")){
                   Toast.makeText(BookInfoesActivity.this,"无法获取路径",Toast.LENGTH_LONG).show();
                }
                File file = new File(imageurl);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed", "onBackPressed");
        this.finish();
    }
    private String imageurl1 = "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.project_back_ll:
                finish();
                break;
            case R.id.share_ll:
                //截屏分享
                screenshot();
                Log.e("imageurl",imageurl);

                ShareDialog dialog=new ShareDialog(BookInfoesActivity.this, new ShareDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent() {
                        // share();
                    }
                },R.style.Theme_Light_Dialog);
                dialog.setPlatformActionListener(BookInfoesActivity.this);
                ShareModel model = new ShareModel();
                model.setImageFilepath(imageurl);
              //  model.setImageUrl(imageurl1);
                model.setText("发现一本好书");
                dialog.initShareParams(model);
                dialog.show();
                break;

        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(getApplicationContext());
    }
}
