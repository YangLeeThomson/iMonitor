package com.ronglian.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.repository.SpreadTrackRepository;
import com.ronglian.service.SpreadTrackService;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月28日 下午9:10:00
* @description:描述
*/
@Service("spreadTrackService")
public class SpreadTrackServiceImpl implements SpreadTrackService {

	@Autowired
	private SpreadTrackRepository spreadTrackRepository;
	
	@Override
	public JSONObject spreadTrack(String unionId) throws IOException {
		// TODO Auto-generated method stub
		String articleJSONString = spreadTrackRepository.getArticle(unionId);
		JSONObject articleJSONObject = JSONObject.parseObject(articleJSONString);
		JSONArray articleJSONArray = (articleJSONObject.getJSONObject("hits")).getJSONArray("hits");
		JSONObject article = null;
		if(!articleJSONArray.isEmpty()) {
			article = articleJSONArray.getJSONObject(0);
		}
		JSONObject articleSource = article.getJSONObject("_source");
		String originSource = articleSource.getString("source");
		String platformName = articleSource.getString("platformName");
		
		JSONObject resultObject = new JSONObject();
		JSONArray array01 = new JSONArray();
		JSONObject su = new JSONObject();
		su.put("name",platformName);
//		if("原创".equals(originSource)) {
//		}else {
//			su.put("name",originSource);
//		}
		array01.add(su);
		JSONObject bao = new JSONObject();
		bao.put("name","爆发点");
		array01.add(bao);
		JSONObject chuan = new JSONObject();
		chuan.put("name","传播点");
		array01.add(chuan);
		resultObject.put("array01",array01);
		
		
		String transArticlesResult = spreadTrackRepository.getTransArticles(unionId);
		JSONObject transArticlesResultObject = JSONObject.parseObject(transArticlesResult);
		JSONObject transArticlesResultHitsObject = transArticlesResultObject.getJSONObject("hits");
		JSONArray transArticlesResultHitsArray = transArticlesResultHitsObject.getJSONArray("hits");
		if(!transArticlesResultHitsArray.isEmpty()) {
			
			//用于统计媒体出现的次数
			JSONObject analysis = new JSONObject();//发布来源
			JSONObject analysis2 = new JSONObject();//采集来源
			
			List<String> relationNodeDistinctList = new ArrayList<String>();
			JSONArray array02 = new JSONArray();
			if(!"原创".equals(originSource)) {
				JSONObject suObject = new JSONObject();
				suObject.put("source",originSource);
				suObject.put("target", platformName);
				array02.add(suObject);
				relationNodeDistinctList.add(originSource+platformName);
			}
			for(int i=0;i<transArticlesResultHitsArray.size();i++) {
				JSONObject _source = transArticlesResultHitsArray.getJSONObject(i).getJSONObject("_source");
				String reportSource = _source.getString("reportSource");
				String crawlSource = _source.getString("crawlSource");
				if(!StringUtils.isEmpty(reportSource) && !StringUtils.isEmpty(crawlSource)) {
					reportSource = reportSource.replace(" ","");
					crawlSource = crawlSource.replace(" ","");
					if(reportSource.equals(crawlSource)) {
						continue;
					}
					String distinctKey = reportSource.trim()+crawlSource.trim();
					if(relationNodeDistinctList.contains(distinctKey)) {
						continue;
					}
					relationNodeDistinctList.add(distinctKey);
					System.out.println("-----------------distinctkey:"+distinctKey+"------------------");
					JSONObject relationNode = new JSONObject();
					relationNode.put("source",reportSource);
					relationNode.put("target",crawlSource);
					array02.add(relationNode);
				}
				
				if(analysis.containsKey(reportSource)) {
					int count = analysis.getIntValue(reportSource)+1;
					analysis.put(reportSource,count);
				}else {
					analysis.put(reportSource,1);
				}
				if(analysis2.containsKey(crawlSource)) {
					int count = analysis2.getIntValue(crawlSource)+1;
					analysis2.put(crawlSource,count);
				}else {
					analysis2.put(crawlSource,1);
				}
			}
			analysis.remove(null);
			analysis.remove("");
			analysis2.remove(null);
			analysis2.remove("");
			resultObject.put("array02",array02);
			
			JSONArray array03 = new JSONArray();
			JSONObject suzhou = new JSONObject();
			suzhou.put("name", platformName);
			suzhou.put("category", platformName);
			array03.add(suzhou);
			
			List<String> distinctList = new ArrayList<String>();
			for(String source:analysis.keySet()) {
				if(StringUtils.isEmpty(source)) {
					continue;
				}
				JSONObject trackNode = new JSONObject();
				trackNode.put("name",source);
				if(analysis.getIntValue(source)>=4) {
					trackNode.put("category","爆发点");
				}else {
					trackNode.put("category","传播点");
				}
				array03.add(trackNode);
				distinctList.add(source);
			}
			for(String source:analysis2.keySet()) {
				if(StringUtils.isEmpty(source)) {
					continue;
				}
				if(distinctList.contains(source)) {
					continue;
				}
				JSONObject trackNode = new JSONObject();
				trackNode.put("name",source);
				trackNode.put("category","传播点");
				array03.add(trackNode);
			}
			resultObject.put("array03",array03);
		}
		return resultObject;
	}
//	@Override
//	public JSONObject spreadTrack(String unionId) throws IOException {
//		// TODO Auto-generated method stub
//		String result = spreadTrackRepository.getArticle(unionId);
//		JSONObject resultObject = JSONObject.parseObject(result);
//		JSONObject articleHitsObject = resultObject.getJSONObject("hits");
//		JSONArray articleHitsArray = articleHitsObject.getJSONArray("hits");
//		if(!articleHitsArray.isEmpty()) {
//			JSONObject articleObjct = articleHitsArray.getJSONObject(0);
//			int isOrigin = articleObjct.getJSONObject("_source").getIntValue("isOrigin");
//			if(isOrigin == 0) {
//				//非原创
//			}else {
//				//原创
//				
//			}
//			String transArticlesResult = spreadTrackRepository.getTransArticles(unionId);
//			JSONObject transArticlesResultObject = JSONObject.parseObject(transArticlesResult);
//			JSONObject transArticlesResultHitsObject = transArticlesResultObject.getJSONObject("hits");
//			JSONArray transArticlesResultHitsArray = transArticlesResultHitsObject.getJSONArray("hits");
//			if(!transArticlesResultHitsArray.isEmpty()) {
//				JSONArray array = new JSONArray();
//				for(int i=0;i<transArticlesResultHitsArray.size();i++) {
//					JSONObject _source = transArticlesResultHitsArray.getJSONObject(i);
//					String reportSource = _source.getString("reportSource");
//					String crawlSource = _source.getString("crawlSource");
//					JSONObject trackNode = new JSONObject();
//					trackNode.put("source",reportSource);
//					trackNode.put("target",crawlSource);
//					array.add(trackNode);
//				}
//			}
//		}
//		
//		
//		return null;
//	}

}
