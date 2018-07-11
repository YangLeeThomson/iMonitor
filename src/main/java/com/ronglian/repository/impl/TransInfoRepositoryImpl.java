package com.ronglian.repository.impl;

import java.io.IOException;
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
import com.ronglian.model.TransInfo;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.TransInfoRepository;
import com.ronglian.utils.Utils;

@Slf4j
@Repository("transInfoRepository")
public class TransInfoRepositoryImpl implements TransInfoRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public List<TransInfo> getTransInfoByUnionIds(List<String> unionIds) throws IOException{
		String endpoint = "imonitor_trans-info-*";
		Map<String, List<String>> parameterMap =new HashMap<String, List<String>>();
		parameterMap.put("unionId",unionIds);
		String query=Utils.queryString(parameterMap);
		String queryString = "{ \r\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		
		List<TransInfo> transinfoList = new LinkedList<TransInfo>();
		String focalMediaResult=elasticRepository.queryES("GET", endpoint, queryString);
		log.debug(focalMediaResult);
		System.out.println(focalMediaResult);
		JSONArray esJson=JSON.parseObject(focalMediaResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++){
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			transinfoList.add(
					new TransInfo(element.getString("unionId"),element.getString("originTitle"),
							element.getString("platformId"),element.getString("platformName"),
							element.getString("platformTypeId"),element.getString("platformTypeName"),
							element.getString("articleId"),element.getDate("originArticlePubTime"),
							element.getString("title"),element.getString("content"),
							element.getString("crawSourceDomain"),element.getString("crawlSource"),
							element.getString("province"),element.getInteger("mediaType"),
							element.getInteger("channel"),element.getDate("reportTime"),
							element.getString("webpageCode"),element.getString("webpageUrl"),
							element.getFloat("nlpScore"),element.getString("reportSource"),
							element.getString("screenshot"),element.getInteger("isTort"),
							element.getDate("createTime")));
		}
		return transinfoList;
	}
	
}
