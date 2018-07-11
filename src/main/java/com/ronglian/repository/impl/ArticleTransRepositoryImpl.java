/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.repository.impl 
 * @author: YeohLee   
 * @date: 2018年6月20日 下午7:25:14 
 */
package com.ronglian.repository.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.ronglian.repository.ArticleTransRepository;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.utils.Utils;

 /** 
 * @ClassName: ArticleTransRepositoryImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月20日 下午7:25:14  
 */
@Slf4j
@Repository("articleTransRepository")
public class ArticleTransRepositoryImpl implements ArticleTransRepository {

	@Autowired
	private ElasticRepository elasticRepository;

	/* (non-Javadoc)
	 * @see com.ronglian.repository.ArticleTransRepository#getTransList(java.lang.String)
	 */
	@Override
	public String getTransList(String unionId,int page,int pageSize ) throws IOException {
		// TODO Auto-generated method stub
		
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);

		String queryString = "{ \r\n" + 
				"  \"from\": "+(page-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 				
				"  \"query\": {\r\n" + 
				"	\"match\": {\r\n" + 
				"	  \"unionId\": {\r\n" + 
				"		\"query\": \""+unionId+"\",\r\n" + 
				"		\"type\": \"phrase\"\r\n" + 
				"	 }\r\n" + 
				"	}\r\n" + 
				"  },\r\n" + 
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
	private static String queryString(Map<String,List<String>> paramterMap) {
		String result = "";
		if(!paramterMap.isEmpty()) {
			for(Map.Entry<String,List<String>> entry:paramterMap.entrySet()) {
				result += entry.getKey()+":"+String.join(" OR "+entry.getKey() + ":", entry.getValue());
				result += " AND ";
			}
			result = result.substring(0,result.length()-4);
			result = result.trim();
			result = "{\n" + 
					"          \"query_string\": {\n" + 
					"            \"query\": \""+result+"\",\n" + 
					"            \"analyze_wildcard\": true\n" + 
					"          }\n" + 
					"        }";
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see com.ronglian.repository.ArticleTransRepository#getTransInfo(java.lang.String)
	 */
	@Override
	public String getTransInfo(String webpageCode) throws IOException {
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
	/* (non-Javadoc)
	 * @see com.ronglian.repository.ArticleTransRepository#getTransListTotal(java.lang.String)
	 */
	@Override
	public String getTransListTotal(String unionId) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);
				String queryString = "{ \r\n" +
						" \"size\":\""+10000+"\",\r\n" +
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
	 * @see com.ronglian.repository.ArticleTransRepository#getTransListByTransTypeAndTort(java.lang.String, int, int, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String getTransListByTransTypeAndTort(String unionId, int page,
			int pageSize, Integer transType, Integer tort) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(tort != null) {
			paramterMap.put("isTort",Arrays.asList(String.valueOf(tort)));
		}
		if(unionId != null){
			paramterMap.put("unionId", Arrays.asList(unionId));
		}
		if(transType != null) {//0:非原创，1:原创，3：全部
			paramterMap.put("channel",Arrays.asList(String.valueOf(transType)));
		}
		String query = Utils.queryString(paramterMap);
		String queryString = "{ \r\n" + 
				"  \"from\": "+(page-1)*pageSize+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+"")+
				"      ]\n" + 
				"    }\n" + 
				"    },\n" + 
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
