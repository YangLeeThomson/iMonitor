/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.repository.impl 
 * @author: YeohLee   
 * @date: 2018年6月21日 上午11:17:24 
 */
package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.SpreadRepository;
import com.ronglian.utils.Utils;

 /** 
 * @ClassName: SpreadRepositoryImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月21日 上午11:17:24  
 */
@Slf4j
@Component("spreadRepository")
public class SpreadRepositoryImpl implements SpreadRepository {

	@Autowired
	private ElasticRepository elasticRepository;

	/* (non-Javadoc)
	 * @see com.ronglian.repository.SpreadRepository#getPlatformTransOrder(int, java.lang.String, java.util.List, java.util.Date, java.util.Date)
	 */
	@Override
	public String getPlatformTransOrder(int accountType, String platformTypeId,
			List<String> platformIdList, Date startTime, Date endTime)
			throws IOException {
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
}
