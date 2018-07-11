package com.ronglian.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ronglian.common.JsonResult;
import com.ronglian.model.User;

/**
* @author: sunqian
* @date:2018年6月14日 上午11:26:59
* @description:描述
*/
public interface UserService {
	   
    public User findUserByName(String username);
    
    public JsonResult login(User user,HttpServletRequest request,HttpServletResponse response);
    
    public List<User> findUsers(String tenantId);

}
