package com.caoyang.tapon.adapter.listview.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.caoyang.tapon.model.base.BaseM;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 艹羊
 * @version V1.0.0
 * @detail 适用于AbsListView(ListView, GridView)的通用适配器, 子类继承即可使用
 */
public abstract class BaseListAdapter<U> extends BaseAdapter {
    protected OnItemClick onItemClick;


    private List<U> mData = new ArrayList<U>();
    protected Context mContext;


    public BaseListAdapter(Context context) {
        mContext = context;
    }

    public BaseListAdapter(Context context, List<U> data) {
        mContext = context;
        mData = data;
    }

    public void setData(List data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    public void addData(List data) {
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void insertData(U item, int atIndex) {
        this.mData.add(atIndex, item);
        this.notifyDataSetChanged();
    }

    public List<U> getData() {
        return mData;
    }


    public final void removeData(U item) {
        this.mData.remove(item);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public U getItem(int position) {
        return mData.get(position);
    }

    protected U getReverseItem(int position) {
        return mData.get((getCount() - 1) - position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void OnClick(BaseM baseM);
    }

    //获取Viewholder
    protected abstract BaseViewHolder getViewHolder(View itemView);

    //获取布局
    protected abstract int getItemLayoutRes();

    protected abstract void ItemVIewChange(BaseViewHolder baseViewHolder, final int position);


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseViewHolder baseViewHolder = null;
        if (convertView != null) {
            baseViewHolder = (BaseViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(this.mContext).inflate(getItemLayoutRes(), parent, false);
            baseViewHolder = getViewHolder(convertView);
            convertView.setTag(baseViewHolder);
        }
        ItemVIewChange(baseViewHolder, position);
        return convertView;
    }
}
