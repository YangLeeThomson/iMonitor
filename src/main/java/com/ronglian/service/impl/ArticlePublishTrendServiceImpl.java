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
import com.ronglian.repository.SpreadTrendAnalysisRepository;
import com.ronglian.service.ArticlePublishTrendService;
import com.ronglian.service.CustomGroupService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午8:10:19
* @description:描述
*/
@Slf4j
@Service("articlePublishTrendService")
public class ArticlePublishTrendServiceImpl implements ArticlePublishTrendService {
	
	@Autowired
	private SpreadTrendAnalysisRepository spreadTrendAnalysisRepository;

	@Autowired
	private CustomGroupService customGroupService;
	
	@Override
	public List<HashMap<String, String>> articlePublishTrend(int accountType, String platformTypeId, String platformId,
			String groupId, Date bTime, Date eTime) throws IOException {
		// TODO Auto-generated method stub
		List<String> platformIdList = null;
		if(!StringUtils.isEmpty(platformId)) {
			platformIdList = Arrays.asList(platformId);
		}
		if(!StringUtils.isEmpty(groupId)) {
			platformIdList =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		String jsonString = null;
		if(accountType == 0) {
			jsonString = spreadTrendAnalysisRepository.articlePublishTrendDay(accountType, platformTypeId, platformIdList, bTime, eTime);
		}else {
			jsonString = spreadTrendAnalysisRepository.articlePublishTrend(accountType, platformTypeId, platformIdList, bTime, eTime);
		}
		log.info("作品发布时间趋势："+jsonString);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		JSONObject aggregations = jsonObject.getJSONObject("aggregations");
		JSONObject mediaType = aggregations.getJSONObject("publishTime");
		JSONArray buckets = mediaType.getJSONArray("buckets");
		List<HashMap<String,String>> return_list = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<buckets.size();i++) {
			JSONObject bucket = (JSONObject)buckets.get(i);
			HashMap<String, String> return_map = new HashMap<String,String>();
			return_map.put("name",bucket.getString("key_as_string"));
			return_map.put("value",bucket.getIntValue("doc_count")+"");
			return_list.add(return_map);
		}
		return return_list;
	}

}
