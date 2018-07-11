package com.ronglian.service;


import com.ronglian.model.Demo;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月8日 上午11:26:59
* @description:描述
*/
public interface DemoService {
	   
    public Demo findDemoFromRedis(String key);

}
