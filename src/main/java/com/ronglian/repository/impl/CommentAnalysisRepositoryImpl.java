package com.ronglian.repository.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.model.CommentAnalysis;
import com.ronglian.repository.CommentAnalysisRepository;
import com.ronglian.repository.ElasticRepository;
//import com.ronglian.utils.ESUtil;
import com.ronglian.utils.Utils;


@Slf4j
@Repository
public class CommentAnalysisRepositoryImpl implements CommentAnalysisRepository {
	
	@Autowired
	private ElasticRepository elasticRepository;

	@Override
	public void saveCommentAnalysis(List<CommentAnalysis> commentAnalysisList) throws JsonProcessingException{
		//new日期对象
		Date date = new Date();
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		elasticRepository.bulkAdd("comment_analysis-"+dateFormat.format(date),"imonitor",commentAnalysisList);
		//ESUtil.getInstance().bulkAdd("comment_analysis-"+dateFormat.format(date),"imonitor",commentAnalysisList);
	}

	@Override
	public List<CommentAnalysis> findCommentAnalysis(List<String> type,String articleId) throws IOException{
		String endpoint = "comment_analysis-*";
		List<String> articleIds=new LinkedList<String>();
		List<CommentAnalysis> commentAnalysisList=new LinkedList<CommentAnalysis>();
		articleIds.add(articleId);
		Map<String,List<String>> paramterMap=new HashMap<String,List<String>>();
		paramterMap.put("type",type);
		paramterMap.put("unionId",articleIds);
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  }\n" +  
				"}";
		System.out.println(queryString);
		String commentAnalysisResult=elasticRepository.queryES("GET", endpoint, queryString);
		JSONArray esJson=JSON.parseObject(commentAnalysisResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++){
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			commentAnalysisList.add(new CommentAnalysis(element.getString("unionId"),element.getInteger("type"),element.getDouble("score"),
					element.getDate("analysisTime"),element.getString("content"),element.getInteger("num")));
		}
		return commentAnalysisList;
	}

	@Override
	public void deleteAnalysisByArticleId(String articleId) throws IOException{
		String endpoint = "comment_analysis-*";
		List<String> articleList =new LinkedList<String>();
		articleList.add(articleId);
		Map<String, List<String>> parameterMap =new HashMap<String, List<String>>();
		parameterMap.put("unionId",articleList);
		String query=Utils.queryString(parameterMap);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  }\n" +  
				"}";
		elasticRepository.delteteES(endpoint,queryString,null);
	}
}