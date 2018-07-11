package com.ronglian.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.PageResult;
import com.ronglian.model.Article;
import com.ronglian.model.TransArticle;
import com.ronglian.repository.CopyrightMonitorRepository;
import com.ronglian.service.CopyrightMonitorService;
import com.ronglian.utils.Utils;

@Service(value = "copyrightMonitorService")
public class CopyrightMonitorServiceImpl implements CopyrightMonitorService {
	
	@Autowired
	CopyrightMonitorRepository copyrightMonitorRepository;

	@Override
	public Article getOriginalArticle(String unionId) {
		String esRst=null;
		try {
			esRst=copyrightMonitorRepository.getOriginalArticle(unionId);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		origin.setIsOrigin(esJson.getIntValue("isOrigin"));
		origin.setClickNum(esJson.getIntValue("clickNum"));
		origin.setCommentNum(esJson.getIntValue("commentNum"));
		origin.setThumbsNum(esJson.getIntValue("thumbsNum"));
		origin.setAwardNum(esJson.getIntValue("awardNum"));
		origin.setShareNum(esJson.getIntValue("shareNum"));
		origin.setSubscribeNum(esJson.getIntValue("subscribeNum"));
		origin.setClassification(esJson.getIntValue("classification"));
		
		return origin;
	}

	@Override
	public TransArticle getTransArticle(String webpageCode) {
		String esRst=null;
		try {
			esRst=copyrightMonitorRepository.getTransArticle(webpageCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject esJson=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		TransArticle trans=new TransArticle();
		trans.setWebpageCode(esJson.getString("webpageCode"));
		trans.setMediaName(esJson.getString("crawlSource"));
		trans.setTransTitle(esJson.getString("title"));
		trans.setTransUrl(esJson.getString("webpageUrl"));
		trans.setTransPublishTime(esJson.getDate("reportTime"));
		trans.setTransContent(esJson.getString("content"));
		trans.setTransContentNum(trans.getTransContent().length());
		trans.setTransAnnotationSource(esJson.getString("reportSource"));
		trans.setTransSimilarity(esJson.getDoubleValue("transSimilarity"));
		trans.setTransEsScore(esJson.getDoubleValue("transEsScore"));
		trans.setIsTort(esJson.getIntValue("isTort"));
		trans.setTransPictureUrl(esJson.getString("pictureUrl"));
		trans.setTransPictureTime(esJson.getDate("transPictureTime"));
		return trans;
	}

	@Override
	public JSONArray getTitleList(String titleWord,int page,int pageSize) {
		// TODO Auto-generated method stub
		JSONArray result=new JSONArray();
		String esRst=null;
		try {
			esRst=copyrightMonitorRepository.getTitleList(titleWord,page,pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray esJson=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++) {
			JSONObject title=new JSONObject();
			title.put("unionId", esJson.getJSONObject(i).getJSONObject("_source").getString("unionId"));
			title.put("title", esJson.getJSONObject(i).getJSONObject("_source").getString("title"));
			title.put("platformName", esJson.getJSONObject(i).getJSONObject("_source").getString("platformName"));
			title.put("articleId", esJson.getJSONObject(i).getJSONObject("_source").getString("articleId"));
			title.put("url", esJson.getJSONObject(i).getJSONObject("_source").getString("url"));
			result.add(title);
		}
		return result;
	}
	
	@Override
	public PageResult<JSONObject> getArticleList(String titleWord,int page,int pageSize) {
		// TODO Auto-generated method stub
		PageResult<JSONObject> pageResult = new PageResult<JSONObject>();
		
		String esRst=null;
		try {
			esRst=copyrightMonitorRepository.getTitleList(titleWord,page,pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray esJson=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		// 赋值分页对象
		pageResult.setNumber(page);
		int totalElement = JSON.parseObject(esRst).getJSONObject("hits").getIntValue("total");
		pageResult.setTotalElements(totalElement);
		pageResult.setTotalPages(Utils.totalPage(totalElement, pageSize));
		pageResult.setLimit(pageSize);
		List<JSONObject> content = new ArrayList<JSONObject>();
		for(int i=0;i<esJson.size();i++) {
			JSONObject title=esJson.getJSONObject(i).getJSONObject("_source");
			title.put("publishTime", Utils.dateToString(title.getDate("publishTime"), "yyyy-MM-dd HH:mm"));
			content.add(title);
		}
		pageResult.setContent(content);
		return pageResult;
	}

}
