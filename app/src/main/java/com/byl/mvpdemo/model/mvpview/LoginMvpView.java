package com.byl.mvpdemo.model.mvpview;

import com.byl.mvpdemo.model.modelbean.LoginBean;
import com.byl.mvpdemo.model.mvpview.base.MvpView;

/**
 * Created by baiyuliang on 2016-7-14.
 */
public interface LoginMvpView extends MvpView {

    /**
     * 登录成功
     * @param loginBean
     */
    void loginSuccess(LoginBean loginBean);

    /**
     * 登录失败
     * @param message
     */
    void loginFail(String message);
}
