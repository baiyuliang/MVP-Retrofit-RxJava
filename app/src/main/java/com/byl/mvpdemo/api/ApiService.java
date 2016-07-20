package com.byl.mvpdemo.api;

import com.byl.mvpdemo.model.modelbean.LoginBean;
import com.byl.mvpdemo.model.modelbean.NewsBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by baiyuliang on 2016-7-14.
 */
public interface ApiService {

    String API_ROOT = "http://route.showapi.com/";

    /**
     * 登录
     * 模拟登录，实际为手机号归属地查询接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("6-1")
    Observable<LoginBean> login(@Field("num") String num);

    /**
     * 新闻列表
     *
     * @param num
     * @param page
     * @return
     */
    @GET("198-1")
    Observable<NewsBean> news(@Query("num") String num, @Query("page") String page);

    /**
     * 美女图片列表
     * 参数类型与新闻列表相同，因此公用NewsBean
     *
     * @param num
     * @param page
     * @param rand 1.随机 0不随机
     * @return
     */
    @GET("197-1")
    Observable<NewsBean> girls(@Query("num") String num, @Query("page") String page, @Query("rand") String rand);

}
