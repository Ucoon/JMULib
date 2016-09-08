package com.coolweather.jmulib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.coolweather.jmulib.Util.BorrowedListAdapter;
import com.coolweather.jmulib.Util.DateUtils;
import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.SharedPreferenceUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.ui.ListViewForScrollview;
import com.coolweather.jmulib.ui.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongJie on 2016/8/30.
 */
public class BorrowedActivity extends Fragment {
    private ListViewForScrollview listView;
    private List<Model> modelList =new ArrayList<>();
    public static long nowtime=System.currentTimeMillis();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.borrowed_layout,container,false);
        listView= (ListViewForScrollview) view.findViewById(R.id.listview);

        getBorrowed(SharedPreferenceUtil.getStringValue(getContext(),SharedPreferenceUtil.COOKIE,""));
        if(SharedPreferenceUtil.getIntValue(getContext(),SharedPreferenceUtil.BooksTotal,0)!=0){
            return view;
        }else{
            TextView textView=new TextView(getContext());
            textView.setText("您当前无借阅图书");
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize(20F);
            return textView;
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    BorrowedListAdapter adapter=new BorrowedListAdapter(getContext(),modelList);
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };
    //查询当前借阅
    private void getBorrowed(String cookie){
        HttpUtil.sendHttpRequestWithHttpURLConnection(UrlConfig.borrowed + cookie, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("response===",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if(status.equals("success")){
                                int booksTotal=jsonObject.getInt("booksTotal");
                                SharedPreferenceUtil.setIntValue(getContext(),SharedPreferenceUtil.BooksTotal,booksTotal);
                               PersonalActivity.tabLayout.getTabAt(0).setText("借阅"+booksTotal);
                                JSONArray booksList=jsonObject.getJSONArray("booksList");
                                for(int i=0;i<booksList.length();i++){
                                    JSONObject borrowedInfoes=booksList.getJSONObject(i);
                                    int No=borrowedInfoes.getInt("No");
                                    String bookid=borrowedInfoes.getString("bookid");
                                    String name=borrowedInfoes.getString("name");
                                    String author=borrowedInfoes.getString("author");
                                    JSONObject expireDate=borrowedInfoes.getJSONObject("expireDate");
                                    String rawValue=expireDate.getString("rawValue");
                                   long time= nowtime-Long.valueOf(DateUtils.dataOne(rawValue));
                                    Log.e("nowTime",Long.valueOf(DateUtils.dataOne(rawValue))+"");
                                    boolean renewable=borrowedInfoes.getBoolean("renewable");
                                    Model model=new Model(name,R.drawable.example,rawValue,renewable,String.valueOf(time/24/3600));
                                    modelList.add(model);
                                }
                                handler.sendEmptyMessage(1);
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
