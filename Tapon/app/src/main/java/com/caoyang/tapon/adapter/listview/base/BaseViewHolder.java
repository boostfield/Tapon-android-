package com.caoyang.tapon.adapter.listview.base;

import android.view.View;

/**
 * Author：艹羊
 * Created Time:2016/09/23 15:16
 */

public class BaseViewHolder {
    private View itemView;

    public BaseViewHolder(View view) {
        this.itemView = view;
    }

    public <T extends View> T findViewById(int viewId) {
        View view = itemView.findViewById(viewId);
        return (T) view;
    }
}
