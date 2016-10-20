package com.caoyang.tapon.adapter.pageradapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.caoyang.tapon.base.BaseFragment;

import java.util.List;


public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragment;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragment.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    /**
     * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
     */
    @Override
    public void finishUpdate(View container) {
        super.finishUpdate(container);
    }
}
