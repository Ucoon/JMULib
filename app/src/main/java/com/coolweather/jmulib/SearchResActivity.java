package com.coolweather.jmulib;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.jmulib.Util.DialogUtil;
import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.Util.TextColorAdapter;
import com.coolweather.jmulib.ui.ListViewForScrollview;
import com.coolweather.jmulib.ui.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongJie on 2016/8/31.
 */
public class SearchResActivity extends AppCompatActivity implements View.OnClickListener {
    List<Model> datalist=new ArrayList();
    private List<String> bookidList=new ArrayList<>();
    private List<String> nameList=new ArrayList<>();
    private List<String> authorList=new ArrayList<>();
    private List<String> publishList=new ArrayList<>();
    private ListViewForScrollview listView;

    private LinearLayout project_back_ll;
    private LinearLayout search_text_ll;
    private EditText edit_search;
    public static Dialog mLoading;

    private String keyword="";
    String address;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sarech_result);
        //沉浸式状态栏
        View view=getWindow().getDecorView();
        int option=View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(option);
        listView= (ListViewForScrollview) findViewById(R.id.search_results);
        project_back_ll= (LinearLayout) findViewById(R.id.project_back_ll);
        project_back_ll.setOnClickListener(this);
        search_text_ll= (LinearLayout) findViewById(R.id.search_text_ll);
        search_text_ll.setOnClickListener(this);
        edit_search= (EditText) findViewById(R.id.edit_search);
        mLoading= DialogUtil.createLoadingDialog(SearchResActivity.this);
        initdata();
    }
    private void initdata(){
        Intent intent=this.getIntent();
        keyword=intent.getStringExtra("address");
        address = UrlConfig.booksList + keyword + "/page/1";
        //URL中含中文，使用正则表达式编码中文字符串
        final String url = Uri.encode(address, "-![.:/,%?&=]");
        loading();
        //网络请求 开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                querySearch(url);
            }
        }).start();

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mLoading.dismiss();
                    TextColorAdapter adapter=new TextColorAdapter(getApplicationContext(),datalist);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(SearchResActivity.this,BookInfoesActivity.class);
                            intent.putExtra("name",nameList.get(i));
                            intent.putExtra("author",authorList.get(i));
                            intent.putExtra("publisher",publishList.get(i));
                            intent.putExtra("bookid",bookidList.get(i));
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

    private void querySearch(final String address) {
        HttpUtil.sendHttpRequestWithHttpURLConnection(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        Log.e("response==",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            Log.e("Search", status);
                            if(status.equals("success")){
                                JSONArray booksList = jsonObject.getJSONArray("booksList");
                                int i;
                                for (i = 0; i < booksList.length(); i++) {

                                    JSONObject object = booksList.getJSONObject(i);
                                    String bookid = object.getString("bookid");
                                    bookidList.add(bookid);
                                    String name = object.getString("name");
                                    nameList.add(name);
                                    String author = object.getString("author");
                                    authorList.add(author);
                                    String publisher = object.getString("publisher");
                                    publishList.add(publisher);
                                    String callNumber = object.getString("callNumber");
                                    String total = object.getString("total");
                                    String available = object.getString("available");
                                    Model model=new Model(name,R.drawable.example,callNumber,publisher,total,available,author);
                                    datalist.add(model);
                                }
                                handler.sendEmptyMessage(1);
                            }else {
                                //status为误，finish()并Toast提醒
                                mLoading.dismiss();
                                Toast.makeText(SearchResActivity.this,"可能 图书馆没有这本书",Toast.LENGTH_LONG).show();
                                SearchResActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                });
//            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoading.dismiss();
                        Toast.makeText(SearchResActivity.this, "可能网络炸了...请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });
                SearchResActivity.this.finish();
            }
        });
    }

    //菊花加载
    public static void loading(){
        Window window=mLoading.getWindow();
        WindowManager.LayoutParams lp=window.getAttributes();
        lp.alpha=0.9f;
        window.setAttributes(lp);
        mLoading.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent=new Intent(SearchResActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.project_back_ll:
                this.finish();
                break;
            case R.id.search_text_ll:
                String speword=edit_search.getText().toString();
                if(!speword.equals("")){
                    String address_two = UrlConfig.booksList + speword + "/page/1";
                    //URL中含中文，使用正则表达式编码中文字符串
                    String url_two = Uri.encode(address_two, "-![.:/,%?&=]");
                    if(datalist.size()!=0){
                        datalist.clear();
                        loading();
                        //网络请求 开启子线程
                        querySearch(url_two);
                    }
                }else{
                    Toast.makeText(SearchResActivity.this,"输入不为空",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
