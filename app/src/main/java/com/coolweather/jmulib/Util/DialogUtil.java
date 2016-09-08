package com.coolweather.jmulib.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.coolweather.jmulib.R;

/**
 * Created by ZongJie on 2016/9/6.
 */
public class DialogUtil {
    public static Dialog createLoadingDialog(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_loading_dialog,null);
        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.dialog_view);//加载布局
        Dialog loadingDialog=new Dialog(context,R.style.loading_dialog);
        loadingDialog.setCancelable(false);//不可以按返回键取消
        loadingDialog.setContentView(linearLayout,new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }
}
