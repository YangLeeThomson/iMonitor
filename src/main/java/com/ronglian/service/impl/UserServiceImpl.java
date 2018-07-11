
package com.ronglian.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.mapper.UserMapper;
import com.ronglian.model.User;
import com.ronglian.service.UserService;

import lombok.extern.slf4j.Slf4j;


/**
* @author: sunqian
* @date:2018年6月14日 下午2:18:55
* @description:描述
*/
@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public User findUserByName(String username) {
		return userMapper.findByName(username);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.UserService#login(com.ronglian.model.User)
	 */
	@Override
	public JsonResult login(User user,HttpServletRequest request,HttpServletResponse response) {
		// TODO Auto-generated method stub
		if(user == null || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getUsername())){
			return new JsonResult(ResultCode.USER_NULL,"未传参用户名或密码");
		}
		User loginner = this.userMapper.findByName(user.getUsername());
		if(loginner == null || !user.getPassword().equalsIgnoreCase(loginner.getPassword())){
			return new JsonResult(ResultCode.USER_ERROR,"用户名或密码不正确");
		}
		HttpSession session = request.getSession();
		System.out.println("Session的Id："+session.getId()
				+"  session时间："+session.getMaxInactiveInterval()
				);
		session.setAttribute("username", loginner.getUsername());
		session.setAttribute("userId",loginner.getId());
		
		log.info("【结束】Controller_login："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		return new JsonResult(ResultCode.SUCCESS, "success",user.getUsername());
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.UserService#findUsers(java.lang.String)
	 */
	@Override
	public List<User> findUsers(String tenantId) {
		// TODO Auto-generated method stub
		List<User> userList = this.userMapper.listByTenantId(tenantId);
		if(userList == null || userList.size()<1){
			return null;
		}
		return userList;
	}
	
}

