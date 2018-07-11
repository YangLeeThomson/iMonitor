package com.ronglian.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月14日 上午9:04:06
* @description:描述
*/
@Slf4j
public class UserAuthenticationFilter implements Filter{
	
	/**
	 * 用户验证服务开关
	 * */
	@Value("$(imonitor.user.authentication.on-off)")
	private String on_off;
	/**
	 * 用户验证服务地址
	 * */
	@Value("$(imonitor.user.authentication.url)")
    private String userAuthenticationUrl;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if("on".equals(on_off)) {
			/**
			 * 1.获取url中携带的username和token
			 * 2.如果缺失用户名或者token，禁止访问服务，返回用户未登录信息
			 * 3.如果获取到用户名和token,则访问用户验证服务，验证用户是否已经登陆
			 * 4.登录过期，返回用户登录过期信息
			 * 5.用户不存在，返回用户不存在信息
			 * 6.验证成功，继续访问服务
			 * */
			log.info("用户验证");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
