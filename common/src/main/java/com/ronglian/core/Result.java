package com.ronglian.core;

import java.io.Serializable;

/**
 * @description: result
 * @author: tianjin
 * @email: eternity_bliss@sina.cn
 * @create: 2018-07-16 上午11:26
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -4306281920927310341L;
    public static final int SUCCESS = 200;
    public static final int FAILURE = 500;


    private T data;

    private int code = SUCCESS;
    private String info = "success";

    public Result() {
        super();
    }

    public Result(T data) {
        super();
        this.data = data;
    }

    public Result(Throwable throwable) {
        this.info = throwable.getLocalizedMessage();
        this.code = FAILURE;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
