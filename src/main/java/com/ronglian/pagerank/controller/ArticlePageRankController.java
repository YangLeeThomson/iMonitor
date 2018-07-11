package com.ronglian.pagerank.controller;

import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.pagerank.model.ArticlePageRank;
import com.ronglian.pagerank.service.ArticlePageRankService;


@RestController
@Slf4j
@RequestMapping("/pagerank")
public class ArticlePageRankController{
	
	@Autowired
	private ArticlePageRankService articlePageRankService;
	
	/**
	 * 查询文章的pageRank信息
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * */
	@RequestMapping(value = "/detonation",method = RequestMethod.GET)
	public JsonResult findByDate(@RequestParam("unionId") String unionId) throws JsonParseException, JsonMappingException, IOException {
		log.debug("unionId:"+unionId);
		return new JsonResult(ResultCode.SUCCESS,"success",articlePageRankService.getDetonation(unionId));
	}
	
	/*@RequestMapping(value = "/receiveArticle",method = RequestMethod.POST)
	public JsonResult receiveArticle(@RequestBody ArticlePageRank articlePageRank) throws JsonParseException, JsonMappingException, IOException {
		return new JsonResult(ResultCode.SUCCESS,"success",articlePageRankService.receiveArticlePageRank(articlePageRank));
	}*/
	
	
	
}
