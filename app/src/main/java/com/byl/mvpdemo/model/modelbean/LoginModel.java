package com.byl.mvpdemo.model.modelbean;

/**
 * Created by baiyuliang on 2016-7-14.
 * <p>
 * prov	String	四川	省
 * city	String	test	市
 * name	String	test	运营商
 * num	Number	1370011	号段
 * provCode	Number	110000	省别编码
 * type	Number	1	1为移动 2为电信 3为联通
 */
public class LoginModel{

    private String ret_code;
    private String prov;
    private String city;
    private String name;
    private String num;
    private String provCode;
    private String type;

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
