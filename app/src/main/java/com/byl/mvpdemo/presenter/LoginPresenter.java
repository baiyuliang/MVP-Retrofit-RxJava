package com.byl.mvpdemo.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.byl.mvpdemo.api.ApiService;
import com.byl.mvpdemo.model.modelbean.LoginBean;
import com.byl.mvpdemo.model.mvpview.LoginMvpView;
import com.byl.mvpdemo.presenter.base.BasePresenter;
import com.byl.mvpdemo.util.LogUtil;
import com.byl.mvpdemo.view.MyProgressDialog;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by baiyuliang on 2016-7-14.
 */
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    Subscription mSubscription;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void login(String account) {
        checkViewAttached();
        showProgressDialog();
        mSubscription = getApiService().login(account)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dissmissProgressDialog();
                        getMvpView().loginFail("登录失败");
                        LogUtil.e("登录失败>>" + e.getMessage());
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        dissmissProgressDialog();
                        if (!loginBean.getShowapi_res_code().equals("0")
                                || loginBean.getShowapi_res_body() == null
                                || TextUtils.isEmpty(loginBean.getShowapi_res_body().getRet_code())
                                || !loginBean.getShowapi_res_body().getRet_code().equals("0")) {
                            getMvpView().loginFail("请检查您输入的手机号码是否正确");
                        } else {
                            getMvpView().loginSuccess(loginBean);
                        }
                    }
                });
    }

}
