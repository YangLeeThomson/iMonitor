package com.ronglian.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ronglian.filter.UserAuthenticationFilter;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月14日 上午9:12:00
* @description:filter配置类
*/
@Configuration
public class FilterConfiguration {
	@Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new UserAuthenticationFilter());
        //拦截规则
        registration.addUrlPatterns("/imonitor/*");
        //过滤器名称
        registration.setName("UserAuthenticationFilter");
        //是否自动注册 false 取消Filter的自动注册
        registration.setEnabled(false);
        //过滤器顺序
        registration.setOrder(1);
        return registration;
    }
}
