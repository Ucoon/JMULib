package com.coolweather.jmulib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Created by ZongJie on 2016/8/12.
 */
public class ListViewForScrollview extends ListView {
    public ListViewForScrollview(Context context) {
        super(context);
    }

    public ListViewForScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expanSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expanSpec);
    }
}
