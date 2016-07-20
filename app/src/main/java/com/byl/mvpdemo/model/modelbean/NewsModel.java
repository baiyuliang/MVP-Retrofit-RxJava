package com.byl.mvpdemo.model.modelbean;

import java.util.List;

/**
 * 新聞
 * Created by baiyuliang on 2016-7-14.
 */
public class NewsModel {
    private String code;
    private String msg;
    private List<News> newslist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<News> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<News> newslist) {
        this.newslist = newslist;
    }
}
