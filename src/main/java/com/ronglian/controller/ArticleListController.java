package com.ronglian.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.PageResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.TransAndOriginalArticle;
import com.ronglian.service.ArticleListService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 各种查询列表接口
 * @author sunqian
 * @date 2018年6月15日 下午3:41:18
 */

@Slf4j
@RestController
@RequestMapping("/articleList")
public class ArticleListController {

	@Autowired
	ArticleListService ArticleListService;

	/**
	 * 19.转载媒体占比文章列表接口
	 */
	@RequestMapping(value = "/transMediaType", method = RequestMethod.GET)
	public JsonResult transMediaTypeList(
			@RequestParam(value = "platformTypeId", required = false) String platformTypeId,
			@RequestParam(value = "platformId", required = false) String platformId,
			@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "mediaType", required = false, defaultValue = "0") int mediaType,
			@RequestParam(value = "startTime", required = true) Date startTime,
			@RequestParam(value = "accountType", required = false, defaultValue = "3") Integer accountType,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		log.debug("转载媒体占比文章列表接口");
		PageResult<TransAndOriginalArticle> platformPageResult = ArticleListService.findTransMediaTypeList(platformTypeId, platformId,
				groupId, mediaType, startTime, accountType, page, pageSize);

		return new JsonResult(ResultCode.SUCCESS, "success", platformPageResult);
	}

	/**
	 * 21.转载渠道占比列表接口
	 */
	@RequestMapping(value = "/transChannel", method = RequestMethod.GET)
	public JsonResult transChannelList(@RequestParam(value = "platformTypeId", required = false) String platformTypeId,
			@RequestParam(value = "platformId", required = false) String platformId,
			@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "channel", required = false, defaultValue = "0") int channel,
			@RequestParam(value = "startTime", required = true) Date startTime,
			@RequestParam(value = "accountType", required = false, defaultValue = "3") Integer accountType,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		log.debug("转载渠道占比列表接口");
		PageResult<TransAndOriginalArticle> platformPageResult = ArticleListService.findTransChannelList(platformTypeId, platformId,
				groupId, channel, startTime, accountType, page, pageSize);

		return new JsonResult(ResultCode.SUCCESS, "success", platformPageResult);
	}

	/**
	 * 23.平台转载排行列表
	 */
	@RequestMapping(value = "/transPlatform", method = RequestMethod.GET)
	public JsonResult transPlatformList(@RequestParam(value = "platformId", required = true) String platformId,
			@RequestParam(value = "startTime", required = true) Date startTime,
			@RequestParam(value = "accountType", required = false, defaultValue = "3") Integer accountType,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		log.debug("平台转载排行列表");
		PageResult<TransAndOriginalArticle> platformPageResult = ArticleListService.findTransPlatformList(platformId, startTime,
				accountType, page, pageSize);

		return new JsonResult(ResultCode.SUCCESS, "success", platformPageResult);
	}
	
	/**
	 * 25.媒体转载排行列表
	 */
	@RequestMapping(value = "/transMediaOrder", method = RequestMethod.GET)
	public JsonResult transMediaOrderList(@RequestParam(value = "platformTypeId", required = false) String platformTypeId,
			@RequestParam(value = "platformId", required = false) String platformId,
			@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "mediaName", required = true) String mediaName,
			@RequestParam(value = "startTime", required = true) Date startTime,
			@RequestParam(value = "accountType", required = false, defaultValue = "3") Integer accountType,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		log.debug("转载渠道占比列表接口");
		PageResult<TransAndOriginalArticle> platformPageResult = ArticleListService.findTransMediaOrderList(platformTypeId, platformId,
				groupId, mediaName, startTime, accountType, page, pageSize);

		return new JsonResult(ResultCode.SUCCESS, "success", platformPageResult);
	}


	@InitBinder
	public void initBinder(WebDataBinder binder) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// 严格限制日期转换
		sdf.setLenient(false);
		// true:允许输入空值，false:不能为空值
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

	}
}
