package com.ronglian.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.service.TransFunction;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 上午11:57:55
* @description:ES查询
*/
public interface ElasticRepository {
	<T> T queryES(String method, String endpoint, String query, TransFunction<String, T> trans, Header... headers) throws IOException;
	String queryES(String method, String endpoint, String query, Header... headers) throws IOException;
	public String updateES(String method, String endpoint, String query, Header... headers) throws IOException;
	public String delteteES(String endpoint, String query, Integer scrollSize, Header... headers) throws IOException;
	public <T> void bulkAdd(String indexName, String indexType, Map<String,T> entityMap) throws JsonProcessingException;
	public <T> void bulkAdd(String indexName, String indexType, List<T> entityList) throws JsonProcessingException;
}
