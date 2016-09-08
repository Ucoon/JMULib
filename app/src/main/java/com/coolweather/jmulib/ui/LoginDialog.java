package com.coolweather.jmulib.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coolweather.jmulib.R;

/**
 * Created by ZongJie on 2016/8/29.
 */
public class LoginDialog extends Dialog {
    EditText user_text;
    EditText pw_text;
    //增加一个回调函数，用以从外部接收返回值
    public  interface IcustomDialogEventListener{
        public void CustomDialogEvent(String text1,String text2);
    }

    private IcustomDialogEventListener icustomDialogEventListener;
    private Context context;
    public LoginDialog(Context context,IcustomDialogEventListener listener,int theme) {
        super(context,theme);
        this.context=context;
        this.icustomDialogEventListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.login_dialog,null);
        user_text= (EditText) view.findViewById(R.id.user_login_account);
//        user_text.setHintTextColor(getContext().getResources().getColor(R.color.colorAccent));
        pw_text= (EditText) view.findViewById(R.id.user_login_pw);
//        pw_text.setHintTextColor(getContext().getResources().getColor(R.color.colorAccent));
        Button login_btn= (Button) view.findViewById(R.id.user_login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user_text.getText().toString().equals("")&&!pw_text.getText().toString().equals("")){
                    icustomDialogEventListener.CustomDialogEvent(user_text.getText().toString(),pw_text.getText().toString());
                }else{
                    Toast.makeText(getContext(),"学号及密码不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        this.setContentView(view,new LinearLayout.LayoutParams((int) (getWindow().getWindowManager().getDefaultDisplay().getWidth()*0.85),
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
