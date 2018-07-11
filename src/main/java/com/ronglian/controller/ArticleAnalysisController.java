/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.controller 
 * @author: YeohLee   
 * @date: 2018年6月13日 下午4:15:32 
 */
package com.ronglian.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.service.ArticleInfoService;

 /** 
 * @ClassName: ArticleAnalysisController 
 * @Description: 单篇文章详情
 * @author: YeohLee
 * @date: 2018年6月13日 下午4:15:32  
 */
@RestController
@Slf4j
@RequestMapping("/article/analysis")
public class ArticleAnalysisController {

	@Autowired
	private ArticleInfoService articleService;
	
	/**
	 * 1.1详情查询单片文章接口
	 * */
	@RequestMapping(value = "/info",method = RequestMethod.GET)
	public JsonResult getArticleInfo(@RequestParam(value="id",required=true) String id) {
		return this.articleService.getArticleInfo(id);
	}
	/**
	 * 1.2单篇文章详情监测状态和检测时间
	 * */
	@RequestMapping(value = "/status",method = RequestMethod.GET)
	public JsonResult getImonitorStatus(@RequestParam(value="id",required=true) String id) {

		return this.articleService.getImonitorStatus(id);
	}
	
	/**
	 * 1.3平台情况分析接口
	*/
	@RequestMapping(value = "/platform",method = RequestMethod.GET)
	public JsonResult getPlatformAnalysis(@RequestParam(value="id",required=true) String id,
			@RequestParam(value="today",required=false) Date today) {
		return this.articleService.getPlatformAnalysis(id);
	}
	/**
	 *1.4 转载趋势接口
	*/
	@RequestMapping(value = "/current",method = RequestMethod.GET)
	public JsonResult getTransCurrent(@RequestParam(value="id",required=true) String id) {
		return this.articleService.getTransCurrent(id);
	}
	/**
	 * 1.5传播路径接口(转刘瀚博)
	*/
	@RequestMapping(value = "/path",method = RequestMethod.GET)
	public JsonResult getTransPath(@RequestParam(value="id",required=true) String id,
			@RequestParam(value="today",required=true) Date today) {
		//从es获取统计数据（transNum 转载数，mediaNum 媒体数）以及转载关系
		Integer transNum = 0;
		Integer mediaNum = 0;
		
		List result = new ArrayList();
		//遍历转载关系
		
		Map map = new HashMap();
		map.put("mediaId", "媒体id");
		map.put("mediaName", "媒体名称 eg：网易、上海证券报。。。");
		map.put("parentId", "父节点  媒体id ");
		
		Map map1 = new HashMap();
		map1.put("mediaId", "媒体id1");
		map1.put("mediaName", "媒体名称1 eg：网易、上海证券报。。。");
		map1.put("parentId", "父节点1  媒体id ");
		
		Map map2 = new HashMap();
		map2.put("mediaId", "媒体id2");
		map2.put("mediaName", "媒体名称2 eg：网易、上海证券报。。。");
		map2.put("parentId", "父节点2  媒体id ");
		
		result.add(map);
		result.add(map2);
		result.add(map1);
		return new JsonResult(ResultCode.SUCCESS,"success",result);
	}
	/**
	 * 1.6转载新闻列表
	*/
	@RequestMapping(value = "/newslist",method = RequestMethod.GET)
	public JsonResult getTransArticleList(@RequestParam(value="id",required=true) String id,
			@RequestParam(value="today",required=false) Date today,
			@RequestParam(value="transType",required=false,defaultValue="9") int transType,
			@RequestParam(value="tort",required=false,defaultValue="9") int tort,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo
			) {
		return this.articleService.getTransArticleList(id,pageNo,pageSize,transType,tort);
	}
	
}
