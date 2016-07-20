package com.byl.mvpdemo.ui.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.adapter.BaseAdapter;
import com.byl.mvpdemo.adapter.NewsAdapter;
import com.byl.mvpdemo.model.modelbean.ImageModel;
import com.byl.mvpdemo.model.modelbean.News;
import com.byl.mvpdemo.model.modelbean.NewsBean;
import com.byl.mvpdemo.model.mvpview.NewsMvpView;
import com.byl.mvpdemo.presenter.NewsPresenter;
import com.byl.mvpdemo.ui.base.BaseFragment;
import com.byl.mvpdemo.util.SysUtil;
import com.byl.mvpdemo.view.pullrecyclerview.PullBaseView;
import com.byl.mvpdemo.view.pullrecyclerview.PullRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class FragmentTab1 extends BaseFragment implements
        BaseAdapter.OnItemClickListener,
        PullBaseView.OnHeaderRefreshListener,
        PullBaseView.OnFooterRefreshListener,
        PullBaseView.OnPullDownScrollListener,
        View.OnClickListener,
        BaseAdapter.OnViewClickListener,
        NewsMvpView {

    RelativeLayout rl_title;
    TextView tv_search;
    ImageView iv_qr;//二维码
    PullRecyclerView mRecyclerView;
    NewsAdapter newsAdapter;
    List<Object> listbanner, listnews;

    int y, //滑动距离
            bannerH;//banner高度
    boolean isPullDown = false;//是否是下拉状态

    NewsPresenter newsPresenter;
    int page;

    @Override
    public int getContentView() {
        return R.layout.fragment_1;
    }

    /**
     * 注意：rl_title.getBackground().setAlpha(0)，设置标题栏背景透明度，会影响其它界面的背景，
     * 如果加上这个效果，那么其他界面的背景色在xml中设置将失效，需代码中动态设置，
     * 这个问题很奇葩，未找到原因，
     * 现在要么不要这个滑动改变透明度的效果，要么其他界面中的背景色就动态设置
     */
    @Override
    public void initView() {
        rl_title = (RelativeLayout) $(R.id.rl_title);//标题栏
        rl_title.getBackground().setAlpha(0);
        tv_search = (TextView) $(R.id.tv_search);
        iv_qr = (ImageView) $(R.id.iv_qr);

        mRecyclerView = (PullRecyclerView) $(R.id.mRecyclerView);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setOnHeaderRefreshListener(this);//设置下拉监听
        mRecyclerView.setOnFooterRefreshListener(this);//设置上拉监听
        mRecyclerView.setOnPullDownScrollListener(this);//设置下拉滑动监听
        mRecyclerView.setCanScrollAtRereshing(false);//设置正在刷新时是否可以滑动，默认不可滑动
        mRecyclerView.setCanPullDown(true);//设置是否可下拉
        mRecyclerView.setCanPullUp(true);//设置是否可上拉
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {//监听滑动距离以改变标题栏透明度
                super.onScrolled(recyclerView, dx, dy);
                y += dy;
                if (y >= bannerH) {
                    rl_title.getBackground().setAlpha(255);
                    rl_title.setVisibility(View.VISIBLE);
                } else if (y >= 0 && y < bannerH) {
                    if (isPullDown) {
                        rl_title.setVisibility(View.GONE);
                    } else {
                        rl_title.getBackground().setAlpha((int) (255 * ((double) y / bannerH)));
                        rl_title.setVisibility(View.VISIBLE);
                    }
                } else {
                    rl_title.getBackground().setAlpha(0);
                    rl_title.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void initClick() {
        tv_search.setOnClickListener(this);
        iv_qr.setOnClickListener(this);
    }

    @Override
    public void initData() {
        bannerH = SysUtil.dip2px(context, 200);//将banner高度转为px
        listbanner = new ArrayList<>();
        listnews = new ArrayList<>();

        newsPresenter = new NewsPresenter(context);
        newsPresenter.attachView(this);
        newsPresenter.getNews("10", String.valueOf(++page));

        initRecyclerView();
    }

    void initRecyclerView() {
        newsAdapter = new NewsAdapter(context, listbanner, listnews, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        newsAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_qr:
                Toast.makeText(context, "二维码", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 上拉加载
     *
     * @param view
     */
    @Override
    public void onFooterRefresh(PullBaseView view) {
        newsPresenter.getNews("10", String.valueOf(++page));
    }

    /**
     * 下拉刷新
     *
     * @param view
     */
    @Override
    public void onHeaderRefresh(PullBaseView view) {
        page = 0;
        newsPresenter.getNews("10", String.valueOf(++page));
    }

    @Override
    public void onPullDownScrolled() {
        isPullDown = true;
        rl_title.setVisibility(View.GONE);
    }

    @Override
    public void onPullDownFinished() {
        isPullDown = false;
        rl_title.setVisibility(View.VISIBLE);
    }


    /**
     * item点击监听
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, ((News) listnews.get(position)).getTitle(), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param position item position
     * @param viewtype 点击的view的类型，调用时根据不同的view传入不同的值加以区分
     */
    @Override
    public void onViewClick(int position, int viewtype) {
        switch (viewtype) {
            case 1:
                Toast.makeText(context, "新闻1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context, "新闻2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "新闻3", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(context, "新闻4", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 下拉请求成功
     *
     * @param newsBean
     */
    @Override
    public void refreshSuccess(NewsBean newsBean) {
        mRecyclerView.onHeaderRefreshComplete();
        //banner 模拟数据
        listbanner.clear();
        ImageModel imageModel = new ImageModel();
        imageModel.setUrl("https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=650d5402a318972bbc3a07cad6cd7b9d/9f2f070828381f305c3fe5bfa1014c086e06f086.jpg");
        listbanner.add(imageModel);
        imageModel = new ImageModel();
        imageModel.setUrl("https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=a219dde79125bc31345d06986ede8de7/a5c27d1ed21b0ef494399077d5c451da80cb3ec1.jpg");
        listbanner.add(imageModel);
        imageModel = new ImageModel();
        imageModel.setUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2040796625,1810502195&fm=111&gp=0.jpg");
        listbanner.add(imageModel);
        //news 真实数据
        listnews.clear();
        List<News> list = newsBean.getShowapi_res_body().getNewslist();
        for (News news : list) {
            listnews.add(news);
        }
        initRecyclerView();
    }

    @Override
    public void loadMoreSuccess(NewsBean newsBean) {
        mRecyclerView.onFooterRefreshComplete();
        List<News> list = newsBean.getShowapi_res_body().getNewslist();
        for (News news : list) {
            listnews.add(news);
        }
        newsAdapter.notifyDataSetChanged();
    }

    /**
     * 请求失败
     *
     * @param message
     */
    @Override
    public void fail(String message) {
        mRecyclerView.onHeaderRefreshComplete();
        mRecyclerView.onFooterRefreshComplete();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        newsPresenter.detachView();
    }
}
