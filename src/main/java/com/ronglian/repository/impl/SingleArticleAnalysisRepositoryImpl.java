package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.SingleArticleAnalysisRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月19日 上午10:00:24
* @description:描述
*/
@Slf4j
@Repository("singleArticleAnalysisRepository")
public class SingleArticleAnalysisRepositoryImpl implements SingleArticleAnalysisRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String platformsSpreadTrend(int accountType, List<String> platformIdList, Date startTime, Date endTime)
			throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String query = Utils.queryString(null, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" +  ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"publishTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"publishTime\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"publishTime\",\n" + 
				"        \"interval\": \"1d\",\n" + 
				"        \"time_zone\": \"Asia/Shanghai\",\n" + 
				"        \"min_doc_count\": 1\n" + 
				"      },\n" + 
				"      \"aggs\": {\n" + 
				"        \"platformId\": {\n" + 
				"          \"terms\": {\n" + 
				"            \"size\": 10,\n" + 
				"            \"field\": \"platformId\",\n" + 
				"            \"order\": {\n" + 
				"               \"_term\": \"desc\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String commentSpreadTimeTrend(int accountType, String articleUnionId, Date startTime, Date endTime)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String commentSpreadArea(int accountType, String articleUnionId, Date startTime, Date endTime)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
