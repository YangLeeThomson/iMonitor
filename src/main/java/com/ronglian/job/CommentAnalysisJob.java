package com.ronglian.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.common.Constants;
import com.ronglian.mapper.LogMapper;
import com.ronglian.model.Comment;
import com.ronglian.repository.CommentAnalysisRepository;
import com.ronglian.repository.CommentRepository;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.MD5Util;
import com.ronglian.utils.Utils;

@Slf4j
@Component
@DisallowConcurrentExecution
public class CommentAnalysisJob implements Job{
	
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
	public void execute(JobExecutionContext context) throws JobExecutionException{
		try{
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
			int count = 0;
			List<String> unionIds=new LinkedList<String>();
			while (it.hasNext()) {
				try {
					String unionId = (String) it.next();
					String hashValue=(String) redisTemplate.opsForHash().get(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE, unionId);
					JSONObject article=JSONObject.parseObject(hashValue);
					unionIds.add(article.get("originApp")+"_"+article.get("articleId"));
					count++;
					it.remove();
					if (count < 100 && articles.size() > 0) {
						continue;
					}
					
					//int tryTimes=0;
					List<Object> list=null;
					List<String> commentContent=null;
					Map<String,List<String>> commentMap=new HashMap<String,List<String>>();
					Map<String,String> taskid=new HashMap<String,String>();
					ObjectMapper mapper = new ObjectMapper();
					String reqJson = null;
					String numRst = null;
					Map<String, String> result = null;
					List<Comment> commentList=commentRepository.getCommentByUnionIds(unionIds);
					for(Comment comment:commentList){
						if(commentMap.containsKey(comment.getUnionId())){
							commentContent=commentMap.get(comment.getUnionId());
						}else{
							commentContent=new LinkedList<String>();
						}
						commentContent.add(comment.getContent());
						commentMap.put(comment.getUnionId(),commentContent);
					}
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
					Thread.sleep(1000 * 5);// 隔3s访问一次才会有结果
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