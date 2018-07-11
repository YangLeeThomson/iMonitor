package com.ronglian.repository.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.repository.ElasticModifyRepository;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月21日 下午3:04:08
* @description:描述
*/
@Slf4j
@Repository("elasticModifyRepository")
public class ElasticModifyRepositoryImpl implements ElasticModifyRepository {
	
	private Client client;
	
	@Value("${imonitor.elasticsearch.clusterName}")
	private String clusterName;
	
	@Value("${imonitor.elasticsearch.host}")
	private String host;
	
	@PostConstruct
    public void init(){
		log.info("-------------------------------imonitor.elasticsearch.host:"+host);
		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.ignore_cluster_name", false).build();
		this.client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, 9500)));		
	}
	
//	@Override
//	public <T> void bulkAdd(String indexName, String indexType, List<T> entityList){
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        for(T t:entityList){
//            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType, "{_id字段}").setSource(t);
//            bulkRequest.add(indexRequestBuilder);
//        }
//        BulkResponse bulkResponse = bulkRequest.get();
//        if (bulkResponse.hasFailures()) {
//            // process failures by iterating through each bulk response item
//        	log.error("elasticsearch add failures!");
//        }
//        
//    }
//	@Override
//	public void bulkAdd(String indexName, String indexType, List<Article> entityList){
//		BulkRequestBuilder bulkRequest = client.prepareBulk();
//		for(Article t:entityList){
//			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType, t.getUnionid()).setSource(t);
//			bulkRequest.add(indexRequestBuilder);
//		}
//		BulkResponse bulkResponse = bulkRequest.get();
//		if (bulkResponse.hasFailures()) {
//			// process failures by iterating through each bulk response item
//			log.error("elasticsearch add failures!");
//		}
//		
//	}
	@Override
	public <T> void bulkAdd(String indexName, String indexType, Map<String,T> entityMap) throws JsonProcessingException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
//		ObjectMapper objectMapper = new ObjectMapper();
		for (Map.Entry<String,T > entry : entityMap.entrySet()) {
			String esjson = JSONObject.toJSONString(entry.getValue());
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType).setId(entry.getKey()).setSource(esjson);
			bulkRequest.add(indexRequestBuilder);
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			//log.debug(queryString);
		}
	}
}
