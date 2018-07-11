package com.ronglian.repository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.Comment;
import com.ronglian.model.CommentDistribute;

public interface CommentRepository{
	public void saveComment(List<Comment> commentList) throws JsonProcessingException;
	public List<Comment> getCommentByUnionIds(List<String> unionIds) throws IOException;
	public PageResult<Comment> findPageList(String orderField,String unionId,int pageNo,int pageSize) throws IOException;
	public List<CommentDistribute> getCommentDistribute(Integer queryType,String unionId) throws IOException;
	public Map<String,Object> getCommentAnalysis(Integer queryType,String unionId);
	public void deleteCommentByIds(List<String> commentIds) throws IOException;
	public List<String> commentIdsContained(List<String> commentIds) throws IOException;
}
