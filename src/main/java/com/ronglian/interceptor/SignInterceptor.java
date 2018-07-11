package com.ronglian.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.model.User;
import com.ronglian.service.impl.UserServiceImpl;
import com.ronglian.utils.RongLianConstant;
import com.ronglian.utils.RongLianResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: SignInterceptor
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月13日 下午4:15:32
 */
@Slf4j
@Component
public class SignInterceptor implements HandlerInterceptor {

	@Resource
	private UserServiceImpl userService;

	private static final String LOGIN_URL = RongLianConstant.LOGIN_URL;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.info("【进入】拦截器preHandle："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));

		// 放行登录login的请求
		StringBuffer url = request.getRequestURL();
		if (url.toString().indexOf(LOGIN_URL) != -1) {
			return true;
		}
		if (url.toString().indexOf(RongLianConstant.API_URL) != -1) {
			return true;
		}
		if (url.toString().indexOf(RongLianConstant.DEMO_URL) != -1) {
			return true;
		}
		String userName = null;
		HttpSession session = request.getSession();
		userName = (String) session.getAttribute("username");
		if (userName == null) {
			returnErrorMessage(response, "未登录或登录超时", 103);
			return false;
		}
		if (userService == null) {
			BeanFactory factory = WebApplicationContextUtils
					.getRequiredWebApplicationContext(request
							.getServletContext());
			userService = (UserServiceImpl) factory.getBean("userService");
		}
		log.info("【结束-返回】拦截器preHandle："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		User user = this.userService.findUserByName(userName);
		if (user != null) {
			return true;
		} else {
			returnErrorMessage(response, "非法用户侵入", 102);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		log.info("【进入】拦截器postHandle："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("【进入】拦截器afterCompletion："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));

	}

	private void returnErrorMessage(HttpServletResponse response,
			String errorMessage, int code) throws IOException {
		RongLianResult rst = new RongLianResult();
		RongLianResult result = rst.build(code, errorMessage);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		// Get the printwriter object from response to write the required json
		// object to the output stream
		PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following,
		// it will return your json object
		ObjectMapper mapper = new ObjectMapper();
		String jsonOfRST = mapper.writeValueAsString(result);
		out.print(jsonOfRST);
		out.flush();
	}

}
