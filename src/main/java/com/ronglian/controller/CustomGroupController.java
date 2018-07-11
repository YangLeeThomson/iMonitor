package com.ronglian.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.model.Platform;
import com.ronglian.model.User;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.PlatformService;
import com.ronglian.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunqian
 * @date:2018年6月14日 上午9:59:07
 * @description:自定义检测项接口
 */

@RestController
@Slf4j
@RequestMapping("/customGroup")
public class CustomGroupController {

	@Autowired
	private CustomGroupService customGroupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	PlatformService platformService;

	/**
	 * 新建自定义检测组
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public JsonResult createGroup(@RequestParam(name = "platformList", required = true) List<String> platformList,
			@RequestParam(name = "groupName", required = true) String groupName,HttpServletRequest request) {
		JsonResult result=new JsonResult();
		log.debug("新建自定义检测组");
		String username=(String) request.getSession().getAttribute("username");
		User loginUser=userService.findUserByName(username);
		if(loginUser==null) {
			result.setCode(ResultCode.NOT_LOGIN);
			result.setMessage("未找到当前用户："+username);
			return result;
		}
		
		if(platformList==null || platformList.size()<1) {
			result.setCode(ResultCode.PARAMS_ERROR);
			result.setMessage("platformList为空");
			return result;
		}
		
		CustomMonitorGroup group=customGroupService.findGroupByGroupName(groupName);
		if(group!=null) {
			result.setCode(ResultCode.UNKNOWN_ERROR);
			result.setMessage("该分组名称重复");
			return result;
		}
		
		CustomMonitorGroup groupToAdd=new CustomMonitorGroup();
		groupToAdd.setUserId(username);
		groupToAdd.setGroupName(groupName);
		String pids=new String();
		for(String platformId:platformList) {
			pids=pids+platformId+",";
		}
		groupToAdd.setPlatformIdList(pids);
		customGroupService.addGroup(groupToAdd);
		return result;
	}
	
	/**
	 * 根据用户名字查询,当前用户已定制的自定义检测组
	 */
	@RequestMapping(value = "/listByUser", method = RequestMethod.GET)
	public JsonResult listByUser(HttpServletRequest request) {
		JsonResult result=new JsonResult();
		log.debug("查询自定义检测组");
		
		String username=(String) request.getSession().getAttribute("username");
		User loginUser=userService.findUserByName(username);
		if(loginUser==null) {
			result.setCode(ResultCode.NOT_LOGIN);
			result.setMessage("未找到当前用户："+username);
			return result;
		}
		
		List<CustomMonitorGroup> groupList=customGroupService.findGroupByUser(username);
		for(CustomMonitorGroup group:groupList) {
			String[] platformIds=group.getPlatformIdList().split(",");
			List<Platform> platformList=new ArrayList<Platform>();
			for(String platformId:platformIds) {
				Platform platform=platformService.findById(platformId);
				platform.setName(platform.getName()+"_"+platform.getPlatformTypeName());
				platformList.add(platform);
			}
			group.setPlatformList(platformList);
		}
		result.setData(groupList);
		return result;
	}
	
	/**
	 * 删除一个当前用户已定制的自定义检测组
	 */
	@RequestMapping(value = "/deleteByGroupId", method = RequestMethod.GET)
	public JsonResult deleteByGroupId(@RequestParam(name = "groupId", required = true) String groupId,
			HttpServletRequest request) {
		JsonResult result=new JsonResult();
		log.debug("根据groupId删除一个自定义检测组");
		
		String username=(String) request.getSession().getAttribute("username");
		User loginUser=userService.findUserByName(username);
		if(loginUser==null) {
			result.setCode(ResultCode.NOT_LOGIN);
			result.setMessage("未找到当前用户："+username);
			return result;
		}
		
		CustomMonitorGroup group=customGroupService.findGroupByGroupId(groupId);
		if(group!=null) {
			customGroupService.deleteCustomGroup(group);
		}
		result.setData(group);
		return result;
	}
	
	/**
	 * 查询当前用户检测组名称重复
	 */
	@RequestMapping(value = "/checkGroupName", method = RequestMethod.GET)
	public JsonResult checkGroupName(@RequestParam(name = "groupName", required = true) String groupName,HttpServletRequest request) {
		JsonResult result=new JsonResult();
		log.debug("查询当前用户检测组名称重复");
		
		String username=(String) request.getSession().getAttribute("username");
		User loginUser=userService.findUserByName(username);
		if(loginUser==null) {
			result.setCode(ResultCode.NOT_LOGIN);
			result.setMessage("未找到当前用户："+username);
			return result;
		}
		
		CustomMonitorGroup group=customGroupService.findGroupByGroupName(groupName);
		if(group!=null) {
			result.setCode(ResultCode.UNKNOWN_ERROR);
			result.setMessage("该名称重复");
			return result;
		}
		
		result.setMessage("该名称不重复");
		return result;
	}


}
