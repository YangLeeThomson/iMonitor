/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.controller 
 * @author: YeohLee   
 * @date: 2018年6月14日 上午11:40:56 
 */
package com.ronglian.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;

 /** 
 * @ClassName: PlatformTransController 
 * @Description: 平台转载
 * @author: YeohLee
 * @date: 2018年6月14日 上午11:40:56  
 */
@RestController
@Slf4j
@RequestMapping("/platform/trans")
public class PlatformTransController {

	/**
	 * 3.1平台转载排行接口
	 * */
	@RequestMapping(value = "/order/{groupId}",method = RequestMethod.GET)
	public JsonResult getPlatformTrans(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@PathVariable("groupId") String groupId,
			@RequestParam(value="today",required=true)Date today,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
		
		//1、从ES服务获取统计数据
		
		//2、for循环遍历统计数据，根据id取platformName装配数据；（取首页排序前5项）
		
		List<Map> resultList = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("platformName", "苏州电视台");
		map.put("platformId", "f4d584f6rf");
		map.put("homepageNum", 123);
		map.put("unHomepageNum", 89);
		map.put("startTime", "2018-06-25");
		map.put("accountType", 0);
		resultList.add(map);
		return new JsonResult(ResultCode.SUCCESS,"success",resultList);
	}
	/**
	 * 3.2平台转载排行更多接口
	 * */
	@RequestMapping(value = "/more/{groupId}",method = RequestMethod.GET)
	public JsonResult getPlatformTransMore(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@PathVariable("groupId") String groupId,
			@RequestParam(value="today",required=true)Date today,
			@RequestParam(value="accountType",required=false)Integer accountType,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo
			) {
		//1、从ES服务获取统计数据
		
		//2、for循环遍历统计数据，根据id取platformName装配数据；（取首页排序全部数据）
		
		List<Map> resultList = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("platformName", "苏州电视台");
		map.put("platformId", "f4d584f6rf");
		map.put("homepageNum", 123);
		map.put("unHomepageNum", 89);
		map.put("startTime", "2018-06-25");
		map.put("accountType", 0);
		resultList.add(map);
		
		Map resultMap = new HashMap();
		resultMap.put("infoList", resultList);
		resultMap.put("totalNum", "总页数  eg：280 ");
		return new JsonResult(ResultCode.SUCCESS,"success",resultMap);
	}
	/**
	 * 3.3平台转载排行列表接口
	 * */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public JsonResult getArticleInfoList(@RequestParam(value="platformId",required=true) String platformId,
			@RequestParam(value="today",required=true)Date today,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo
			) {
		Integer count = 0;
		//1、从ES服务获取文章总数；
		/*
		 * if(count != 0){
		 * count = "count在此赋值";
		 * }
		 * */
		
		//2、从ES服务获取文章列表（分页数据列表）  
		
		//3、for循环遍历es数据？？？
		
		List resultList = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("id", "文章id");
		map.put("transMedia", "转载媒体    新浪、 网易");
		map.put("transTitle", "转载标题");
		map.put("transTime", "转载时间格式：2018-06-28 15：51  精确到分");
		map.put("homepageStatus", "1,表示首页；0表示非首页");
		map.put("homepageTime", "首页刊登时间长度  37小时 或 54分钟   不足1小时精确到分");
		map.put("title", "原文标题");
		resultList.add(map);
		return new JsonResult(ResultCode.SUCCESS,"success",resultList);
	}
	
}
