package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.PlatformCompareAnalysisRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月18日 下午11:47:42
* @description:描述
*/
@Slf4j
@Repository("platformCompareAnalysisRepository")
public class PlatformCompareAnalysisRepositoryImpl implements PlatformCompareAnalysisRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String articleInfoCompare(int accountType,String platformTypeId,List<String> platformIdList, Date startTime, Date endTime) throws IOException {
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
				"    \"platformId\": {\n" + 
				"      \"terms\": {\n" + 
				"        \"field\": \"platformId\",\n" + 
				"        \"size\": 50,\n" + 
				"        \"order\": {\n" + 
				"          \"_count\": \"desc\"\n" + 
				"        }\n" + 
				"      },\n" + 
				"      \"aggs\": {\n" + 
				"        \"subscribeNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"subscribeNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"transNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"transNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"shareNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"shareNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"commentNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"commentNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"thumbsNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"thumbsNum\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        \"awardNum\": {\n" + 
				"          \"sum\": {\n" + 
				"            \"field\": \"awardNum\"\n" + 
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

}
