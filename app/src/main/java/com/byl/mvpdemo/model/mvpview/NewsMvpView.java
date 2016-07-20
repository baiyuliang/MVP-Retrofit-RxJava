package com.byl.mvpdemo.model.mvpview;

import com.byl.mvpdemo.model.modelbean.NewsBean;
import com.byl.mvpdemo.model.mvpview.base.MvpView;

/**
 * Created by baiyuliang on 2016-7-14.
 */
public interface NewsMvpView extends MvpView {

    /**
     * 下拉刷新成功
     *
     * @param newsBean
     */
    void refreshSuccess(NewsBean newsBean);

    /**
     * 上拉加载成功
     *
     * @param newsBean
     */
    void loadMoreSuccess(NewsBean newsBean);

    /**
     * 失败
     *
     * @param message
     */
    void fail(String message);
}
