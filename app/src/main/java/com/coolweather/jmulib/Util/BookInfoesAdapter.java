package com.coolweather.jmulib.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coolweather.jmulib.R;
import com.coolweather.jmulib.ui.Model;

import java.util.List;

/**
 * Created by ZongJie on 2016/9/5.
 */
public class BookInfoesAdapter extends ArrayAdapter<Model> {
    private int resourceId;
    public BookInfoesAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Model bookModel=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.book_No= (TextView) view.findViewById(R.id.book_No);
            viewHolder.status_book= (TextView) view.findViewById(R.id.status_book);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.book_No.setText(bookModel.getBook_No());
        viewHolder.status_book.setText(bookModel.getStatus_book());
        return view;
    }
    class ViewHolder{
        TextView book_No;
        TextView status_book;
    }
}
