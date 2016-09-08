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
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.jmulib.Util.BookInfoesAdapter;
import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.ui.ListViewForScrollview;
import com.coolweather.jmulib.ui.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongJie on 2016/9/4.
 */
public class BookInfoesActivity extends AppCompatActivity implements View.OnClickListener {
    private String name, author, publisher, bookid;
    private TextView book_name_content;
    private TextView book_author;
    private TextView publisher_text;

    private ImageView project_back_btn;
    private ImageView share_img;

    private TextView infoes_text;
    private TextView callNumber;
    private TextView location;
    private List<Model> bookModelList=new ArrayList<>();
    private ListViewForScrollview listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_content);
        //沉浸式状态栏
        View view=getWindow().getDecorView();
        int option=View.SYSTEM_UI_FLAG_FULLSCREEN;
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
        project_back_btn = (ImageView) findViewById(R.id.project_back_btn);
        project_back_btn.setOnClickListener(this);
        share_img = (ImageView) findViewById(R.id.share_img);
        share_img.setOnClickListener(this);

        listView= (ListViewForScrollview) findViewById(R.id.book_listview);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //子线程切换回UI线程
                        handleResponse(response);
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                      SearchResActivity.mLoading.dismiss();
                       Toast.makeText(BookInfoesActivity.this,"可能网络炸了...请稍后重试",Toast.LENGTH_LONG).show();
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
                    BookInfoesAdapter bookInfoesAdapter=new BookInfoesAdapter(BookInfoesActivity.this,R.layout.book_list_item,bookModelList);
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
                    String bookStatus=object.getString("bookStatus");
                    Model bookModel=new Model("图书"+String.valueOf(i+1),bookStatus);
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
    public static Bitmap bitmap;
    private String imageurl;
    private void screenView(){
        //获取界面
        View view=getWindow().getDecorView();
        //从界面中获取图像：getDrawingCache()
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap=view.getDrawingCache();
        if(bitmap!=null){
            //获取内置SD卡路径
            String sdCardPath= Environment.getExternalStorageDirectory().getPath();
            //图片文件路径
            imageurl=sdCardPath+ File.separator+"screenshot.png";
            if(imageurl.equals("")){
                Toast.makeText(BookInfoesActivity.this,"无法获取路径",Toast.LENGTH_LONG).show();
                //创建File对象，储存截屏后的图片
                File file=new File(imageurl);
                try {
                    //图片压缩
                    FileOutputStream os=new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);//100表示不压缩
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed","onBackPressed");
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.project_back_btn:
                finish();
                break;
            case R.id.share_img:
                //截屏分享
                screenView();
                break;

        }
    }
}
