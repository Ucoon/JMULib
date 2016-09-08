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
public class ExpiredActivity extends Fragment {
    private ListViewForScrollview listView_expired;
    private List<Model> modelList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.expiredbooks_layout,container,false);
        listView_expired= (ListViewForScrollview) view.findViewById(R.id.listview_expired);
        ExpiredBook(SharedPreferenceUtil.getStringValue(getContext(),SharedPreferenceUtil.COOKIE,""));
        if(SharedPreferenceUtil.getIntValue(getContext(),SharedPreferenceUtil.ExpiredTotal,0)!=0){
            return view;
        }else{
            TextView textView=new TextView(getContext());
            textView.setText("您当前无催还图书");
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
                    listView_expired.setAdapter(adapter);
                    break;
            }
        }
    };
    //查询催还图书
    private void ExpiredBook(String cookie){
        HttpUtil.sendHttpRequestWithHttpURLConnection(UrlConfig.expired + cookie, new HttpCallBackListener() {
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
                                int expiredTotal=jsonObject.getInt("expiredTotal");
                                SharedPreferenceUtil.setIntValue(getContext(),SharedPreferenceUtil.ExpiredTotal,expiredTotal);
                                PersonalActivity.tabLayout.getTabAt(1).setText("催还"+expiredTotal);
                                JSONArray expiredBooks=jsonObject.getJSONArray("expiredBooks");
                                for(int i=0;i<expiredBooks.length();i++){
                                    JSONObject expiredBooksInfoes=expiredBooks.getJSONObject(i);
                                    int No=expiredBooksInfoes.getInt("No");
                                    String bookid=expiredBooksInfoes.getString("bookid");
                                    String name=expiredBooksInfoes.getString("name");
                                    String author=expiredBooksInfoes.getString("author");
                                    String location= expiredBooksInfoes.getString("location");
                                    JSONObject expiredDate=expiredBooksInfoes.getJSONObject("expireDate");
                                    String rawValue=expiredDate.getString("rawValue");
                                    long time= BorrowedActivity.nowtime-Long.valueOf(DateUtils.dataOne(rawValue));
                                    Log.e("nowTime",Long.valueOf(DateUtils.dataOne(rawValue))+"");
                                    Model model=new Model(name,R.drawable.example,rawValue,String.valueOf(time/24/3600));
                                    modelList.add(model);
                                    handler.sendEmptyMessage(1);
                                }
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
