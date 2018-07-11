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
import com.ronglian.service.SpreadTimeTrendService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/spreadTrend")
public class SpreadTimeTrendController {

	@Autowired
	private SpreadTimeTrendService spreadTimeTrendService;
	
	/**
	 * 传播时间趋势接口
	 * */
	@RequestMapping(value = "/timeTrend",method = RequestMethod.GET)
	public JsonResult getSpreadTimeCurrent(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
			
		log.info("传播时间趋势接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getSpreadTimeTend(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 媒体转载排行接口,首页
	 * */
	@RequestMapping(value = "/mediaOrder",method = RequestMethod.GET)
	public JsonResult getMediaOrder(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
		log.info("媒体转载排行接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getMediaOrder(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 媒体转载排行接口,媒体转载分析页
	 * */
	@RequestMapping(value = "/mediaOrderSuper",method = RequestMethod.GET)
	public JsonResult getMediaOrderSuper(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")int accountType,
			@RequestParam(value="mediaType",required=false)Integer mediaType,
			@RequestParam(value="channel",required=false)Integer channel,
			@RequestParam(value="pageNo",required=false,defaultValue="1")int pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize) {
		log.info("媒体转载排行接口Super");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getMediaOrderSuper(platformTypeId,platformId,groupId,startTime,accountType,mediaType,channel,pageNo,pageSize));
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
