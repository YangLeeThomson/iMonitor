package com.ronglian.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ronglian.model.Demo;
import com.ronglian.service.DemoService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月2日 下午2:18:55
* @description:描述
*/
@Slf4j
@Service(value = "demoService")
//@Transactional
public class DemoServiceImpl implements DemoService{
	
	

	@Override
	@Cacheable(value = "democache", keyGenerator = "wiselyKeyGenerator")
	public Demo findDemoFromRedis(String key) {
		// TODO Auto-generated method stub
		log.info("无缓存的时候调用这里---key:"+key);
		return new Demo(222,"demo222");
	}

}
