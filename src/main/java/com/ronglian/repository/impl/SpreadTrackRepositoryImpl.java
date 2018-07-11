package com.ronglian.repository.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.SpreadTrackRepository;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月28日 下午8:58:13
* @description:描述
*/
@Slf4j
@Repository("spreadTrackRepository")
public class SpreadTrackRepositoryImpl implements SpreadTrackRepository {

	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public String getTransArticles(String unionId) throws IOException {
		// TODO Auto-generated method stub
		String queryString = "{\n" + 
				"  \"size\":1000,"+
				"  \"_source\":[\n" + 
				"    \"reportSource\",\n" + 
				"    \"crawlSource\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"match\": {\n" + 
				"      \"unionId\": {\n" + 
				"        \"query\": \""+unionId+"\",\n" + 
				"        \"type\": \"phrase\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", "imonitor_trans_info-*", queryString);
	}

	@Override
	public String getArticle(String unionId) throws IOException {
		// TODO Auto-generated method stub
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"match\": {\n" + 
				"      \"_id\": {\n" + 
				"        \"query\": \""+unionId+"\",\n" + 
				"        \"type\": \"phrase\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		log.info(queryString);
		return elasticRepository.queryES("GET", "imonitor_article-*", queryString);
	}

}
