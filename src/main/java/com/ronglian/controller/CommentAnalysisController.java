package com.ronglian.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
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
import com.ronglian.model.Comment;
import com.ronglian.service.CommentAnalysisService;

@RestController
@Slf4j
public class CommentAnalysisController{
	@Autowired
	private CommentAnalysisService commentAnalysisService;
	/**
	 * 保存或更新评论
	 * @throws IOException 
	 * @throws ParseException 
	 * */
	@RequestMapping(value = "/api/comment/receiveComment",method = RequestMethod.POST)
	public JsonResult saveFocalMediaTrans(@RequestBody List<Map<String,Object>> requestMapList) throws ParseException, IOException {
		List<Comment> commentList=new LinkedList<Comment>();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String,Object> requestMap:requestMapList){
			commentList.add(new Comment((String)requestMap.get("id"),requestMap.get("userName")!=null?(String)requestMap.get("userName"):null
					,requestMap.get("region")!=null?(String)requestMap.get("region"):null,requestMap.get("publishTime")!=null?dateFormat.parse((String)requestMap.get("publishTime")):date,
					requestMap.get("upNum")!=null?(Integer)requestMap.get("upNum"):null,requestMap.get("downNum")!=null?(Integer)requestMap.get("downNum"):null,
					requestMap.get("unionId")!=null?(String)requestMap.get("unionId"):null,requestMap.get("content")!=null?(String)requestMap.get("content"):null));
		}
		if(commentList.size()>0){
			commentAnalysisService.recieveComment(commentList);
		}
		return new JsonResult(ResultCode.SUCCESS,"success","success");
	}
	
	/**
	 * 保存或更新评论分析
	 * @throws IOException 
	 * @throws ParseException 
	 * */
	@RequestMapping(value = "/api/comment/receiveCommentAnalysis",method = RequestMethod.POST)
	public JsonResult recieveCommentAnalysis(@RequestBody Map<String,Object> requestMap) throws ParseException, IOException{
		commentAnalysisService.recieveCommentAnalysis(requestMap);
		return new JsonResult(ResultCode.SUCCESS,"success","success");
	}
	
	/*@RequestMapping(value = "/receiveCommentAnalysis",method = RequestMethod.POST)
	public JsonResult recieveCommentAnalysis(@RequestBody String requestMap) throws ParseException, JsonProcessingException{
		System.out.println(requestMap);
		return new JsonResult(ResultCode.SUCCESS,"success","success");
	}*/

	/**
	 * 分页查询列表
	 * @throws IOException 
	 * */
	@RequestMapping(value = "/comment/findComments",method = RequestMethod.GET)
	public JsonResult find(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId,
				           @RequestParam(value = "orderField", required = false, defaultValue = "") String orderField,
						   @RequestParam("pageNo") int pageNo,
						   @RequestParam("pageSize") int pageSize) throws IOException {
		log.debug("findComments:unionId:"+unionId+",orderField:"+orderField+",pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.findPageList(orderField,unionId,pageNo,pageSize));
	}
	@RequestMapping(value = "/comment/getDistribute",method = RequestMethod.GET)
	public JsonResult getDistribute(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId,
				           @RequestParam(value = "queryType", required = false, defaultValue = "") Integer queryType) throws IOException {
		log.debug("getCommentDistribute:queryType:"+queryType+",unionId:"+unionId);
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentDistribute(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getWordCloud",method = RequestMethod.GET)
	public JsonResult getWordCloud(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		log.debug("getWordCloud:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("1");
		queryType.add("2");
		queryType.add("3");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getTags",method = RequestMethod.GET)
	public JsonResult getTags(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		log.debug("getTags:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("6");
		queryType.add("7");
		queryType.add("8");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getOpinions",method = RequestMethod.GET)
	public JsonResult getOpinions(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		log.debug("getOpinions:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("4");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getSentiment",method = RequestMethod.GET)
	public JsonResult getSentiment(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		log.debug("getSentiment:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("5");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
}
