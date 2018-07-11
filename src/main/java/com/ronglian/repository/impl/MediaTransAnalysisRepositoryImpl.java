package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.MediaTransAnalysisRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月18日 下午11:21:53
* @description:描述
*/
@Slf4j
@Repository("mediaTransAnalysisRepository")
public class MediaTransAnalysisRepositoryImpl implements MediaTransAnalysisRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String TransMediaBang(Integer mediaType,Integer channel,int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		if(mediaType != null) {
			paramterMap.put("mediaType",Arrays.asList(String.valueOf(mediaType)));
		}
		if(channel != null) {
			paramterMap.put("channel",Arrays.asList(String.valueOf(channel)));
		}
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
	public String TransMediaBangSuper(Integer mediaType,Integer channel,int accountType, String platformTypeId, List<String> platformIdList, Date startTime,
			Date endTime,int pageNo,int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		if(mediaType != null && mediaType!=0) {
			if(mediaType == 6) {
				paramterMap.put("mediaType",Arrays.asList("0"));
			}else {
				paramterMap.put("mediaType",Arrays.asList(String.valueOf(mediaType)));
			}
		}
		if(channel != null && channel != 0) {
			if(channel==5) {
				paramterMap.put("channel",Arrays.asList("0"));
			}else {
				paramterMap.put("channel",Arrays.asList(String.valueOf(channel)));
			}
		}
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
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
				"        },\n" + 
				"		{"+
		        "   		\"exists\": {"+
		        "       		\"field\": \"crawlSource\" "+
		        "   		}"+
		        "   	}"+
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
//		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transArticleClassificationProportion(int accountType, String platformTypeId,
			List<String> platformIdList, Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		String query = Utils.queryString(paramterMap);
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
				"    \"classification\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"classification\",\n" + 
				"        \"size\": 50,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String transArticleClassificationTop10(int classification,String other,int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		String queryString = null;
		String query = null;
		String must_not = "";
		if(classification == 0) {
			if(!StringUtils.isEmpty(other)) {
				String[] classificationArray = other.split(",");
				for(int i=0;i<classificationArray.length;i++) {
					must_not += "{ \"match\": { \"classification\":"+classificationArray[i]+" }},";
				}
				must_not = must_not.substring(0,must_not.length()-1);
			}
			query = Utils.queryString(paramterMap);
			queryString = "{\r\n" + 
					"  \"from\":1,\r\n" + 
					"  \"size\":10,\r\n" + 
					"  \"_source\":[\r\n" + 
					"    \"unionId\",\r\n" + 
					"    \"platformId\",\r\n" + 
					"    \"platformName\",\r\n" + 
					"    \"platformTypeId\",\r\n" + 
					"    \"platformTypeName\",\r\n" + 
					"    \"articleId\",\r\n" + 
					"    \"title\",\r\n" + 
					"    \"publishTime\",\r\n" + 
					"    \"classification\"\r\n" + 
					"  ],\r\n" + 
					"  \"query\": {\r\n" + 
					"    \"bool\": {\r\n" + 
					"      \"must\": [\n" + ("".equals(query)?"":query+",")+
					"        {\r\n" + 
					"            \"range\":{\r\n" + 
					"                \"publishTime\": {\n" + 
					"                    \"gte\": "+startTime.getTime()+",\n" + 
					"                    \"lte\": "+endTime.getTime()+",\n" + 
					"                    \"format\": \"epoch_millis\"\n" + 
					"                }\r\n" + 
					"            }\r\n" + 
					"        }\r\n" + 
					"      ],\r\n" + 
					"      \"must_not\": [\r\n" + must_not+
					"      ]\r\n" + 
					"    }\r\n" + 
					"  },\r\n" + 
					"  \"sort\":{\r\n" + 
					"      \"transNum\":{\r\n" + 
					"          \"order\": \"desc\"\r\n" + 
					"      }\r\n" + 
					"  }\r\n" + 
					"}";
		}else {
			paramterMap.put("classification",Arrays.asList(String.valueOf(classification)));
			query = Utils.queryString(paramterMap);
			queryString = "{\n" + 
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
					"    \"publishTime\"\n" + 
					"  ],\n" + 
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
					"      ]\n" + 
					"    }\n" + 
					"  },\n" + 
					"  \"sort\": [\n" + 
					"    {\n" + 
					"      \"transNum\": {\n" + 
					"        \"order\": \"desc\"\n" + 
					"      }\n" + 
					"    }\n" + 
					"  ]\n" + 
					"}";
		}
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	
	@Override
	public String getTransPeriod(int accountType, String platformTypeId,List<String> platformIdList, Date startTime, Date endTime)
			throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		String query = Utils.queryString(null, platformIdList);
		String queryString = "{\r\n" + 
				"  \"size\": 0,\r\n" + 
				"  \"query\": {\r\n" + 
				"    \"bool\": {\r\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\r\n" + 
				"          \"range\": {\r\n" + 
				"            \"originArticlePubTime\": {\r\n" + 
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
				"  \"_source\": {\r\n" + 
				"    \"excludes\": []\r\n" + 
				"  },\r\n" + 
				"  \"aggs\": {\r\n" + 
				"    \"originArticlePubTime\": {\r\n" + 
				"      \"date_histogram\": {\r\n" + 
				"        \"field\": \"originArticlePubTime\",\r\n" + 
				"        \"interval\": \"1d\",\r\n" + 
				"        \"time_zone\": \"Asia/Shanghai\",\r\n" + 
				"        \"min_doc_count\": 1\r\n" + 
				"      },\r\n" + 
				"      \"aggs\": {\r\n" + 
				"        \"reportTime\": {\r\n" + 
				"          \"date_histogram\": {\r\n" + 
				"            \"field\": \"reportTime\",\r\n" + 
				"            \"interval\": \"1d\",\r\n" + 
				"            \"time_zone\": \"Asia/Shanghai\",\r\n" + 
				"            \"min_doc_count\": 1\r\n" + 
				"          }\r\n" +
				"        }\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

}
