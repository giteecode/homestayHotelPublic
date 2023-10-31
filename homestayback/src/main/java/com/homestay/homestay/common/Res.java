package com.homestay.homestay.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/09/21/15:03
 * @Description:
 */


public class Res<T> {
    private String code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Res() {
    }

    public Res(T data) {
        this.data = data;
    }

    public static Res success() {
        Res res = new Res<>();
        res.setCode("200");
        res.setMsg("成功");
        return res;
    }

    public static <T> Res<T> success(T data) {
        Res<T> res = new Res<>(data);
        res.setCode("200");
        res.setMsg("成功");
        return res;
    }

    public static Res error(String code, String msg) {
        Res res = new Res();
        res.setCode(code);
        res.setMsg(msg);
        return res;
    }
}


