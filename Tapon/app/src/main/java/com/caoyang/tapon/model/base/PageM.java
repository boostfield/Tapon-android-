package com.caoyang.tapon.model.base;

import java.util.List;

public class PageM<U> {
    public List<U> getDataList() {
        return DataList;
    }

    public int getTotal() {
        return TotalCount;
    }

    private List<U> DataList;
    private int TotalCount;

    public PageM(List<U> dataList, int total) {
        this.DataList = dataList;
        this.TotalCount = total;
    }

    public PageM() {
    }




}
