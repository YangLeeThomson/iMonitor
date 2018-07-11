package com.ronglian.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.Constants;
import com.ronglian.repository.MediaTransAnalysisRepository;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.PlatformTransClassificationService;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午5:01:50
* @description:描述
*/
@Slf4j
@Service("platformTransClassificationService")
public class PlatformTransClassificationServiceImpl implements PlatformTransClassificationService {
	@Autowired
	private MediaTransAnalysisRepository mediaTransAnalysisRepository; 
	@Autowired
	private CustomGroupService customGroupService;
	
	@Override
	public List<HashMap<String, String>> transArticleClassificationProportion(int accountType, String platformTypeId,
			String platformId,String groupId, Date startTime, Date endTime) throws IOException {
		// TODO Auto-generated method stub
		List<String> platformIdList = null;
		if(!StringUtils.isEmpty(platformId)) {
			platformIdList = Arrays.asList(platformId);
		}
		if(!StringUtils.isEmpty(groupId)) {
			platformIdList =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		String jsonString = mediaTransAnalysisRepository.transArticleClassificationProportion(accountType, platformTypeId, platformIdList, startTime, endTime);
		log.info("转载分类占比："+jsonString);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		JSONObject aggregations = jsonObject.getJSONObject("aggregations");
		JSONObject mediaType = aggregations.getJSONObject("classification");
		JSONArray buckets = mediaType.getJSONArray("buckets");
		List<HashMap<String,String>> return_list = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<buckets.size();i++) {
			JSONObject bucket = (JSONObject)buckets.get(i);
			HashMap<String, String> return_map = new HashMap<String,String>();
			return_map.put("name",(String)Constants.CLASSIFICATION_CODE_NEW.get(bucket.getIntValue("key")));
			return_map.put("value",bucket.getIntValue("doc_count")+"");
			return_map.put("classification",bucket.getIntValue("key")+"");
			return_list.add(return_map);
		}
		log.info(return_list.toString());
		List<HashMap<String,String>> new_return_list = new ArrayList<HashMap<String,String>>();
		int inx = 1;
		int other_sum = 0;
		for(HashMap<String,String> hashMap:return_list) {
			if("其他".equals(hashMap.get("name"))) {
				other_sum += Integer.valueOf(hashMap.get("value").toString());
			}else {
				if(inx<=5) {
					new_return_list.add(hashMap);
				}else {
					other_sum += Integer.valueOf(hashMap.get("value").toString());
				}
			}
			inx += 1;
		}
		HashMap<String,String> other_map = new HashMap<>();
		other_map.put("name","综合");
		other_map.put("value",other_sum+"");
		other_map.put("classification",0+"");
		new_return_list.add(other_map);
		return new_return_list;
	}

	@Override
	public List<HashMap<String,String>> transArticleClassificationTop10(int classification,String other,int accountType, String platformTypeId,
			String platformId,String groupId, Date startTime, Date endTime, int pageNo, int pageSize) throws IOException {
		// TODO Auto-generated method stub
		List<String> platformIdList = null;
		if(!StringUtils.isEmpty(platformId)) {
			platformIdList = Arrays.asList(platformId);
		}
		if(!StringUtils.isEmpty(groupId)) {
			platformIdList =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		String jsonString = mediaTransAnalysisRepository.transArticleClassificationTop10(classification,other, accountType, platformTypeId, platformIdList, startTime, endTime, pageNo, pageSize);
		log.info("转载分类top10："+jsonString);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		log.info("PlatformTransClassificationService.transArticleClassificationTop10("+accountType+","+platformTypeId+","+platformIdList+","+platformId+","+groupId+","+startTime+","+endTime+","+pageNo+","+pageSize+") take time:"+jsonObject.getInteger("took"));
		JSONObject hits = jsonObject.getJSONObject("hits");
		List<HashMap<String,String>> return_list = new ArrayList<HashMap<String,String>>();
		if(hits != null) {
			JSONArray articles = hits.getJSONArray("hits");
			if(articles.size() > 0) {
				for(int i=0;i<articles.size();i++) {
					JSONObject _source = ((JSONObject) articles.get(i)).getJSONObject("_source");
					HashMap<String,String> return_map = new HashMap<String,String>();
					return_map.put("platform",_source.getString("platformName")+"_"+_source.getString("platformTypeName"));
					return_map.put("title",_source.getString("title"));
					return_map.put("articleId",_source.getString("articleId"));
					return_map.put("unionId",_source.getString("unionId"));
					if(_source.getDate("publishTime") != null) {
						return_map.put("publishTime",Utils.dateToString(_source.getDate("publishTime"),"yyyy-MM-dd HH:mm"));
					}else {
						return_map.put("publishTime","");
					}
					return_list.add(return_map);
				}
			}
		}
		return return_list;
	}

}
