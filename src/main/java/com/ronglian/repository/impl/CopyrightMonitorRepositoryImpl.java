package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ronglian.repository.CopyrightMonitorRepository;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("copyrightMonitorRepository")
public class CopyrightMonitorRepositoryImpl implements CopyrightMonitorRepository{
	
	@Autowired
	private ElasticRepository elasticRepository;

	@Override
	public String getOriginalArticle(String unionId) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",1, null, null);
		String queryString = "{ \r\n" + 
				"  \"query\": {\r\n" + 
				"	\"match\": {\r\n" + 
				"	  \"unionId\": {\r\n" + 
				"		\"query\": \""+unionId+"\",\r\n" + 
				"		\"type\": \"phrase\"\r\n" + 
				"	 }\r\n" + 
				"	}\r\n" + 
				"  }\r\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	/* (non-Javadoc)
	 * @see com.ronglian.repository.CopyrightMonitorRepository#getOriginalArticleListByArticleId(java.lang.String)
	 */
	@Override
	public String getOriginalArticleListByArticleId(String articleId)
			throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",1, null, null);
		String queryString = "{ \r\n" + 
				"  \"query\": {\r\n" + 
				"	\"match\": {\r\n" + 
				"	  \"articleId\": {\r\n" + 
				"		\"query\": \""+articleId+"\",\r\n" + 
				"		\"type\": \"phrase\"\r\n" + 
				"	 }\r\n" + 
				"	}\r\n" + 
				"  }\r\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String getTransArticle(String webpageCode)throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);
		String queryString = "{ \r\n" + 
				"  \"query\": {\r\n" + 
				"	\"match\": {\r\n" + 
				"	  \"webpageCode\": {\r\n" + 
				"		\"query\": \""+webpageCode+"\",\r\n" + 
				"		\"type\": \"phrase\"\r\n" + 
				"	 }\r\n" + 
				"	}\r\n" + 
				"  }\r\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	@Override
	public String getTitleList(String titleWord,int page,int pageSize) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",1, null, null);
		String queryString = "{ \r\n" + 
				"  \"from\": "+(page-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"    \"_source\": [\r\n" + 
				"          \"unionId\",\r\n" + 
				"          \"articleId\",\r\n" + 
				"          \"title\",\r\n" + 
				"          \"url\",\r\n" + 
				"          \"platformName\",\r\n" + 
				"          \"publishTime\"\r\n" + 
				"	],\r\n" + 
				"  	\"query\": {\r\n" + 
				"    \"bool\": {\r\n" + 
				"      \"must\": [\r\n" + 
				"        {\r\n" + 
				"          \"query_string\": {\r\n" + 
				"            \"default_field\": \"title\",\r\n" + 
				"            \"query\": \""+titleWord+"\"\r\n" + 
				"          }\r\n" + 
				"        }\r\n" + 
				"      	],\r\n" + 
				"      \"must_not\": [],\r\n" + 
				"      \"should\": []\r\n" + 
				"    }\r\n" + 
				"  }"+
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.repository.CopyrightMonitorRepository#getOriginalArticleList(java.util.List)
	 */
	@Override
	public String getOriginalArticleList(List<String> unionIdList)
			throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_article-",1, null, null);
		String query = queryString( unionIdList);
		String queryString = "{ \r\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"publishTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.info("queryString:"+queryString);
		return elasticRepository.queryES("GET", endpoint, queryString);
	}
	
	private static String queryString(List<String> platformIdList) {
		String result = null;
		if((platformIdList == null || platformIdList.isEmpty())) {
			result = "";
		}else if( (platformIdList != null && !platformIdList.isEmpty())) {
			
			result = "        {\n" + 
					"          \"query_string\": {\n" + 
					"            \"query\": \"unionId:"+String.join(" or unionId:", platformIdList)+"\",\n" + 
					"            \"analyze_wildcard\": true\n" + 
					"          }\n" + 
					"        }";
		}
		return result;
	}


}
