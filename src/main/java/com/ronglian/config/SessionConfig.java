package com.ronglian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月3日 上午10:38:37
* @description:session 共享
*/
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

}
