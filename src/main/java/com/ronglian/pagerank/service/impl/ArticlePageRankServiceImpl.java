package com.ronglian.pagerank.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ronglian.model.TransInfo;
import com.ronglian.pagerank.mapper.ArticlePageRankMapper;
import com.ronglian.pagerank.model.ArticlePageRank;
import com.ronglian.pagerank.service.ArticlePageRankService;
import com.ronglian.repository.CopyrightMonitorRepository;
import com.ronglian.repository.TransInfoRepository;

import com.ronglian.utils.HttpUtil;
import com.ronglian.model.Article;

@Service("articlePageRankService")
public class ArticlePageRankServiceImpl implements ArticlePageRankService {
	@Autowired
	private ArticlePageRankMapper articlePageRankMapper;
	@Autowired
	private CopyrightMonitorRepository copyrightMonitorRepository;
	@Autowired
	private TransInfoRepository transInfoRepository;
	
	//@Value("${imoniter.interface.pagerank.nlp}")
	private String tonlpUrl;
	
	@Override
	public void saveArticlePageRank(ArticlePageRank articlePageRank){
		articlePageRankMapper.add(articlePageRank);
	}
	
	@Override
	public void deleteArticlePageRank(String id){
		articlePageRankMapper.delete(id);
	}
	
	@Override
	public Map<String,Object> getDetonation(String unionId) throws IOException{
		Map<String,Object> result= new HashMap<String,Object>();
		String esRst=copyrightMonitorRepository.getOriginalArticle(unionId);
		JSONArray esArray=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		if(esArray.size()<1) {
			return null;
		}
		JSONObject esJson=esArray.getJSONObject(0).getJSONObject("_source");
		Article origin =new Article();
		origin.setUnionId(esJson.getString("unionId"));
		origin.setPlatformName(esJson.getString("platformName"));
		origin.setPlatformId(esJson.getString("platformId"));
		origin.setPlatformTypeId(esJson.getString("platformTypeId"));
		origin.setPlatformTypeName(esJson.getString("platformTypeName"));
		origin.setTitle(esJson.getString("title"));
		origin.setUrl(esJson.getString("url"));
		origin.setPublishTime(esJson.getDate("publishTime"));
		origin.setContentNum(esJson.getString("content").length());
		origin.setArticleId(esJson.getString("articleId"));
		origin.setContent(esJson.getString("content"));
		result.put("center",origin);
		result.put("linkedList",  articlePageRankMapper.findByUnionId(unionId));
		return result;
	}

	@Override
	public List<TransInfo> findTransInfoByUnionId(List<String> unionIds) throws IOException{
		return transInfoRepository.getTransInfoByUnionIds(unionIds);
	}

	@Override
	public String receiveArticlePageRank(ArticlePageRank articlePageRank) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		String reqJson = mapper.writeValueAsString(articlePageRank);
		
		String numRst = HttpUtil.post(tonlpUrl, reqJson);
		return numRst;
		
	}
	
}