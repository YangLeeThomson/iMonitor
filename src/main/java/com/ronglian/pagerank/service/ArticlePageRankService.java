package com.ronglian.pagerank.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ronglian.model.TransInfo;
import com.ronglian.pagerank.model.ArticlePageRank;

public interface ArticlePageRankService{
	public void saveArticlePageRank(ArticlePageRank articlePageRank);
	public void deleteArticlePageRank(String id);
	public Map<String,Object> getDetonation(String unionId) throws IOException;
	public List<TransInfo> findTransInfoByUnionId(List<String> unionIds) throws IOException;
	public String receiveArticlePageRank(ArticlePageRank articlePageRank) throws IOException;
}
