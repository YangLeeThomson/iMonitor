/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.config 
 * @author: YeohLee   
 * @date: 2018年6月22日 下午8:19:18 
 */
package com.ronglian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 /** 
 * @ClassName: WebSecurityConfig 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月22日 下午8:19:18  
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        
                // 关闭csrf保护功能（跨域访问）
		http.csrf().disable().authorizeRequests().antMatchers("/api/**").permitAll();//访问API下无需登录认证权限
    } 
}
