package com.ronglian.repository.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.FocalMedia;
import com.ronglian.model.FocalMediaTrans;
import com.ronglian.model.FocalTransCount;
import com.ronglian.model.Platform;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.repository.FocalMediaRepository;
import com.ronglian.mapper.FocalMediaMapper;
import com.ronglian.mapper.PlatformMapper;
//import com.ronglian.utils.ESUtil;
import com.ronglian.utils.Utils;
/**
 * 用unionId来区分各条数据
 * 第一次的时候是全部的数据，包括unionId和url;之后只用unionId
 * @author ronglian
 *
 */
@Slf4j
@Repository
public class FocalMediaRepositoryImpl implements FocalMediaRepository{
	
	@Autowired
	private ElasticRepository elasticRepository;
	@Autowired
	private FocalMediaMapper focalMediaMapper;
	@Autowired
	private PlatformMapper platformMapper;
	/**
	 * 重点媒体转载统计查询
	 * 当前查询分片不合理，之后要改成按转载时间分片。
	 * @throws IOException 
	 * queryType:1按媒体统计，2按平台统计
	 * */
	@Override
	public PageResult<FocalTransCount> findFocalTransCountPage(List<String> queryIds,Integer queryType,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException{
		//String endpoint = Utils.endpointCreater("focal_media_trans-",0, startTime, endTime);
		String endpoint = "focal_media_trans-*";
		String aggs=null;
		String focalQuery = null;
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(queryIds!=null&&queryIds.size()>0){
			if(queryType==1||queryType==4){
				paramterMap.put("mediaId",queryIds);
			}else if(queryType==2||queryType==3){
				paramterMap.put("platformId",queryIds);
			}
		}
		String query = Utils.queryString2(paramterMap);
		if(queryType==0){
			aggs="  }\n" ;
		}else{
			if(queryType==1||queryType==3){
				aggs=	"  },\n" + 
						"\"aggs\": {\n"+
						"    \"mediaId\": {\n"+
						"      \"terms\": {\n"+
						"        \"field\": \"mediaId\",\n"+
						"        \"order\": {\n"+
						"          \"_count\": \"desc\"\n"+
						"        }"+
						"      },"+
						"      \"aggs\": {\n"+
						"        \"publishStatus\": {\n"+
						"          \"terms\": {\n"+
						"            \"field\": \"publishStatus\",\n"+
						"            \"order\": {\n"+
						"              \"_count\": \"desc\"\n"+
						"            }\n"+
						"          }\n"+
						"        }\n"+
						"      }\n"+
						"    }\n"+
						"  }\n";
			}else if(queryType==2||queryType==4){
				aggs=	"  },\n" + 	
						"\"aggs\": {\n"+
						"    \"platformId\": {\n"+
						"      \"terms\": {\n"+
						"        \"field\": \"platformId\",\n"+
						"        \"order\": {\n"+
						"          \"_count\": \"desc\"\n"+
						"        }\n"+
						"      },\n"+
						"      \"aggs\": {\n"+
						"        \"publishStatus\": {\n"+
						"          \"terms\": {"+
						"            \"field\": \"publishStatus\",\n"+
						"            \"order\": {\n"+
						"              \"_count\": \"desc\"\n"+
						"            }\n"+
						"          }\n"+
						"        }\n"+
						"      }\n"+
						"    }\n"+
						"  }\n";
			}
		}
		focalQuery = "{\n" + 
				"  \"from\": 0,\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"transTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				aggs +
				"}";
		String countQuery="{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"transTime\": {\n" + 
				"              \"gte\": "+startTime.getTime()+",\n" + 
				"              \"lte\": "+endTime.getTime()+",\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				aggs +
				"}";
		//String countQuery0="{\"query\": {\"bool\": {\"must\": [], \"must_not\": []}},\"_source\": false}";
		System.out.println(focalQuery);
		PageResult<FocalTransCount> pageResult = new PageResult<FocalTransCount>();
		String countResult=elasticRepository.queryES("GET", endpoint, countQuery);
		int totalCount = JSON.parseObject(countResult).getJSONObject("hits").getIntValue("total");
		//if(totalCount > 0&&queryIds!=null&&queryIds.size()>0) {
		if(totalCount > 0) {
			String focalMediaResult=elasticRepository.queryES("GET", endpoint, focalQuery);
			StringBuffer sb = new StringBuffer();
			log.debug(focalMediaResult);
			System.out.println(focalMediaResult);
			Set<String> ids=new HashSet<String>();
			List<FocalTransCount> list=new LinkedList<FocalTransCount>();
			List<FocalTransCount> resultList=new LinkedList<FocalTransCount>();
			if(queryType==1||queryType==3){
				JSONArray esJson= JSON.parseObject(focalMediaResult).getJSONObject("aggregations").getJSONObject("mediaId").getJSONArray("buckets");
				for(int i=0;i<esJson.size();i++){
					JSONObject element=esJson.getJSONObject(i);
					JSONArray insideJson= element.getJSONObject("publishStatus").getJSONArray("buckets");
					for(int j=0;j<insideJson.size();j++){
						list.add(new FocalTransCount(null,element.getString("key"),null,null,null,null,null,
								insideJson.getJSONObject(j).getInteger("key")==1? insideJson.getJSONObject(j).getInteger("doc_count"):0
										,insideJson.getJSONObject(j).getInteger("key")==0? insideJson.getJSONObject(j).getInteger("doc_count"):0));
						ids.add(element.getString("key"));
					}
				}
				if(ids!=null&&ids.size()>0){
					for(String id:ids){
						sb.append("\"");
						sb.append(id);
						sb.append("\"");
						sb.append(',');
						resultList.add(new FocalTransCount(null,id,null,null,null,null,null,0,0));
					}
					List<FocalMedia> focalMedias=focalMediaMapper.findFocalMediaByIds(sb.substring(0,sb.length()-1));
					for(int i=0;i<resultList.size();i++){
						for(int j=0;j<focalMedias.size();j++){
							if(resultList.get(i).getMediaId().equals(focalMedias.get(j).getId())){
								resultList.get(i).setMediaName(focalMedias.get(j).getName()+"_"+focalMedias.get(j).getTypeName());
								resultList.get(i).setOriginMediaName(focalMedias.get(j).getName());
							}
						}
						for(int k=0;k<list.size();k++){
							if(resultList.get(i).getMediaId().equals(list.get(k).getMediaId())){
								resultList.get(i).setHomePageTrans(resultList.get(i).getHomePageTrans()+list.get(k).getHomePageTrans());
								resultList.get(i).setNonHomeTrans(resultList.get(i).getNonHomeTrans()+list.get(k).getNonHomeTrans());
							}
						}
					}
				}
			}else if(queryType==2||queryType==4){
				JSONArray esJson= JSON.parseObject(focalMediaResult).getJSONObject("aggregations").getJSONObject("platformId").getJSONArray("buckets");
				for(int i=0;i<esJson.size();i++){
					JSONObject element=esJson.getJSONObject(i);
					JSONArray insideJson= element.getJSONObject("publishStatus").getJSONArray("buckets");
					for(int j=0;j<insideJson.size();j++){
						list.add(new FocalTransCount(null,null,element.getString("key"),null,null,null,null,
								insideJson.getJSONObject(j).getInteger("key")==1? insideJson.getJSONObject(j).getInteger("doc_count"):0
										,insideJson.getJSONObject(j).getInteger("key")==0? insideJson.getJSONObject(j).getInteger("doc_count"):0));
						ids.add(element.getString("key"));
					}
				}
				if(ids!=null&&ids.size()>0){
					for(String id:ids){
						sb.append("\"");
						sb.append(id);
						sb.append("\"");
						sb.append(',');
						resultList.add(new FocalTransCount(null,null,id,null,null,null,null,0,0));
					}
					List<Platform> platforms=platformMapper.findPlatformByIds(sb.substring(0,sb.length()-1));
					for(int i=0;i<list.size();i++){
						for(int j=0;j<platforms.size();j++){
							if(list.get(i).getPlatformId().equals(platforms.get(j).getId())){
								list.get(i).setPlatformName(platforms.get(j).getName()+"_"+platforms.get(j).getPlatformTypeName());
								list.get(i).setOriginPlatformName(platforms.get(j).getName());
							}
						}
					}
					for(int i=0;i<resultList.size();i++){
						for(int j=0;j<platforms.size();j++){
							if(resultList.get(i).getPlatformId().equals(platforms.get(j).getId())){
								resultList.get(i).setPlatformName(platforms.get(j).getName()+"_"+platforms.get(j).getPlatformTypeName());
								resultList.get(i).setOriginPlatformName(platforms.get(j).getName());
							}
						}
						for(int k=0;k<list.size();k++){
							if(resultList.get(i).getPlatformId().equals(list.get(k).getPlatformId())){
								resultList.get(i).setHomePageTrans(resultList.get(i).getHomePageTrans()+list.get(k).getHomePageTrans());
								resultList.get(i).setNonHomeTrans(resultList.get(i).getNonHomeTrans()+list.get(k).getNonHomeTrans());
							}
						}
					}
				}
			}
			Collections.sort(resultList);
			if(resultList.size()<=pageSize*(pageNo-1)){
				pageResult.setContent(null);
			}else if(resultList.size()<=pageSize*pageNo){
				pageResult.setContent(resultList.subList(pageSize*(pageNo-1),resultList.size()));
			}else{
				pageResult.setContent(resultList.subList(pageSize*(pageNo-1),pageSize*pageNo));
			}
			pageResult.setTotalElements(resultList.size());
			int totalPages = Utils.totalPage(resultList.size(), pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
		}else {
			pageResult.setTotalElements(0);
			pageResult.setTotalPages(0);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			pageResult.setContent(null);
		}
		return pageResult;
	}
	
	/**
	 * 重点媒体转载列表
	 * @throws IOException 
	 * queryType:
	 * 为1时，查询某媒体的平台转载情况，此时queryId是媒体Id
	 * 为2时，查询某平台的媒体转载情况，此时queryId是平台Id
	 * 为0时，查询所有平台、媒体的转载情况，此时不会用到queryId
	 * */
	@Override
	public PageResult<FocalMediaTrans> findFocalMediaTransPage(List<String> queryIds,Integer queryType,Integer publishStatus,String mediaId,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException{
		//String endpoint = Utils.endpointCreater("focal_media_trans-*",0, startTime, endTime);
		String endpoint ="focal_media_trans-*";
		//String countQuery="{\"query\": {\"bool\": {\"must\": [], \"must_not\": []}},\"_source\": false}";
		
		Map<String,List<String>> paramterMap = new HashMap<>();
		if(publishStatus!=null){
			List<String> publishStatuses =new LinkedList<String>();
			if(publishStatus==1){
				publishStatuses.add("1");
			}else if(publishStatus==0){
				publishStatuses.add("0");
				publishStatuses.add("3");
			}
			paramterMap.put("publishStatus",publishStatuses);
		}
		if(queryType==1){
			if(!StringUtils.isEmpty(mediaId)){
				List<String> mediaIds =new LinkedList<String>();
				mediaIds.add(mediaId);
				paramterMap.put("platformId",queryIds);
				paramterMap.put("mediaId",mediaIds);
			}else{
				paramterMap.put("mediaId",queryIds);
			}
		}else if(queryType==2||queryType==3){
			if(!StringUtils.isEmpty(mediaId)){
				List<String> mediaIds =new LinkedList<String>();
				mediaIds.add(mediaId);
				paramterMap.put("mediaId",mediaIds);
			}
			paramterMap.put("platformId",queryIds);
		}
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"from\": "+((pageNo-1)*pageSize)+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query+",")+
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"transTime\": {\n" + 
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
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"transTime\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.debug(queryString);
		PageResult<FocalMediaTrans> pageResult = new PageResult<FocalMediaTrans>();
		//String countResult=elasticRepository.queryES("GET", endpoint, countQuery);
		String focalMediaResult=elasticRepository.queryES("GET", endpoint, queryString);
		log.debug(focalMediaResult);
		System.out.println(focalMediaResult);
		int totalCount = JSON.parseObject(focalMediaResult).getJSONObject("hits").getIntValue("total");
		if(totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<FocalMediaTrans> list=new LinkedList<FocalMediaTrans>();
			JSONArray esJson=JSON.parseObject(focalMediaResult).getJSONObject("hits").getJSONArray("hits");
			for(int i=0;i<esJson.size();i++){
				JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
				
				list.add(new FocalMediaTrans(element.getString("id"),element.get("unionId")!=null?(String)element.get("unionId"):null,element.getString("platformId"),element.getString("originalId"),
						element.getString("mediaId"),element.getString("transTitle"),element.getString("transUrl"),element.getDate("transTime"),element.getInteger("publishStatus"),element.getInteger("homePageSeconds"),
						element.getString("originalTitle"),element.getDate("updateTime"),element.getDouble("percent")));
			}
			pageResult.setContent(list);
		}else {
			pageResult.setTotalElements(0);
			pageResult.setTotalPages(0);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			pageResult.setContent(null);
		}
		return pageResult;
	}

	/**
	 * 存储重点媒体转载记录
	 * @throws JsonProcessingException 
	 * 
	 * */
	@Override
	public void saveFocalMediaTrans(List<FocalMediaTrans> focalMediaTransList) throws JsonProcessingException{
		//new日期对象
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
			Map<String,FocalMediaTrans> esMap=new HashMap<String,FocalMediaTrans>();
			esMap.put(focalMediaTrans.getId(),focalMediaTrans);
			//ESUtil.getInstance().bulkAdd("focal_media_trans-"+dateFormat.format(focalMediaTrans.getTransTime()),"imonitor",esMap);
			elasticRepository.bulkAdd("focal_media_trans-"+dateFormat.format(focalMediaTrans.getTransTime()),"imonitor",esMap);
		}
	}
	
	private String getDistributeQuery(String queryId,Integer queryType){
		String result = null;
		
		if(queryType==0||queryId==null){
			result="";
		}else if(queryType==1){
			result = "        {\n" + 
					"          \"query_string\": {\n" + 
					"            \"query\": \"mediaId:"+queryId+"\",\n" + 
					"            \"analyze_wildcard\": true\n" + 
					"          }\n" + 
					"        }"; 
		}else if(queryType==2){
			result = "        {\n" + 
					"          \"query_string\": {\n" + 
					"            \"query\": \"platformId:"+queryId+"\",\n" + 
					"            \"analyze_wildcard\": true\n" + 
					"          }\n" + 
					"        }"; 
		}
		return result;
	}

	@Override
	public void updateFocalMediaTrans(List<FocalMediaTrans> focalMediaTransList) throws IOException{
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String query = null;
		//ObjectMapper objectMapper = new ObjectMapper();
		for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
	    	 query = "{\r\n" + 
					"    \"doc\" : "+"   {  \"publishStatus\":"+focalMediaTrans.getPublishStatus()+
					",\"homePageSeconds\":"+focalMediaTrans.getHomePageSeconds()+
					" } }";
			elasticRepository.updateES("POST","/focal_media_trans-"+dateFormat.format(focalMediaTrans.getTransTime())+"/imonitor/"+focalMediaTrans.getId(), query);
		}
	}

	@Override
	public Map<String,FocalMediaTrans> findByIds(List<String> ids) throws IOException{
		String endpoint = "focal_media_trans-*";
		Map<String,FocalMediaTrans> resultMap = new HashMap<String,FocalMediaTrans>();
		Map<String,List<String>> paramterMap = new HashMap<>();
		paramterMap.put("id",ids);
		String query = Utils.queryString2(paramterMap);
		String queryString = "{\n" + 
				"  \"_source\": [\n" + 
				"    \"id\",\n" + 
				"    \"platformId\",\n" + 
				"    \"originalId\",\n" + 
				"    \"mediaId\",\n" + 
				"    \"transTitle\",\n" + 
				"    \"transTime\",\n" + 
				"    \"publishStatus\",\n" + 
				"    \"homePageSeconds\",\n" + 
				"    \"originalTitle\",\n" + 
				"    \"updateTime\",\n" + 
				"    \"percent\"\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ]\n" + 
				"    }\n" + 
				"  }\n" + 
				"}";
		System.out.println(queryString);
		String focalMediaResult=elasticRepository.queryES("GET", endpoint, queryString);
		System.out.println(focalMediaResult);
		JSONArray esJson=JSON.parseObject(focalMediaResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++) {
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			resultMap.put(element.getString("id"),new FocalMediaTrans(element.getString("id"),element.get("unionId")!=null?(String)element.get("unionId"):null,element.getString("platformId"),element.getString("originalId"),
					element.getString("mediaId"),element.getString("transTitle"),element.getString("transUrl"),element.getDate("transTime"),element.getInteger("publishStatus"),element.getInteger("homePageSeconds"),
					element.getString("originalTitle"),element.getDate("updateTime"),element.getDouble("percent")));
		}
		return resultMap;
	}

	@Override
	public void overByMediaId(String mediaId) throws IOException{
		Map<String,List<String>> paramterMap = new HashMap<>();
		List<String> mediaIds =new LinkedList<String>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		mediaIds.add(mediaId);
		paramterMap.put("mediaId",mediaIds);
		String query = Utils.queryString2(paramterMap);
		String updateQuery = "{\r\n" + 
				"    \"doc\" : "+"   {  \"publishStatus\":4 " +
				" } }";
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n " + ("".equals(query)?"":query)+
				"      ],\n" +  
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  }\n" +  
				"}";
		String focalMediaResult=elasticRepository.queryES("GET", "focal_media_trans-*", queryString);
		System.out.println(focalMediaResult);
		JSONArray esJson=JSON.parseObject(focalMediaResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++) {
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			elasticRepository.updateES("POST","/focal_media_trans-"+dateFormat.format(element.getDate("transTime"))+"/imonitor/"+element.getString("id"), updateQuery);
		}
	}
}