package com.byl.mvpdemo.presenter.base;

import android.content.Context;

import com.byl.mvpdemo.api.ApiService;
import com.byl.mvpdemo.api.ApiUtil;
import com.byl.mvpdemo.model.mvpview.base.MvpView;
import com.byl.mvpdemo.view.MyProgressDialog;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    public Context context;
    private T mMvpView;
    private ApiService apiService;
    private MyProgressDialog myProgressDialog;


    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
        apiService = ApiUtil.createApiService();
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void showProgressDialog() {
        if (context != null) {
            if (myProgressDialog == null) {
                myProgressDialog = new MyProgressDialog(context);
            }
            myProgressDialog.show();
        }
    }

    public void dissmissProgressDialog() {
        if (myProgressDialog != null) {
            myProgressDialog.dismiss();
        }
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("使用Presenter前，请先调用attachView");
        }
    }
}

