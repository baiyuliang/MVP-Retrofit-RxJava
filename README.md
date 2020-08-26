# MVP-Retrofit-RxJava（项目年久失修，不再建议使用）
Android MVP+Retrofit+RxJava项目实践

1.登录          2.首页          3.图片

![登录](http://img.blog.csdn.net/20160720171017747)![首页](http://img.blog.csdn.net/20160720171056528)![图片](http://img.blog.csdn.net/20160720171615828)

整个项目使用MVP架构，导航栏使用TabLayout+ViewPager+Fragment，网络请求部分则使用目前流行的Retrofit+RxJava！

下拉刷新： PullRecylerView：https://github.com/baiyuliang/PullRecyclerView
CSDN：http://blog.csdn.net/baiyuliang2013/article/details/51516727

部分代码：

LoginActivity：

```
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
```

网络请求核心代码：
ApiUtil：

```
public class ApiUtil {

    public static ApiService createApiService() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);//设置请求超时时间10s
        OkHttpClient client = okHttpClientBuilder
                .addInterceptor((chain) -> {//添加统一参数
                    Request request = chain.request();
                    HttpUrl url = request.url()
                            .newBuilder()
                            .addQueryParameter("showapi_appid", "20676")
                            .addQueryParameter("showapi_sign", "f730cd8c4cf8498895f83d43ddaba8c2")
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .addInterceptor((chain) -> {//log拦截器
                    Request request = chain.request();
                    LogUtil.v("request>>" + request.toString());
                    Response response = chain.proceed(chain.request());
                    MediaType mediaType = response.body().contentType();
                    String content = response.body().string();
                    LogUtil.i("response>>" + content);
                    return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
                })
                .hostnameVerifier((hostname, session) -> true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.API_ROOT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

}
```

#### 已知bug：

首页使用RecyclerView+Header的模式，整体为一个RecyclerView，因RecycleView没有类似ScrollView的滑动监听，因此测量RecycleView滑动距离时，在item数量变化情况下会影响测量结果，导致首页标题栏跟随手指滑动透明效果出现问题，此案例只作为学习使用，实际项目中可以使用阿里的vlayout，强大易用！

