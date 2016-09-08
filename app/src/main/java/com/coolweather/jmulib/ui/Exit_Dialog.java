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

import com.coolweather.jmulib.R;

/**
 * Created by ZongJie on 2016/8/30.
 */
public class Exit_Dialog extends Dialog {
    //增加一个回调函数，用以从外部接收返回值
    public  interface IcustomDialogEventListener{
        public void CustomDialogEvent();
    }

    private IcustomDialogEventListener icustomDialogEventListener;
    private Context context;
    public Exit_Dialog(Context context,IcustomDialogEventListener listener,int theme) {
        super(context,theme);
        this.context=context;
        this.icustomDialogEventListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.exit_dialog,null);
        Button exit_btn= (Button) view.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icustomDialogEventListener.CustomDialogEvent();
            }
        });
        Button cancel_btn= (Button) view.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.setContentView(view,new LinearLayout.LayoutParams((int) (getWindow().getWindowManager().getDefaultDisplay().getWidth()*0.85),
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
