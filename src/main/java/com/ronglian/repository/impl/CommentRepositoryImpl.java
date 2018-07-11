package com.ronglian.repository.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.Comment;
import com.ronglian.model.CommentDistribute;
import com.ronglian.repository.CommentRepository;
import com.ronglian.repository.ElasticRepository;
//import com.ronglian.utils.ESUtil;
import com.ronglian.utils.Utils;

@Slf4j
@Repository
public class CommentRepositoryImpl implements CommentRepository{
	
	@Autowired
	private ElasticRepository elasticRepository;
	
	@Override
	public void saveComment(List<Comment> commentList) throws JsonProcessingException{
		if(commentList!=null&&commentList.size()>0){
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		for(Comment comment:commentList){
			Map<String,Comment> esMap=new HashMap<String,Comment>();
			esMap.put(comment.getId(),comment);
			//ESUtil.getInstance().bulkAdd("comment-"+dateFormat.format(comment.getPublishTime()),"imonitor",esMap);
			elasticRepository.bulkAdd("comment-"+dateFormat.format(comment.getPublishTime()),"imonitor",esMap);
		}
		}
	}

	@Override
	public List<Comment> getCommentByUnionIds(List<String> unionIds) throws IOException{
		String endpoint ="comment-*";
		Map<String, List<String>> parameterMap =new HashMap<String, List<String>>();
		parameterMap.put("unionId",unionIds);
		String query=Utils.queryString(parameterMap);
		String queryString = "{\n" + 
				"\"size\": 10000,\n" +
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" +  
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  }\n" +  
				"}";
		log.debug(queryString);
		List<Comment> commentList = new LinkedList<Comment>();
		String focalMediaResult=elasticRepository.queryES("GET", endpoint, queryString);
		log.debug(focalMediaResult);
		System.out.println(focalMediaResult);
		JSONArray esJson=JSON.parseObject(focalMediaResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++){
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			commentList.add(new Comment(element.getString("id"),element.getString("userName"),element.getString("region"),element.getDate("publishTime"),element.getInteger("upNum"),
					element.getInteger("downNum"),element.getString("unionId"),element.getString("content")));
		}
		return commentList;
	}

	@Override
	public PageResult<Comment> findPageList(String orderField,String unionId,int pageNo,int pageSize) throws IOException{
		String endpoint ="comment-*";
		
		String query = 
				"        {\n" + 
				"          \"query_string\": {\n" + 
				"            \"query\": \"unionId:"+unionId+"\",\n" + 
				"            \"analyze_wildcard\": true\n" + 
				"          }\n" + 
				"        }";
		String countQuery="{\"query\": {\"bool\": {\"must\": ["+("".equals(query)?"":query)+"], \"must_not\": []}},\"_source\": false}";
		String queryString = "{\n" + 
				"  \"from\": "+((pageNo-1)*pageSize)+",\n" + 
				"  \"size\": "+pageSize+",\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \""+orderField+"\": {\n" + 
				"        \"order\": \"desc\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		log.debug(queryString);
		PageResult<Comment> pageResult = new PageResult<Comment>();
		String countResult=elasticRepository.queryES("GET", endpoint, countQuery);
		String commentResult=elasticRepository.queryES("GET", endpoint, queryString);
		log.debug(commentResult);
		System.out.println(commentResult);
		int totalCount = JSON.parseObject(countResult).getJSONObject("hits").getIntValue("total");
		if(totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<Comment> list=new LinkedList<Comment>();
			JSONArray esJson=JSON.parseObject(commentResult).getJSONObject("hits").getJSONArray("hits");
			for(int i=0;i<esJson.size();i++){
				JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
				list.add(new Comment(element.getString("id"),element.getString("userName"),element.getString("region"),element.getDate("publishTime"),element.getInteger("upNum"),
						element.getInteger("downNum"),element.getString("unionId"),element.getString("content")));
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

	@Override
	public List<CommentDistribute> getCommentDistribute(Integer queryType,String unionId) throws IOException{
		List<CommentDistribute> commentDistributeList = new LinkedList<CommentDistribute>();
		String endpoint = "comment-*";
		String aggs=null;
		String focalQuery = null;
		String query =  
				"        {\n" + 
				"          \"query_string\": {\n" + 
				"            \"query\": \"unionId:"+unionId+"\",\n" + 
				"            \"analyze_wildcard\": true\n" + 
				"          }\n" + 
				"        }"; 
		if(queryType==0){
			aggs="  }\n" ;
		}else{
			if(queryType==1){
				aggs=	"  },\n" + 
						"\"aggs\": {\n"+
						"    \"region\": {\n"+
						"      \"terms\": {\n"+
						"        \"field\": \"region\",\n"+
						"        \"order\": {\n"+
						"          \"_count\": \"desc\"\n"+
						"        }"+
						"      }"+
						"    }\n"+
						"  }\n";
			}else if(queryType==2){
				aggs=	"  },\n" + 	
						"\"aggs\": {\n"+
						"\"publishTime\": {\n"+
						"\"date_histogram\": {\n"+
				        "\"field\": \"publishTime\",\n"+
				        "\"time_zone\":\"+08:00\",\n" +
				        "\"interval\": \"day\",\n"+ 
				        "\"format\": \"yyyy-MM-dd\",\n"+
				        "\"order\": {\n"+
						"\"_count\": \"desc\"\n"+
						" }"+
				        "}\n"+
						"}\n"+
						"}\n";
			}
		}
		focalQuery = "{\n" + 
				"  \"from\": 0,\n" + 
				"  \"size\": 0,\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				aggs +
				"}";
		System.out.println(focalQuery);
		String focalMediaResult=elasticRepository.queryES("GET", endpoint, focalQuery);
		log.debug(focalMediaResult);
		System.out.println(focalMediaResult);
			if(queryType==1){
				JSONArray esJson= JSON.parseObject(focalMediaResult).getJSONObject("aggregations").getJSONObject("region").getJSONArray("buckets");
				for(int i=0;i<esJson.size();i++){
					JSONObject element=esJson.getJSONObject(i);
					commentDistributeList.add(new CommentDistribute(element.getString("key"),element.getInteger("doc_count")));
				}
			}else if(queryType==2){
				JSONArray esJson= JSON.parseObject(focalMediaResult).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets");
				for(int i=0;i<esJson.size();i++){
					JSONObject element=esJson.getJSONObject(i);
					commentDistributeList.add(new CommentDistribute(element.getString("key_as_string"),element.getInteger("doc_count")));
				}
			}
		if(queryType==2&&commentDistributeList!=null&&commentDistributeList.size()>0){
			Collections.sort(commentDistributeList, new Comparator<CommentDistribute>() {
	            public int compare(CommentDistribute s1,CommentDistribute s2){
	        		try{
	        			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        			Date d1= sdf.parse(s1.getKey());
	        			Date d2 = sdf.parse(s2.getKey());
	        			return  (int)((d1.getTime() - d2.getTime())/(24*3600*1000)) ;
	        			}catch(ParseException e){
	        				e.printStackTrace();
	        			}
	        		return 0;
	        	}
			});
		}
		return commentDistributeList;
	}

	@Override
	public Map<String,Object> getCommentAnalysis(Integer queryType,String unionId){
		
		return null;
	}

	@Override
	public void deleteCommentByIds(List<String> commentIds) throws IOException{
		String endpoint = "comment-*";
		Map<String, List<String>> parameterMap =new HashMap<String, List<String>>();
		parameterMap.put("id",commentIds);
		String query=Utils.queryString(parameterMap);
		String queryString = "{\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  }\n" +  
				"}";
		elasticRepository.delteteES(endpoint,queryString,null);
	}

	@Override
	public List<String> commentIdsContained(List<String> commentIds) throws IOException{
		List<String> list=new LinkedList<String>();
		if(commentIds==null||commentIds.size()==0){
			return list;
		}
		String endpoint = "comment-*";
		Map<String, List<String>> parameterMap =new HashMap<String, List<String>>();
		parameterMap.put("id",commentIds);
		String query=Utils.queryString(parameterMap);
		String queryString = "{\n" + 
				"  \"size\": 10000,\n" +
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + ("".equals(query)?"":query)+
				"      ],\n" + 
				"      \"must_not\": []\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"_source\": \n" + 
				"    \"id\" \n" + 
				"  \n" +  
				"}";
		String commentResult=elasticRepository.queryES("GET", endpoint, queryString);
		JSONArray esJson=JSON.parseObject(commentResult).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++){
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			list.add(element.getString("id"));
		}
		return list;
	}
	
	public static long DateCompare(String s1,String s2){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d1= sdf.parse(s1);
			Date d2 = sdf.parse(s2);
			return ((d1.getTime() - d2.getTime())/(24*3600*1000));
			}catch(ParseException e){
				e.printStackTrace();
			}
		return 0;
	}

	
}