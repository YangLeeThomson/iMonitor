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
import com.ronglian.common.ResultCode;
import com.ronglian.service.CopyrightMonitorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 版权检测，获取原文和转载文章详情接口
 * @author sunqian 
 * @date 2018年6月15日 下午4:29:57
 */
@Slf4j
@RestController
@RequestMapping("")
public class CopyrightMonitorController {

	@Autowired
	private CopyrightMonitorService copyrightMonitorService;
	
	/**
	 * 68.版权检测，获取原文接口
	 * */
	@RequestMapping(value = "/copyrightMonitor/originalArticle",method = RequestMethod.GET)
	public JsonResult getOriginalArticle(@RequestParam(value="unionId",required=true) String unionId) {
			
		log.debug("版权检测，获取原文接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.copyrightMonitorService.getOriginalArticle(unionId));
	}
	
	/**
	 * 69.版权检测，获取转载文章接口
	 * */
	@RequestMapping(value = "/copyrightMonitor/transArticle",method = RequestMethod.GET)
	public JsonResult getTransArticle(@RequestParam(value="webpageCode",required=true) String webpageCode) {
			
		log.debug("版权检测，获取转载文章接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.copyrightMonitorService.getTransArticle(webpageCode));
	}
	
	/**
	 * 4-6.标题搜索框接口，下拉级联接口
	 * */
	@RequestMapping(value = "/search/titleList",method = RequestMethod.GET)
	public JsonResult getTitleList(@RequestParam(value="titleWord",required=true) String titleWord,
			@RequestParam(value="page",required=false,defaultValue="1") int page,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
		log.debug("标题搜索框接口，下拉级联接口");
		titleWord=titleWord.replace("\"", "\\\"");
		return new JsonResult(ResultCode.SUCCESS,"success",this.copyrightMonitorService.getTitleList(titleWord,page,pageSize));
	}
	
	/**
	 * 4-6.标题搜索框接口，点击按钮获取搜索列表
	 * */
	@RequestMapping(value = "/search/articleList",method = RequestMethod.GET)
	public JsonResult getArticleList(@RequestParam(value="titleWord",required=true) String titleWord,
			@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
		log.debug("标题搜索框接口，点击按钮获取搜索列表");
		titleWord=titleWord.replace("\"", "\\\"");
		return new JsonResult(ResultCode.SUCCESS,"success",this.copyrightMonitorService.getArticleList(titleWord,pageNo,pageSize));
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // 严格限制日期转换
        sdf.setLenient(false);
        //true:允许输入空值，false:不能为空值 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

    }
}
