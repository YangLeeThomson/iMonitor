package com.ronglian.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.Comment;
import com.ronglian.model.CommentAnalysis;
import com.ronglian.model.CommentDistribute;

public interface CommentAnalysisService{
	public PageResult<Comment> findPageList(String orderField,String articleId,int pageNo,int pageSize) throws IOException;
	public List<CommentDistribute> getCommentDistribute(Integer queryType,String articleId) throws IOException;
	public List<CommentAnalysis> getCommentAnalysis(List<String> type,String articleId) throws IOException;
	public void recieveComment(List<Comment> commentList) throws JsonProcessingException, IOException;
	public void recieveCommentAnalysis(Map<String,Object> requestMap) throws ParseException, JsonProcessingException, IOException;
}