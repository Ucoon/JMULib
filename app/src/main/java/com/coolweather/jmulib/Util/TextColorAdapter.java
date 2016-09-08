package com.coolweather.jmulib.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.jmulib.R;
import com.coolweather.jmulib.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongJie on 2016/9/4.
 */
public class TextColorAdapter  extends BaseAdapter{
    private Context context;
    private List<Model> models;
    public TextColorAdapter(Context context, List<Model> model){
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
            view1= LayoutInflater.from(context).inflate(R.layout.search_result_item,null);
            viewHolder=new ViewHolder();
            viewHolder.book_name= (TextView) view1.findViewById(R.id.search_book_name);
            viewHolder.book_author= (TextView) view1.findViewById(R.id.search_book_author);
           // viewHolder.book_location= (TextView) view1.findViewById(R.id.book_location);
            viewHolder.book_num_many= (TextView) view1.findViewById(R.id.book_num_many);
            viewHolder.book_publisher= (TextView) view1.findViewById(R.id.book_publisher);
            viewHolder.book_number= (TextView) view1.findViewById(R.id.book_number);
            viewHolder.book_callnum= (TextView) view1.findViewById(R.id.book_callnum);
            viewHolder.book_bg= (ImageView) view1.findViewById(R.id.search_book_bg);
            view1.setTag(viewHolder);
        }else{
            view1=view;
            viewHolder= (ViewHolder) view1.getTag();
        }
        viewHolder.book_bg.setImageResource(model.getImageid());
        viewHolder.book_name.setText(model.getBook_name());
        viewHolder.book_author.setText(model.getAuthor());
     //   viewHolder.book_location.setText(model.getLocation());
        viewHolder.book_num_many.setText(model.getTotal());
        viewHolder.book_publisher.setText(model.getPublisher());
        viewHolder.book_number.setText(model.getBookNumber());
        viewHolder.book_callnum.setText(model.getNowbooks());
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
        ImageView book_bg;
        TextView book_author;
      //  TextView book_location;
        TextView book_num_many;
        TextView book_publisher;
        TextView book_number;
        TextView book_callnum;

    }
}
