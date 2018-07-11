package com.ronglian.repository;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月21日 下午3:03:43
* @description:描述
*/
public interface ElasticModifyRepository {

	public <T> void bulkAdd(String indexName, String indexType, Map<String,T> entityMap) throws JsonProcessingException;
	
}
