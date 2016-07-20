package com.byl.mvpdemo.ui.main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.ui.base.BaseFragment;
import com.byl.mvpdemo.util.LogUtil;


/**
 */
public class FragmentTab3 extends BaseFragment {

    LinearLayout ll_head;

    @Override
    public int getContentView() {
        return R.layout.fragment_3;
    }

    @Override
    public void initView() {
        initTitleBar("", "我的", "", 0, this);
        ll_head = (LinearLayout) $(R.id.ll_head);
        ll_head.setBackgroundColor(getResources().getColor(R.color.common_title_bg));
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        LogUtil.e("FragmentTab3");
    }

    @Override
    public void onClick(View v) {

    }
}
