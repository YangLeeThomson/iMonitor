/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.controller 
 * @author: YeohLee   
 * @date: 2018年6月22日 下午4:29:33 
 */
package com.ronglian.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.User;
import com.ronglian.service.UserService;

 /** 
 * @ClassName: UserController 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月22日 下午4:29:33  
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	/** 
	* @Title: login 
	* @Description: TODO 
	* @param user
	* @return JsonResult
	* @author YeohLee
	* @date 2018年6月22日下午4:38:15
	*/ 
	@RequestMapping(value = "/login",method = RequestMethod.POST)
//	public JsonResult login(@RequestBody User user,HttpServletRequest request,HttpServletResponse response){
	public JsonResult login(@RequestParam String username,@RequestParam String password,HttpServletRequest request,HttpServletResponse response){
//		response.addHeader("Access-Control-Allow-Origin", "*");
		log.info("进入Controller_login："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST");
		response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return this.userService.login(user,request,response);

	}
	
	@RequestMapping(value = "/logout")
//	public JsonResult login(@RequestBody User user,HttpServletRequest request,HttpServletResponse response){
	public JsonResult logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		session.removeAttribute("username");
		session.removeAttribute("userId");
		System.out.println("Session的Id："+session.getId()+"   session时间"+session.getMaxInactiveInterval());
		return new JsonResult(ResultCode.SUCCESS, "success",null);
	}
	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
//	public JsonResult login(@RequestBody User user,HttpServletRequest request,HttpServletResponse response){
	public JsonResult findAllUsers(HttpServletRequest request,HttpServletResponse response){
		List<User> userList = this.userService.findUsers("1");
		return new JsonResult(ResultCode.SUCCESS, "success",userList);
	}
	
}
