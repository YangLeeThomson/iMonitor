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
import com.ronglian.service.ArticleComparedService;

 /** 
 * @ClassName: ArticleComparedController 
 * @Description: 作品对比分析
 * @author: YeohLee
 * @date: 2018年6月13日 下午4:16:37  
 */
@RestController
@Slf4j
@RequestMapping("/article/compared")
public class ArticleComparedController {

	@Autowired
	private ArticleComparedService articleComparedService; 
	
	/**
	 * 2.1查询标题接口
	 * */
	@RequestMapping(value = "/search",method = RequestMethod.GET)
	public JsonResult getArticleList(@RequestParam(value="keyword",required=true) String keyword) {
		
		return this.articleComparedService.getArticleListByTitle(keyword);
	}
	/**
	 * 2.2保存对比接口
	 * */
//	@RequestMapping(value = "/add",method = RequestMethod.POST)
//	public JsonResult addArticleCompared(@RequestBody Map articleComparedGroup) {
	@RequestMapping(value = "/add",method = RequestMethod.GET)
	public JsonResult addArticleCompared(@RequestParam(value="userId",required=false) String userId,
			HttpServletRequest request,
			@RequestParam(value="idList",required=true) String[] idList
			) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		Map articleComparedGroup = new HashMap();
		List<String> idListArray = java.util.Arrays.asList(idList);
		if(StringUtils.isBlank(userId)){
			return new JsonResult(ResultCode.ERROR,"error","userId was null");
		}
		if(idList == null || idListArray.size() < 2){
			return new JsonResult(ResultCode.ERROR,"error","the idList may be was null or it's size less than 2");
		}
		if(idListArray.size() > 3){
			return new JsonResult(ResultCode.ERROR,"error","the idList's size is more than 3");
		}
		String groupId = UUID.randomUUID().toString();
		articleComparedGroup.put("groupId", groupId);
		articleComparedGroup.put("idList", idListArray);
		articleComparedGroup.put("userId", userId);
		this.articleComparedService.addGroup(articleComparedGroup);
		return new JsonResult(ResultCode.SUCCESS,"success",groupId);
	}
	
	/**
	 * 2.3查询已添加对比列表接口
	 * */
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult getArticleCompared(@RequestParam(value="userId",required=false) String userId,
			HttpServletRequest request,
			@RequestParam(value="today",required=false) Date today,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		return this.articleComparedService.getArticleCompared(userId);
	
	}
	/**
	 * 2.4删除已添加对比列表接口
	 * */
	@RequestMapping(value = "/del",method = RequestMethod.GET)
	public JsonResult deleteArticleCompared(@RequestParam(value="groupId",required=true) String groupId,
			HttpServletRequest request,
			@RequestParam(value="userId",required=false) String userId) {
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		int num;
		if(StringUtils.isBlank(groupId)){
			return new JsonResult(ResultCode.ERROR,"error","the groupId was null");
		}
		num = this.articleComparedService.delGroup(groupId);
		return new JsonResult(ResultCode.SUCCESS,"success",num);
	}
	
}
