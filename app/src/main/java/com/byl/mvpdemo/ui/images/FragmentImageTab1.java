package com.byl.mvpdemo.ui.images;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.adapter.BaseAdapter;
import com.byl.mvpdemo.adapter.ImagesAdapter;
import com.byl.mvpdemo.model.modelbean.News;
import com.byl.mvpdemo.model.modelbean.NewsBean;
import com.byl.mvpdemo.model.mvpview.NewsMvpView;
import com.byl.mvpdemo.presenter.ImagesPresenter;
import com.byl.mvpdemo.ui.base.BaseFragment;
import com.byl.mvpdemo.view.pullrecyclerview.PullBaseView;
import com.byl.mvpdemo.view.pullrecyclerview.PullRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class FragmentImageTab1 extends BaseFragment implements NewsMvpView,
        PullBaseView.OnHeaderRefreshListener,
        PullBaseView.OnFooterRefreshListener,
        BaseAdapter.OnItemClickListener,
        BaseAdapter.OnViewClickListener {

    PullRecyclerView mPullRecyclerView;
    ImagesAdapter imagesAdapter;
    List<Object> objectList;

    ImagesPresenter imagesPresenter;
    int page;

    @Override
    public int getContentView() {
        return R.layout.fragment_images_tab1;
    }

    @Override
    public void initView() {
        mPullRecyclerView = (PullRecyclerView) $(R.id.mPullRecyclerView);
        mPullRecyclerView.setOnHeaderRefreshListener(this);//设置下拉监听
        mPullRecyclerView.setOnFooterRefreshListener(this);//设置上拉监听
        mPullRecyclerView.setCanScrollAtRereshing(false);//设置正在刷新时是否可以滑动，默认不可滑动
        mPullRecyclerView.setCanPullDown(true);//设置是否可下拉
        mPullRecyclerView.setCanPullUp(true);//设置是否可上拉
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        objectList = new ArrayList<>();
        imagesPresenter = new ImagesPresenter(context);
        imagesPresenter.attachView(this);
        imagesPresenter.getImages("20", String.valueOf(++page), "0");
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onFooterRefresh(PullBaseView view) {
        imagesPresenter.getImages("20", String.valueOf(++page), "0");
    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {
        page = 0;
        imagesPresenter.getImages("20", String.valueOf(++page), "0");
    }

    @Override
    public void refreshSuccess(NewsBean newsBean) {
        mPullRecyclerView.onHeaderRefreshComplete();
        objectList.clear();
        List<News> list = newsBean.getShowapi_res_body().getNewslist();
        for (News news : list) {
            objectList.add(news);
        }
        imagesAdapter = new ImagesAdapter(context, objectList, this);
        mPullRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        imagesAdapter.setOnItemClickListener(this);
        mPullRecyclerView.setAdapter(imagesAdapter);
    }

    @Override
    public void loadMoreSuccess(NewsBean newsBean) {
        mPullRecyclerView.onFooterRefreshComplete();
        List<News> list = newsBean.getShowapi_res_body().getNewslist();
        for (News news : list) {
            objectList.add(news);
        }
        imagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, "item>>" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewClick(int position, int viewtype) {
        switch (viewtype) {
            case 1:
                Toast.makeText(context, "赞>>" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imagesPresenter.detachView();
    }

}
