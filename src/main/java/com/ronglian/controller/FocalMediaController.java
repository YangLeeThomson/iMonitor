package com.ronglian.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.service.FocalMediaService;

@RestController
@Slf4j
public class FocalMediaController{
	
	@Autowired
	private FocalMediaService focalMediaService;
	
	/**
	 * 保存或更新重点媒体转载记录
	 * @throws IOException 
	 * @throws ParseException 
	 * */
	@RequestMapping(value = "/api/focalmedia/receiveTrans",method = RequestMethod.POST)
	public JsonResult saveFocalMediaTrans(@RequestBody List<Map<String,Object>> requestMapList) throws ParseException, IOException {
		focalMediaService.saveFocalMediaTrans(requestMapList);
		return new JsonResult(ResultCode.SUCCESS,"success","success");
	}
	
	@RequestMapping(value = "/focalmedia/findTransCount",method = RequestMethod.GET)
	public JsonResult findFocalTransCountPage(
			@RequestParam(value="queryId",required=false) String queryId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="mainGroup",required=false) String mainGroup,
			@RequestParam(value="queryType",required=true) Integer queryType,
			@RequestParam(value="startTime",required=false) Long startTime,
			@RequestParam(value="endTime",required=false) Long endTime,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo) throws IOException {
		Calendar cStart = Calendar.getInstance();
		cStart.setTimeInMillis(startTime!=null?startTime:0);
		Calendar cEnd = Calendar.getInstance();
		if(endTime!=null)
			cEnd.setTimeInMillis(endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",focalMediaService.findFocalTransCountPage(queryId,groupId,mainGroup,queryType,
				cStart.getTime(), cEnd.getTime(),pageNo,pageSize));
	}
	
	@RequestMapping(value = "/focalmedia/findTransPage",method = RequestMethod.GET)
	public JsonResult findFocalMediaTransPage(
			@RequestParam(value="queryId",required=false) String queryId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="mainGroup",required=false) String mainGroup,
			@RequestParam(value="mediaId",required=false) String mediaId,
			@RequestParam(value="publishStatus",required=false) Integer publishStatus,
			@RequestParam(value="queryType",required=true) Integer queryType,
			@RequestParam(value="startTime",required=false) Long startTime,
			@RequestParam(value="endTime",required=false) Long endTime,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo) throws IOException {
		Calendar cStart = Calendar.getInstance();
		cStart.setTimeInMillis(startTime!=null?startTime:0);
		Calendar cEnd = Calendar.getInstance();
		if(endTime!=null)
			cEnd.setTimeInMillis(endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",focalMediaService.findFocalMediaTransPage(queryId,groupId,mainGroup,mediaId,queryType,
				publishStatus,cStart.getTime(), cEnd.getTime(),pageNo,pageSize));
	}
	
}