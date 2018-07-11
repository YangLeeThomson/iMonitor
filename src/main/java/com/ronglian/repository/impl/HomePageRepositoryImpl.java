package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.HomePageRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 上午11:05:48
* @description:首页总览数据层
*/
@Slf4j
@Component("homePageRepository")
public class HomePageRepositoryImpl implements HomePageRepository {
	
	@Autowired
	private ElasticRepository elasticRepository;

	@Override
	public String spreadCondition(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime) throws IOException {
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
				"    \"clickNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"clickNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"transNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"transNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"subscribeNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"subscribeNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"shareNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"shareNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"commentNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"commentNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"thumbsNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"thumbsNum\"\n" + 
				"      }\n" + 
				"    },\n" + 
				"    \"awardNum\": {\n" + 
				"      \"sum\": {\n" + 
				"        \"field\": \"awardNum\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String platformProportion(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime) throws IOException {
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
				"    \"platformId\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"platformId\",\n" + 
				"        \"size\": 50,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	
	/**
	 * 平台组作品占比
	 * */
	@Override
	public String platformTypeProportion(int accountType,Date startTime,Date endTime)throws IOException{
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String queryString = "{\r\n" + 
				"  \"query\": {\r\n" + 
				"    \"bool\": {\r\n" + 
				"      \"must\": [\r\n" + 
				"        {\r\n" + 
				"          \"range\": {\r\n" + 
				"            \"publishTime\": {\r\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\r\n" + 
				"            }\r\n" + 
				"          }\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"must_not\": []\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  \"size\": 0,\r\n" + 
				"  \"_source\": {\r\n" + 
				"    \"excludes\": []\r\n" + 
				"  },\r\n" + 
				"  \"aggs\": {\r\n" + 
				"    \"platformTypeId\": {\r\n" + 
				"      \"terms\": {\r\n" + 
				"        \"field\": \"platformTypeId\",\r\n" + 
				"        \"size\": 50,\r\n" + 
				"        \"order\": {\r\n" + 
				"          \"_count\": \"desc\"\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}


	@Override
	public String originArticleProportion(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
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
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"isOrigin\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"isOrigin\",\n" + 
				"        \"size\": 5,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String spreadDateTrend(int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
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
				"        \"comprehensive\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"comprehensive\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"transNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"transNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"commentNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"commentNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"clickNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"clickNum\"\n" + 
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
	public String spreadDateTrendOneDay(int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
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
				"        \"interval\": \"1h\",\n" + 
				"        \"time_zone\": \"Asia/Shanghai\",\n" + 
				"        \"format\": \"HH\",\n" + 
				"        \"min_doc_count\": 1\n" + 
				"      },\n" + 
				"      \"aggs\": {\n" + 
				"        \"comprehensive\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"comprehensive\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"commentNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"commentNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"clickNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"clickNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"transNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"transNum\"\n" + 
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
	public String spreadAreaDistribution(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		paramterMap.put("_exists_",Arrays.asList(String.valueOf("province")));
		String query = Utils.queryString(paramterMap);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"province\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"province\",\n" + 
				"        \"size\": 50,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transMediaTypeProportion(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"mediaType\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"mediaType\",\n" + 
				"        \"size\": 10,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transMediaTypeList(int mediaType,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		paramterMap.put("mediaType",Arrays.asList(String.valueOf(mediaType)));
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"articleId\",\n" + 
				"    \"title\",\n" + 
				"    \"crawlSource\",\n" + 
				"    \"webpageCode\",\n" + 
				"    \"webpageUrl\",\n" + 
				"    \"originArticlePubTime\",\n" + 
				"    \"originTitle\",\n" + 
				"    \"reportTime\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"reportTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	
	@Override
	public String transChannelProportion(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"channel\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"channel\",\n" + 
				"        \"size\": 20,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transChannelList(int channel,int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException{
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		if(channel!=0) {
			paramterMap.put("channel",Arrays.asList(String.valueOf(channel)));
		}
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"articleId\",\n" + 
				"    \"title\",\n" + 
				"    \"crawlSource\",\n" + 
				"    \"originTitle\",\n" + 
				"    \"webpageCode\",\n" + 
				"    \"webpageUrl\",\n" + 
				"    \"originArticlePubTime\",\n" + 
				"    \"reportTime\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"reportTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
		
	}
	
	@Override
	public String platformTransedBang(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"platformId\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"platformId\",\n" + 
				"        \"size\": 50,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String TransMediaBang(int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"crawlSource\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"crawlSource\",\n" + 
				"        \"size\": 3000,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transPlatformList(int accountType, String platformId, Date startTime, Date endTime, int pageNo,
			int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformId)) {
			List<String> platformIdList =new ArrayList<String>();
			platformIdList.add(platformId);
			paramterMap.put("platformId", platformIdList);
		}
		String query = Utils.queryString(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"articleId\",\n" + 
				"    \"title\",\n" + 
				"    \"crawlSource\",\n" + 
				"    \"webpageCode\",\n" + 
				"    \"webpageUrl\",\n" + 
				"    \"originArticlePubTime\",\n" + 
				"    \"originTitle\",\n" + 
				"    \"reportTime\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"reportTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transMediaNameList(int accountType, String mediaName,String platformTypeId,List<String> platformIdList,Date startTime, Date endTime, int pageNo,
			int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(mediaName)) {
			paramterMap.put("crawlSource",Arrays.asList(mediaName));
		}
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"articleId\",\n" + 
				"    \"title\",\n" + 
				"    \"crawlSource\",\n" + 
				"    \"webpageCode\",\n" + 
				"    \"webpageUrl\",\n" + 
				"    \"originArticlePubTime\",\n" + 
				"    \"originTitle\",\n" + 
				"    \"reportTime\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"reportTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String articleTransCount(int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"from\": 0,\n" + 
				"  \"size\": 2,\n" + 
				"  \"_source\": [\n" + 
				"    \"webpageCode\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String spreadDateTrendTransCount(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"originArticlePubTime\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"originArticlePubTime\",\n" + 
				"        \"interval\": \"1d\",\n" + 
				"        \"time_zone\": \"Asia/Shanghai\",\n" + 
				"        \"format\": \"yyyy-MM-dd\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String spreadDateTrendTransCountOneDay(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",0, startTime, endTime);
		String query = Utils.queryString(platformTypeId, platformIdList);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"originArticlePubTime\": {\n" + 
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
				"    \"originArticlePubTime\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"originArticlePubTime\",\n" + 
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
