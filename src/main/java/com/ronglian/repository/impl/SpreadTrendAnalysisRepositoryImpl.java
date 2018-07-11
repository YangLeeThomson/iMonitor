package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.SpreadTrendAnalysisRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午1:42:58
* @description:传播趋势分析
*/
@Slf4j
@Repository("spreadTrendAnalysisRepository")
public class SpreadTrendAnalysisRepositoryImpl implements SpreadTrendAnalysisRepository {

	@Autowired
	private ElasticRepository elasticRepository;

	@Override
	public String spreadTimeTrendOneDayTop10(String platformTypeId, List<String> platformIdList, Date day,long start,long end,
			String orderField, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",0, day, null);
		String query = Utils.queryString(platformTypeId, platformIdList);
		Date onePlusDate = Utils.plusDay(1,day);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"title\",\n" + 
				"    \"transNum\",\n" + 
				"    \"commentNum\",\n" + 
				"    \"clickNum\",\n" + 
				"    \"thumbsNum\",\n" + 
				"    \"articleId\",\n" + 
				"    \"publishTime\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"publishTime\": {\n" + 
				"              \"gte\": "+start+",\n" + 
				"              \"lte\": "+end+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \""+orderField+"\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	
	@Override
	public String spreadTimeTrendOneDayTop10TransNum(String platformTypeId, List<String> platformIdList,Date day, long start,long end,
			String orderField, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String query = Utils.queryString(platformTypeId, platformIdList);
		Date onePlusDate = Utils.plusDay(1,day);
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",0, day, onePlusDate);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+start+",\n" + 
				"              \"lte\": "+end+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"size\": 0,\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\r\n" + 
				"    \"unionId\": {\r\n" + 
				"      \"terms\": {\r\n" + 
				"        \"field\": \"unionId\",\r\n" + 
				"        \"size\": "+pageNo*pageSize+",\r\n" + 
				"        \"order\": {\r\n" + 
				"          \"_count\": \"desc\"\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" +  
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String articlePublishTrend(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
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
				"  \"size\": 0,\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"publishTime\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"publishTime\",\n" + 
				"        \"interval\": \"day\",\n" + 
				"        \"format\": \"yyyy-MM-dd\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String articlePublishTrendDay(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",0, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
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
				"  \"size\": 0,\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"publishTime\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"publishTime\",\n" + 
				"        \"interval\": \"1h\",\n" + 
				"        \"time_zone\": \"Asia/Shanghai\",\n" + 
				"        \"format\": \"HH\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

}
