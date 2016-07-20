package com.byl.mvpdemo.ui.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.byl.mvpdemo.R;

public class FragmentVedioAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    private Fragment[] mFragments = null;
    private String[] mFragmentTitles = null;

    public FragmentVedioAdapter(Context context, FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.mContext = context;
        mFragments = fragments;
        mFragmentTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles[position];
    }
}
