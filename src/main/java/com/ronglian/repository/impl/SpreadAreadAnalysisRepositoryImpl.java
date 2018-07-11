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
import com.ronglian.repository.SpreadAreadAnalysisRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午2:50:39
* @description:描述
*/
@Slf4j
@Repository("spreadAreadAnalysisRepository")
public class SpreadAreadAnalysisRepositoryImpl implements SpreadAreadAnalysisRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String areaTransList(String areaName, int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",accountType, startTime, endTime);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(!StringUtils.isEmpty(platformTypeId)) {
			paramterMap.put("platformTypeId",Arrays.asList(platformTypeId));
		}
		if(platformIdList != null && !platformIdList.isEmpty()) {
			paramterMap.put("platformId", platformIdList);
		}
		paramterMap.put("province",Arrays.asList(String.valueOf(areaName)));
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
				"    \"webpageUrl\",\n" + 
				"    \"articleId\",\n" + 
				"    \"title\",\n" + 
				"    \"crawlSource\",\n" + 
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

}
