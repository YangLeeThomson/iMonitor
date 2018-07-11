package com.ronglian.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import com.ronglian.service.TransPeriodService;

import lombok.extern.slf4j.Slf4j;


/**
* @author: 黄硕/huangshuo
* @date:2018年4月27日 上午9:49:07
* @description:demo
*/
@Slf4j
@RestController
@RequestMapping("/trans")
public class TransPeriodController {
	
	
//	PlatformArticleInfoService platformArticleInfoService;
	
	@Autowired
    private TransPeriodService transPeriodService;
	
	/**
	 * 转载时段占比接口
	 * */
	@RequestMapping(value = "/transPeriod",method = RequestMethod.GET)
	public JsonResult getSpreadTimeCurrent(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,HttpServletRequest request) {
//		log.debug("accountType:"+accountType);
		log.info("【进入】Controller_transPeriod："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		return new JsonResult(ResultCode.SUCCESS,"success",this.transPeriodService.getTransPeriod(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 综合数值排行接口
	 * */
	@RequestMapping(value = "/comprehensiveNum",method = RequestMethod.GET)
	public JsonResult getComprehensiveNum(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam(value="orderCode",required=false,defaultValue="1")int orderCode,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize) {
		//log.debug("accountType:"+accountType);
		return new JsonResult(ResultCode.SUCCESS,"success",this.transPeriodService.getComprehensiveNum(platformTypeId,platformId,groupId,startTime,accountType,orderCode,page,pageSize));
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
