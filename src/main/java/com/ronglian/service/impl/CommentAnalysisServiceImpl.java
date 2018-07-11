package com.ronglian.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.Comment;
import com.ronglian.model.CommentAnalysis;
import com.ronglian.model.CommentDistribute;
import com.ronglian.service.CommentAnalysisService;
import com.ronglian.repository.CommentAnalysisRepository;
import com.ronglian.repository.CommentRepository;
import com.ronglian.repository.ElasticRepository;

@Slf4j
@Service("commentAnalysisService")
public class CommentAnalysisServiceImpl implements CommentAnalysisService {
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
    private RedisTemplate<Object,Object> redisTemplate;
	
	@Autowired
	private CommentAnalysisRepository commentAnalysisRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ElasticRepository elasticRepository;
	/**
	 * 评论分页查询
	 * 
	 * orderType:1按时间排序 2按热度排序
	 * @throws IOException 
	 * */
	@Override
	public PageResult<Comment> findPageList(String orderField,String articleId,int pageNo,int pageSize) throws IOException{
	return commentRepository.findPageList(orderField,articleId,pageNo,pageSize);
	}

	/**
	 * 评论分布查询
	 * queryType:1查询地域分布 2查询时间分布
	 * @throws IOException 
	 * */
	@Override
	public List<CommentDistribute> getCommentDistribute(Integer queryType,String articleId) throws IOException{
		return commentRepository.getCommentDistribute(queryType,articleId);
	}
	
	/**
	 * 评论分析查询
	 * queryType:1情感分析 2命名实体分析 3典型意见分析 4词云分析
	 * @throws IOException 
	 * */
	@Override
	public List<CommentAnalysis> getCommentAnalysis(List<String> queryType,String articleId) throws IOException{
		return commentAnalysisRepository.findCommentAnalysis(queryType,articleId);
	}

	@Override
	public void recieveComment(List<Comment> commentList) throws IOException{
		List<String> commentIds=new LinkedList<String>();
		for(Comment comment:commentList){
			commentIds.add(comment.getId());
		}
		commentRepository.deleteCommentByIds(commentIds);
		commentRepository.saveComment(commentList);
	}

	@Override
	public void recieveCommentAnalysis(Map<String,Object> requestMap) throws ParseException, IOException{
		List<Map<String,Object>> opinionResultList=(List<Map<String,Object>>)((Map)requestMap.get("opinions")).get("opinionResultList");
		List<CommentAnalysis> commentAnalysisList=new LinkedList<CommentAnalysis>();
		NumberFormat nf=NumberFormat.getPercentInstance();
		commentAnalysisRepository.deleteAnalysisByArticleId((String)requestMap.get("taskid"));
		if(opinionResultList!=null&&opinionResultList.size()>0){
			for(Map<String,Object> opinionResult:opinionResultList){
				commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),4,
						(Double)opinionResult.get("proportion"),new Date(),(String)opinionResult.get("opinion")
						,(Integer)opinionResult.get("num")));
			}
		}
		Map<String,Integer> sentiments=(Map)requestMap.get("sentimentStatistics");
		for(Map.Entry<String,Integer> sentiment:sentiments.entrySet()){
			if("positive".equals(sentiment.getKey())){
				commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),5,
						null,new Date(),"正面指数"
						,sentiment.getValue()));
			}else if("neutral".equals(sentiment.getKey())){
				commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),5,
						null,new Date(),"中性指数"
						,sentiment.getValue()));
			}else if("negative".equals(sentiment.getKey())){
				commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),5,
						null,new Date(),"负面指数"
						,sentiment.getValue()));
			}
			
		}
		Map<String,List> wordsClouds=(Map<String,List>)((Map)requestMap.get("wordsCloud")).get("cloudsofwords");
		Map<String,List> tags=(Map<String,List>)((Map)requestMap.get("wordsCloud")).get("tags");
		List<List<Object>> wordList =null;
		List<List<Object>> tagList=null;
		for(Map.Entry<String,List> wordsCloud:wordsClouds.entrySet()){ 
			if(wordsCloud.getKey().equals("ner")&&wordsCloud.getValue().size()>0){
				wordList=(List<List<Object>>)wordsCloud.getValue();
				for(List<Object> word:wordList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),1,(Double)word.get(2),
							new Date(),(String)word.get(0),(Integer)word.get(1)));
				}
			}
			if(wordsCloud.getKey().equals("noun")&&wordsCloud.getValue().size()>0){
				wordList=(List<List<Object>>)wordsCloud.getValue();
				for(List<Object> word:wordList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),2,(Double)word.get(2),
							new Date(),(String)word.get(0),(Integer)word.get(1)));
				}
			}
			if(wordsCloud.getKey().equals("adj")&&wordsCloud.getValue().size()>0){
				wordList=(List<List<Object>>)wordsCloud.getValue();
				for(List<Object> word:wordList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),3,(Double)word.get(2),
							new Date(),(String)word.get(0),(Integer)word.get(1)));
				}
			}
		}
		for(Map.Entry<String,List> tag:tags.entrySet()){
			if(tag.getKey().equals("org")&&tag.getValue().size()>0){
				tagList=(List<List<Object>>)tag.getValue();
				for(List<Object> oneTag:tagList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),6,null,
							new Date(),(String)oneTag.get(0),(Integer)oneTag.get(1)));
				}
			}
			if(tag.getKey().equals("location")&&tag.getValue().size()>0){
				tagList=(List<List<Object>>)tag.getValue();
				for(List<Object> oneTag:tagList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),7,null,
							new Date(),(String)oneTag.get(0),(Integer)oneTag.get(1)));
				}
			}
			if(tag.getKey().equals("name")&&tag.getValue().size()>0){
				tagList=(List<List<Object>>)tag.getValue();
				for(List<Object> oneTag:tagList){
					commentAnalysisList.add(new CommentAnalysis((String)requestMap.get("taskid"),8,null,
							new Date(),(String)oneTag.get(0),(Integer)oneTag.get(1)));
				}
			}
		}
		commentAnalysisRepository.saveCommentAnalysis(commentAnalysisList);
	}
	
}