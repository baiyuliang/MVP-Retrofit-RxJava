package com.byl.mvpdemo.api;

import com.byl.mvpdemo.util.LogUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baiyuliang on 2016-7-14.
 */
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
