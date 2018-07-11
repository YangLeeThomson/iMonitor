package com.ronglian.repository;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.model.CommentAnalysis;

public interface CommentAnalysisRepository{
	public void saveCommentAnalysis(List<CommentAnalysis> commentAnalysisList) throws JsonProcessingException;
	List<CommentAnalysis> findCommentAnalysis(List<String> type,String articleId) throws IOException;
	public void deleteAnalysisByArticleId(String articleId) throws IOException;
}
