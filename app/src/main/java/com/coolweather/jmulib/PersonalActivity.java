package com.coolweather.jmulib;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.jmulib.Util.HttpCallBackListener;
import com.coolweather.jmulib.Util.HttpUtil;
import com.coolweather.jmulib.Util.MyViewPagerAdapter;
import com.coolweather.jmulib.Util.SharedPreferenceUtil;
import com.coolweather.jmulib.Util.UrlConfig;
import com.coolweather.jmulib.ui.AutoHeightViewPager;
import com.coolweather.jmulib.ui.Exit_Dialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongJie on 2016/8/7.
 */
public class PersonalActivity extends AppCompatActivity {
    private TextView userremarks,personal_username,userDepartment;
    private ImageView personal_exit;
    private ImageView search_img;
    public static TabLayout tabLayout;
    AutoHeightViewPager viewPager;
    String[] titles={"借阅","催还"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_layout);
        View view=getWindow().getDecorView();
        int option=View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(option);
       String cookie= SharedPreferenceUtil.getStringValue(PersonalActivity.this,SharedPreferenceUtil.COOKIE,"");

        Log.e("onCreat","onCreat");
        init();
        if(!SharedPreferenceUtil.getStringValue(PersonalActivity.this,SharedPreferenceUtil.USERINFO,"").equals("")){
            initdata();
        }else {
            getInfoes(cookie);
        }
        //getBorrowed(cookie);

    }
    private void init(){
        personal_username= (TextView) findViewById(R.id.personal_username);
        userDepartment= (TextView) findViewById(R.id.userDepartment);
        userremarks= (TextView) findViewById(R.id.userRemarks);
        personal_exit= (ImageView) findViewById(R.id.personal_exit);
        personal_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Exit_Dialog exit_dialog=new Exit_Dialog(PersonalActivity.this, new Exit_Dialog.IcustomDialogEventListener() {
                    @Override
                    public void CustomDialogEvent() {
                        SharedPreferenceUtil.clear(PersonalActivity.this);
                      PersonalActivity.this.finish();
                    }
                },R.style.Dialog);
                exit_dialog.show();
            }
        });
        search_img= (ImageView) findViewById(R.id.search_img);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonalActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        tabLayout= (TabLayout) findViewById(R.id.tablayout);


        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new BorrowedActivity());
        fragmentList.add(new ExpiredActivity());
         viewPager= (AutoHeightViewPager) findViewById(R.id.viewPager);
        MyViewPagerAdapter mViewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),titles,fragmentList);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initdata(){
        personal_username.setText(SharedPreferenceUtil.getStringValue(PersonalActivity.this,SharedPreferenceUtil.USERNAME,""));
        userDepartment.setText(SharedPreferenceUtil.getStringValue(PersonalActivity.this,SharedPreferenceUtil.UserDepartment,""));
        userremarks.setText(SharedPreferenceUtil.getStringValue(PersonalActivity.this,SharedPreferenceUtil.UserRemarks,""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");

    }

    //获取个人信息
    private void getInfoes(String cookie){
        HttpUtil.sendHttpRequestWithHttpURLConnection(UrlConfig.infoes + cookie, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object=new JSONObject(response);
                            String status=object.getString("status");
                            if(status.equals("success")) {
                                JSONObject jsonObject = object.getJSONObject("infoes");
                                SharedPreferenceUtil.setStringValue(PersonalActivity.this,SharedPreferenceUtil.USERINFO, String.valueOf(jsonObject));
                                String userName = jsonObject.getString("userName");
                                personal_username.setText(userName);
                                SharedPreferenceUtil.setStringValue(PersonalActivity.this, SharedPreferenceUtil.USERNAME, userName);
                                String useDepartment = jsonObject.getString("userDepartment");
                                userDepartment.setText(useDepartment);
                                SharedPreferenceUtil.setStringValue(PersonalActivity.this, SharedPreferenceUtil.UserDepartment, useDepartment);
                                String userRemarks = jsonObject.getString("userRemarks");
                                userremarks.setText(userRemarks);
                                SharedPreferenceUtil.setStringValue(PersonalActivity.this, SharedPreferenceUtil.UserRemarks, userRemarks);
                                Log.e("infoes",jsonObject+"");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalActivity.this, "可能网络炸了...请稍后重试", Toast.LENGTH_LONG).show();
                    }
                });
                PersonalActivity.this.finish();
            }
        });
    }
//    /**
//     * 重新设置ViewPager高度
//     * @param position
//     */
//    public void resetViewPagerHeight(int position){
//        View child=viewPager.getChildAt(position);
//        if(child!=null){
//            child.measure(0,0);
//            int h=child.getMeasuredHeight();
//            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) viewPager.getLayoutParams();
//            params.height=h+50;
//            viewPager.setLayoutParams(params);
//        }
//    }
//    public class  myAsyncTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                Thread.sleep(1000);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            resetViewPagerHeight(0);
//        }
//    }
}
