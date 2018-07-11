package com.ronglian.job;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.common.Constants;
import com.ronglian.mapper.LogMapper;
import com.ronglian.model.Comment;
import com.ronglian.model.Log;
import com.ronglian.repository.CommentAnalysisRepository;
import com.ronglian.repository.CommentRepository;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.MD5Util;
import com.ronglian.utils.Utils;

@Slf4j
@Component
@DisallowConcurrentExecution
public class CommentJob implements Job{
	
	@Value("${imoniter.interface.nlp.commentanalysis}")
	private String commentAnalysisUrl;
	
	@Value("${imoniter.interface.comment.suzhou.url}")
	private String suzhouUrl;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentAnalysisRepository commentAnalysisRepository;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	ElasticRepository elasticRepository;
	
	@Autowired
	private LogMapper logMapper;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		try{
			saveSuzhouComments();
			getCommentAnalysis();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void getCommentAnalysis() throws IOException{
		List<String> dateList = Utils.getDateList(null,Constants.MONITOR_DAY_LENGTH,Constants.DEFAULT_DATE_FORMAT_YMD);
		for (String publishDate : dateList) {
			try {
			Set<Object> articles = redisTemplate.opsForHash().keys(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE);
			Iterator<Object> it = articles.iterator();
			//int count = 0;
			while (it.hasNext()) {
				try {
					List<String> unionIds=new LinkedList<String>();
					String unionId = (String) it.next();
					/*String hashValue=(String) redisTemplate.opsForHash().get(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE, unionId);
					JSONObject article=JSONObject.parseObject(hashValue);*/
					unionIds.add(unionId);
					/*count++;
					it.remove();
					if (count < 5 && articles.size() > 0) {
						continue;
					}*/
					//int tryTimes=0;
					List<Object> list=null;
					List<String> commentContent=new LinkedList<String>();
					Map<String,List<String>> commentMap=new HashMap<String,List<String>>();
					Map<String,String> taskid=new HashMap<String,String>();
					ObjectMapper mapper = new ObjectMapper();
					String reqJson = null;
					String numRst = null;
					Map<String, String> result = null;
					List<Comment> commentList=commentRepository.getCommentByUnionIds(unionIds);
					for(Comment comment:commentList){
						commentContent.add(comment.getContent());
					}
					commentMap.put(unionId,commentContent);
					for (Map.Entry<String,List<String> > entry : commentMap.entrySet()) {
						list = new LinkedList<Object>();
						taskid.put("taskid",entry.getKey());
						list.add(entry.getValue());
						list.add(taskid);
						reqJson = mapper.writeValueAsString(list);
						System.out.println(reqJson);
						
						String time = String.valueOf(System.currentTimeMillis() / 1000);
						String suffixedTime = time + "HI8i921&";
						String md5Time = MD5Util.encodeByMD5(suffixedTime);
						String key = md5Time.substring(0, 8);
						String url = commentAnalysisUrl + "?time=" + time + "&key=" + key;
						numRst = HttpUtil.post(url, reqJson);
						result = (Map<String, String>)mapper.readValue(numRst, Map.class);
						/*if(result==null||!result.get("code").equals("200")){
							while(tryTimes<3){
								numRst = HttpUtil.post(url, reqJson);
								result = (Map<String, String>)mapper.readValue(numRst, Map.class);
								System.out.println(result);
								tryTimes++;
								if(result!=null&&result.get("code").equals("200")){
									continue;
								}
							}
							if(tryTimes>=3){
								logMapper.add(new Log(1,commentAnalysisUrl,"发送给nlp评论分析失败",entry.getKey(),"code"+result.get("code")+";msg:"+result.get("msg")));
								log.info(numRst);
							}
						}*/
					}
					Thread.sleep(50);// 隔3s访问一次才会有结果
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} 
		} catch (Exception e) {
			log.error("CommentJob 异常", e);
			continue;
		}
		}		
	}
	
	public void saveSuzhouComments() {
		List<String> dateList = Utils.getDateList(null,Constants.MONITOR_DAY_LENGTH,Constants.DEFAULT_DATE_FORMAT_YMD);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date ();
		for (String publishDate : dateList) {
			try {
			Set<Object> articles = redisTemplate.opsForHash().keys(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE);
			Iterator<Object> it = articles.iterator();
			int count = 0;
			List<Map<String, Object>> articleList = new ArrayList<Map<String, Object>>();
			while (it.hasNext()) {
				try {
					Map<String, Object> requestBody = new HashMap<String, Object>();
					Map<String, Object> oneData = new HashMap<String,Object>();
					String unionId = (String) it.next();
					String hashValue=(String) redisTemplate.opsForHash().get(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE, unionId);
					JSONObject article=JSONObject.parseObject(hashValue);
					oneData.put("origin_app", article.get("originApp"));
					oneData.put("media_id",  article.get("articleId"));
					articleList.add(oneData);
					count++;
					it.remove();
					if (count < 50 && articles.size() > 0) {
						continue;
					}
					requestBody.put("count", count);
					requestBody.put("data", articleList);
					String time = String.valueOf(System.currentTimeMillis() / 1000);
					String suffixedTime = time + "HI8i921&";
					String md5Time = MD5Util.encodeByMD5(suffixedTime);
					String key = md5Time.substring(0, 8);
					String url = suzhouUrl + "?time=" + time + "&key=" + key;
					ObjectMapper mapper = new ObjectMapper();
					String reqJson = mapper.writeValueAsString(requestBody);
					System.out.println(reqJson);
					String numRst = HttpUtil.post(url, reqJson);
					System.out.println(numRst);
					if (StringUtils.isNotEmpty(numRst)) {
						JSONObject relust = JSON.parseObject(numRst);
						JSONArray rstList =relust.getJSONArray("data");
						log.info("--调用评论接口--本次提交文章数：{},返回的评论数{}", articleList.size(), rstList.size());
						count = 0;
						Map<String,Comment> commentMap = new HashMap<String,Comment>();
						List<Comment> commentList =new LinkedList<Comment>();
						articleList = new ArrayList<Map<String, Object>>();
						if (rstList.size() < 1) {
							continue;
						}
						for (int i = 0; i < rstList.size(); i++) {
								JSONObject numJson = rstList.getJSONObject(i);
								String originApp= numJson.getString("origin_app");
								String mediaId=numJson.getString("media_id");
								JSONArray commentArray = numJson.getJSONArray("comments");
								JSONObject commentJson = null;
								if(commentArray!=null&&commentArray.size()>0){
								for(int j=0;j<commentArray.size();j++){
									commentJson=commentArray.getJSONObject(j);
									commentMap.put(commentJson.getString("comment_id"), new Comment(commentJson.getString("comment_id"),
											commentJson.getString("nick_name"),StringUtils.isEmpty(commentJson.getString("c_time"))?date:sdf.parse(commentJson.getString("c_time")),
													originApp+"_"+mediaId,commentJson.getString("content")));
								}
								}
						}
						List<String> commentIds = new ArrayList<>(commentMap.keySet());
						List<String> containedCommentIds = new LinkedList<String>();
						int i=1;
						if(commentIds!=null&&commentIds.size()>0){
						for(;i<commentIds.size()/50;i++){
							containedCommentIds.addAll(commentRepository.commentIdsContained(commentIds.subList((i-1)*50,(i*50)-1)));
						}
						if(((i-1)*50)!=commentIds.size()){
							containedCommentIds.addAll(commentRepository.commentIdsContained(commentIds.subList((i-1)*50,commentIds.size()-1)));
						}
						}
						if(containedCommentIds!=null&&containedCommentIds.size()>0){
							for(Map.Entry<String,Comment> entry:commentMap.entrySet()){
								if(containedCommentIds.contains(entry.getKey())){
									continue;
								}else{
									commentList.add(entry.getValue());
								}
							}
						}else{
							for(Map.Entry<String,Comment> entry:commentMap.entrySet()){
									commentList.add(entry.getValue());
							}
						}
						commentRepository.saveComment(commentList);
					}
					Thread.sleep(1000 * 20);// 隔3s访问一次才会有结果
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} 
		} catch (Exception e) {
			log.error("CommentJob 异常", e);
			continue;
		}
		}
	}
}