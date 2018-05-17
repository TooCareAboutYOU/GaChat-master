package com.gachat.main.util.widgets;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by admin on 2018/4/8.
 */

public class PullOnLoadSwipeRefreshLayout extends SwipeRefreshLayout {


    public PullOnLoadSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public PullOnLoadSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
