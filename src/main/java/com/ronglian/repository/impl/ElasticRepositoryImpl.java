package com.ronglian.repository.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.TransFunction;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 上午11:59:25
* @description:描述
*/
@Slf4j
@Repository("elasticRepository")
public class ElasticRepositoryImpl implements ElasticRepository {
	
	private Client client;
	
	@Value("${imonitor.elasticsearch.clusterName}")
	private String clusterName;
	
	@Value("${imonitor.elasticsearch.host}")
	private String host;

	@Value("${imonitor.elasticsearch.port}")
	private Integer port;
//	
	private RestClient restClient;
//	
    private static Map<String, String> params = new HashMap<>();

    @Value("${imonitor.elasticsearch.hosts}")
    private String hosts;

    static {
        params.put("pretty", "true");
        params.put("ignore_unavailable", "true");
    }

    @PostConstruct
    public void init(){
        String[] hostsArray = hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hostsArray.length];
        for(int i = 0; i < hostsArray.length; i++){
            String[] ipPort = hostsArray[i].split(":");
            HttpHost httpHost = new HttpHost(ipPort[0], Integer.parseInt(ipPort[1]), "http");
            httpHosts[i] = httpHost;
        }
        restClient = RestClient.builder(httpHosts).setMaxRetryTimeoutMillis(60000).build();
    
        log.info("-------------------------------imonitor.elasticsearch.host:"+host);
		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.ignore_cluster_name", false).build();
		this.client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, port)));		
    }

    /**
     * query from elastic
     * @param method httpMethod
     * @param endpoint index and operate such as _search
     * @param query es query
     * @param trans TransFunction impl
     * @param headers HTTP HEADER
     * @param <T> result class
     * @return List T
     * @throws IOException
     */
    public <T> T queryES(String method, String endpoint, String query, TransFunction<String, T> trans, Header... headers) throws IOException {
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        if(log.isDebugEnabled()) {
            log.debug("es请求索引:" + endpoint + ", 请求体:" + query);
        }
        Response response = restClient.performRequest(method, endpoint, params, entity, headers);
        return transResponse(trans, response);
    }
    /**
     * query from elastic
     * @param method httpMethod
     * @param endpoint index and operate such as _search
     * @param query es query
     * @param trans TransFunction impl
     * @param headers HTTP HEADER
     * @param <T> result class
     * @return List T
     * @throws IOException
     */
    public String queryES(String method, String endpoint, String query, Header... headers) throws IOException {
    	HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
//    	if(log.isDebugEnabled()) {
    	log.info("es请求索引:" + endpoint + ", 请求体:" + query);
//    	}
    	long begingtime = System.currentTimeMillis();
    	Response response = restClient.performRequest(method, endpoint+"/_search", params, entity, headers);
    	long endtime = System.currentTimeMillis();
    	log.info("es请求时间:"+(endtime-begingtime));
    	return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    }
    /**
     * query from elastic
     * @param method httpMethod
     * @param endpoint index and operate such as _search
     * @param query es query
     * @param trans TransFunction impl
     * @param headers HTTP HEADER
     * @param <T> result class
     * @return List T
     * @throws IOException
     */
    public String updateES(String method, String endpoint, String query, Header... headers) throws IOException {
//    	 query = "{\r\n" + 
//				"    \"doc\" : "+query+
//				"}";
    	HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
    	System.out.println(query);
    	if(log.isDebugEnabled()) {
    		log.debug("es请求索引:" + endpoint + ", 请求体:" + query);
    	}
    	Response response = restClient.performRequest("POST", endpoint+"/_update", new HashMap<>(), entity, headers);
    	return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    }

    /**
     * response to result list
     * @param trans  TransFunction impl
     * @param response  org.elasticsearch.client.Response
     * @param <T> result class
     * @return
     * @throws IOException
     */
    private <T> T transResponse(TransFunction<String, T> trans, Response response) throws IOException {
        String source = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        logger.info("查询结果：" + source);
        return trans.transfer(source);
    }

    @Override
	public <T> void bulkAdd(String indexName, String indexType, Map<String,T> entityMap) throws JsonProcessingException {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
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

	@Override
	public String delteteES(String endpoint,String query,Integer scrollSize,Header ... headers) throws IOException{
		HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
    	if(log.isDebugEnabled()) {
    		log.debug("es请求索引:" + endpoint + ", 请求体:" + query);
    	}
    	Response response = restClient.performRequest("POST", endpoint+"/_delete_by_query?scroll_size="+(scrollSize!=null?scrollSize:1000), new HashMap<>(), entity, headers);
    	return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
	}

	@Override
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
}
