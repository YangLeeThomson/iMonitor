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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.HotArticleRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午12:06:18
* @description:描述
*/
@Slf4j
@Repository
public class HotArticleRepositoryImpl implements HotArticleRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String hotArticleList(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime, int isOrigin,String orderField, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",accountType, startTime, endTime);
//		String query = Utils.queryString(platformTypeId, platformIdList);

		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		if(isOrigin != 3) {//0:非原创，1:原创，3：全部
			paramterMap.put("isOrigin",Arrays.asList(String.valueOf(isOrigin)));
		}
		String query = Utils.queryString(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+(pageNo-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"_source\": [\n" + 
				"    \"unionId\",\n" + 
				"    \"articleId\",\n" + 
				"    \"platformTypeId\",\n" + 
				"    \"platformTypeName\",\n" + 
				"    \"platformId\",\n" + 
				"    \"platformName\",\n" + 
				"    \"title\",\n" + 
				"    \"transNum\",\n" + 
				"    \"commentNum\",\n" + 
				"    \"clickNum\",\n" + 
				"    \"thumbsNum\",\n" + 
				"    \"publishTime\",\n" + 
				"    \"report\"\n" + 
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
	public String hotArticleListTransNum(int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime, int isOrigin,String orderField, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
//		String query = Utils.queryString(platformTypeId, platformIdList);

		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		if(isOrigin != 3) {//0:非原创，1:原创，3：全部
			paramterMap.put("isOrigin",Arrays.asList(String.valueOf(isOrigin)));
		}
		String query = Utils.queryString(paramterMap);
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
		log.info(queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public int getTransNum(String unionId) {
		int transNum=0;
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);
		String queryString="{\r\n" + 
				"  \"size\": 0,\r\n" + 
				"  \"query\": {\r\n" + 
				"    \"bool\": {\r\n" + 
				"      \"must\": [\r\n" + 
				"{\r\n" + 
				"          \"query_string\": {\r\n" + 
				"            \"query\": \"unionId:"+unionId+"\",\r\n" + 
				"            \"analyze_wildcard\": true\r\n" + 
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
				"    \"unionId\": {\r\n" + 
				"      \"terms\": {\r\n" + 
				"        \"field\": \"unionId\",\r\n" + 
				"        \"size\": 300000,\r\n" + 
				"        \"order\": {\r\n" + 
				"          \"_count\": \"desc\"\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		//log.info("queryString:"+queryString);
		try {
			String rst= elasticRepository.queryES("GET", endpoint, queryString);
			JSONArray temp=JSON.parseObject(rst).getJSONObject("aggregations").getJSONObject("unionId").getJSONArray("buckets");
			if(temp.size()>0)
				transNum=temp.getJSONObject(0).getIntValue("doc_count");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transNum;
	}

}
