package com.caoyang.tapon.adapter.pageradapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.caoyang.tapon.base.BaseFragment;

import java.util.List;

/**
 * 适合多个fragment，内存占用少
 */
public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitles;
    private List<BaseFragment> mFragments;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<String> mTitles, List<BaseFragment> mFragments) {
        super(fm);
        this.mTitles = mTitles;
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

}
