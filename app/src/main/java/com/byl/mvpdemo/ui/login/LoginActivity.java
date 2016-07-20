package com.byl.mvpdemo.ui.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.model.modelbean.LoginBean;
import com.byl.mvpdemo.model.mvpview.LoginMvpView;
import com.byl.mvpdemo.presenter.LoginPresenter;
import com.byl.mvpdemo.ui.base.BaseActivity;
import com.byl.mvpdemo.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    EditText et_account, et_pwd;
    Button btn_login;
    LoginPresenter loginPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        initTitleBar("", "登录", "注册", 0, this);
        title_bar.setBackgroundColor(getResources().getColor(R.color.common_title_bg));
        et_account = (EditText) $(R.id.account);
        et_pwd = (EditText) $(R.id.password);
        btn_login = (Button) $(R.id.login);
    }

    @Override
    public void initClick() {
        btn_login.setOnClickListener(this);
    }

    @Override
    public void initData() {
        loginPresenter = new LoginPresenter(this);
        loginPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login:
                doLogin();
                break;
        }
    }

    /**
     * 登录
     */
    void doLogin() {
        String account = et_account.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(LoginActivity.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        loginPresenter.login(account);
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        startActivity(MainActivity.class);
        finish();
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFail(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }
}

