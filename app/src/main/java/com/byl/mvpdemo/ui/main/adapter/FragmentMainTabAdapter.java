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

public class FragmentMainTabAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    private Fragment[] mFragments = null;
    private String[] mFragmentTitles =null;
    private Integer[] mFragmentIcons = null;

    public FragmentMainTabAdapter(Context context, FragmentManager fm, Fragment[] fragments, String[] titles, Integer[] icons) {
        super(fm);
        this.mContext = context;
        mFragments = fragments;
        mFragmentTitles = titles;
        mFragmentIcons = icons;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles[position];
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext).inflate(R.layout.item_main_tab, null);
        TextView tabText = (TextView) tab.findViewById(R.id.tv_title);
        ImageView tabImage = (ImageView) tab.findViewById(R.id.iv_icon);
        tabText.setText(mFragmentTitles[position]);
        tabImage.setBackgroundResource(mFragmentIcons[position]);
        if (position == 0) {
            tab.setSelected(true);
        }
        return tab;
    }
}
