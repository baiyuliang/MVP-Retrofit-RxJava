package com.byl.mvpdemo.ui.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.ui.base.BaseActivity;
import com.byl.mvpdemo.ui.main.adapter.FragmentMainTabAdapter;


/**
 * 主页
 */
public class MainActivity extends BaseActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    private String[] mTabTitles;
    private Integer[] mTabIcons;
    private Fragment[] mFragments;
    private FragmentMainTabAdapter fragmentMainTabAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) $(R.id.vp_content);
        tabLayout = (TabLayout) $(R.id.tabl_navigation);
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        mTabTitles = new String[]{"新闻", "图片", "我的"};
        mTabIcons = new Integer[]{R.drawable.tab1_selector, R.drawable.tab2_selector, R.drawable.tab3_selector};
        mFragments = new Fragment[]{new FragmentTab1(), new FragmentTab2(), new FragmentTab3()};
        setupViewPager();
        setupTabLayout();
        tabLayout.getTabAt(0).select();
    }

    private void setupTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(fragmentMainTabAdapter.getTabView(i));
        }
        tabLayout.requestFocus();
    }

    private void setupViewPager() {
        fragmentMainTabAdapter = new FragmentMainTabAdapter(context, getSupportFragmentManager(), mFragments, mTabTitles, mTabIcons);
        viewPager.setAdapter(fragmentMainTabAdapter);
        viewPager.setOffscreenPageLimit(2);
    }


}
