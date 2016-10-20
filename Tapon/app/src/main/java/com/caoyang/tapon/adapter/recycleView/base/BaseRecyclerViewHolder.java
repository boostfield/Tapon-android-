package com.caoyang.tapon.adapter.recycleView.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T findViewById(int viewId) {
        View view = itemView.findViewById(viewId);
        return (T) view;
    }
}
