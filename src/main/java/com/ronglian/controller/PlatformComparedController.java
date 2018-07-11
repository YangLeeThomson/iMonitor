/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.controller 
 * @author: YeohLee   
 * @date: 2018年6月13日 下午4:16:37 
 */
package com.ronglian.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.service.PlatformComparedService;

 /** 
 * @ClassName: ArticleComparedController 
 * @Description: 平台对比分析
 * @author: YeohLee
 * @date: 2018年6月13日 下午4:16:37  
 */
@RestController
@Slf4j
@RequestMapping("/platform/compared")
public class PlatformComparedController {

	@Autowired
	private PlatformComparedService platformService;

	/**
	 * 4.1新增对比平台接口
	 * */
//	@RequestMapping(value = "/add",method = RequestMethod.POST)
//	public JsonResult addPlatformGroupCompare(@RequestBody Map map) {
	@RequestMapping(value = "/add",method = RequestMethod.GET)
	public JsonResult addPlatformGroupCompare(@RequestParam(value="userId",required=false) String userId,
			HttpServletRequest request,
			@RequestParam(value="platformIdList",required=true) String[] platformIdList
			) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		Map map = new HashMap();
		List<String> idListArray = java.util.Arrays.asList(platformIdList);
		if(StringUtils.isBlank(userId)){
			return new JsonResult(ResultCode.ERROR,"error","userId was null");
		}
		if(platformIdList == null || idListArray.size() < 2){
			return new JsonResult(ResultCode.ERROR,"error","the idList may be was null or it's size less than 2");
		}
		if(idListArray.size() > 3){
			return new JsonResult(ResultCode.ERROR,"error","the idList's size is more than 3");
		}
		String groupId = UUID.randomUUID().toString();
		map.put("groupId", groupId);
		map.put("userId", userId);
		map.put("platformIdList", idListArray);
		return this.platformService.addPlatformGroup(map);
	}
	/**
	 * 4.2删除对比平台接口
	 * */
	@RequestMapping(value = "/del",method = RequestMethod.GET)
	public JsonResult deletePlatformGroupCompare(@RequestParam(value="groupId",required=true) String groupId,
			HttpServletRequest request,
			@RequestParam(value="userId",required=false) String userId) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		if(StringUtils.isBlank(groupId)){
			return new JsonResult(ResultCode.ERROR,"error","groupId was null");
		}
		return this.platformService.deletePlatformGroupCompare(groupId);
	}
	
	/**
	 * 4.3按名称查询平台接口
	 * */
	@RequestMapping(value = "/search",method = RequestMethod.GET)
	public JsonResult searchPlatformGroupCompare(@RequestParam(value="keyword",required=true) String keyword) {
		if(StringUtils.isBlank(keyword)){
			return new JsonResult(ResultCode.ERROR,"error","keyword was null");
		}
		return this.platformService.searchPlatformGroupCompare(keyword);
	}
	/**
	 *
	 * 4.4平台对比数据查询接口
	 * */
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult findPlatformGroupCompare(@RequestParam(value="userId",required=false) String userId,
			HttpServletRequest request,
			@RequestParam(value="today",required=true)Date today,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType
			) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		return this.platformService.findPlatformGroupCompare(userId, today, accountType);
	}
	/**
	 *  4.5查询当前用户对比记录平台接口
	 * */
	@RequestMapping(value = "/group",method = RequestMethod.GET)
	public JsonResult getPlatformGroupCompare(@RequestParam(value="userId",required=false) String userId,
			HttpServletRequest request
			) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		return this.platformService.getPlatformGroupCompare(userId);
	}
	
}
