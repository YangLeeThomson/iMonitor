package com.ronglian.service;

import java.io.Serializable;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月14日 下午4:15:05
* @description:描述
*/
public interface TransFunction<F, T> extends Serializable{
    T transfer(F f);
}