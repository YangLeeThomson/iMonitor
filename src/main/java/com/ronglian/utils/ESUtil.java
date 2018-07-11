package com.ronglian.utils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.model.Article;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 黄硕/huangshuo
 * @date:2018年6月14日 上午11:44:59
 * @description:描述
 */
@Slf4j
public class ESUtil {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

	private Client client;

	private ESUtil() {
		setNodeClient();
	}

	public static ESUtil getInstance() {
		return LazyHolder.INSTANCE;
	}

	public Client getClient() {
		return client;
	}

	/**
	 * init the ES client with version 5.2.0
	 */
	private void setNodeClient() {

		String clusterName = "sbs";

		String host = "172.17.101.141";

		Settings settings = Settings.builder().put("cluster.name", clusterName)
				.put("client.transport.ignore_cluster_name", false)
				// .put("node.client", true)
//				 .put("client.transport.sniff", true)
				.build();

		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, 9500)));
	}

	public <T> void bulkAdd(String indexName, String indexType, Map<String,T> entityMap) throws JsonProcessingException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Map.Entry<String,T > entry : entityMap.entrySet()) {
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType).setId(entry.getKey()).setSource(objectMapper.writeValueAsString(entry.getValue()));
			bulkRequest.add(indexRequestBuilder);
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			log.info(bulkResponse.buildFailureMessage());
			System.out.println(bulkResponse.buildFailureMessage());
		}
	}
	
	public <T> void bulkAdd(String indexName, String indexType, List<T> entityList) throws JsonProcessingException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		ObjectMapper objectMapper = new ObjectMapper();
		for (T article:entityList) {
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, indexType).setSource(objectMapper.writeValueAsString(article));
			bulkRequest.add(indexRequestBuilder);
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			log.info(bulkResponse.buildFailureMessage());
			System.out.println(bulkResponse.buildFailureMessage());
		}

	}

	public <T> void bulkUpdate(String indexName, String indexType, Map<String,T> entityMap) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map.Entry<String,T > entry : entityMap.entrySet()) {
			UpdateRequestBuilder updateRequestBuilder=client.prepareUpdate(indexName, indexType,entry.getKey()).setDoc(entry.getValue());
			bulkRequest.add(updateRequestBuilder);
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			//log.debug(queryString);
		}
	}
	
	public void updateSingle(String indexName, String indexType,String id, Map popertiesMap) {
		UpdateRequestBuilder urb= client.prepareUpdate(indexName, indexType, id);
        urb.setDoc(popertiesMap);
        urb.execute().actionGet();
	}
	
	public void updateList(String indexName, String indexType, Map<String,Map> entitysMap) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map.Entry<String,Map > entry : entitysMap.entrySet()) {
			UpdateRequestBuilder updateRequestBuilder=client.prepareUpdate(indexName, indexType,entry.getKey()).setDoc(entry.getValue());
			bulkRequest.add(updateRequestBuilder);
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			//log.debug(queryString);
		}
	}
	
	private static class LazyHolder implements Serializable {
		private static final ESUtil INSTANCE = new ESUtil();
	}
}
