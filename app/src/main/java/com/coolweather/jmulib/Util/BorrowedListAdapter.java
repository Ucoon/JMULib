package com.coolweather.jmulib.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.jmulib.R;
import com.coolweather.jmulib.ui.Model;

import java.util.List;

/**
 * Created by ZongJie on 2016/8/30.
 */
public class BorrowedListAdapter extends BaseAdapter {
    private Context context;
    private List<Model> models;
    public BorrowedListAdapter(Context context, List<Model> model){
        this.context=context;
        this.models=model;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Model model= models.get(i);
        View view1;
        ViewHolder viewHolder;
        if(view==null){
            view1= LayoutInflater.from(context).inflate(R.layout.list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.book_name= (TextView) view1.findViewById(R.id.book_name);
            viewHolder.rawValue= (TextView) view1.findViewById(R.id.rawValue);
            viewHolder.outTime= (TextView) view1.findViewById(R.id.outTime);
            viewHolder.renewable= (TextView) view1.findViewById(R.id.renewable);
            viewHolder.book_bg= (ImageView) view1.findViewById(R.id.book_bg);
            view1.setTag(viewHolder);
        }else{
            view1=view;
            viewHolder= (ViewHolder) view1.getTag();
        }
        viewHolder.book_bg.setImageResource(model.getImageid());
        viewHolder.book_name.setText(model.getBook_name());
        viewHolder.rawValue.setText(model.getRawValue()+"应还");
        viewHolder.outTime.setText("还剩"+model.getOutTime()+"天");
        if(model.getRenewable()){
            viewHolder.renewable.setBackground(context.getResources().getDrawable(R.drawable.list_text_bg));
        }else{
            viewHolder.renewable.setVisibility(View.GONE);
        }
        return view1;
    }

    @Override
    public Object getItem(int i) {
        if (models.size() == 0) {
            return null;
        }
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    class ViewHolder{
        TextView book_name;
        TextView rawValue;
        TextView renewable;
        TextView outTime;
        ImageView book_bg;
    }
}

