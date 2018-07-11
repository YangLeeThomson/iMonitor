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
import com.ronglian.repository.HomePageRepository;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.PlatformTransChannelService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午4:31:54
* @description:描述
*/
@Slf4j
@Service("platformTransChannelService")
public class PlatformTransChannelServiceImpl implements PlatformTransChannelService {
	@Autowired
	private HomePageRepository homePageRepository; 
	@Autowired
	private CustomGroupService customGroupService;
	
	@Override
	public List<HashMap<String,String>> transChannelProportion(int accountType,String platformTypeId,String platformId,String groupId,Date startTime,Date endTime)throws IOException{
		List<String> platformIdList = null;
		if(!StringUtils.isEmpty(platformId)) {
			platformIdList = Arrays.asList(platformId);
		}
		if(!StringUtils.isEmpty(groupId)) {
			platformIdList =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		String jsonString = homePageRepository.transChannelProportion(accountType, platformTypeId, platformIdList, startTime, endTime);
		log.info("转载渠道占比："+jsonString);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		JSONObject aggregations = jsonObject.getJSONObject("aggregations");
		JSONObject mediaType = aggregations.getJSONObject("channel");
		JSONArray buckets = mediaType.getJSONArray("buckets");
		List<HashMap<String,String>> return_list = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<buckets.size();i++) {
			JSONObject bucket = (JSONObject)buckets.get(i);
			HashMap<String, String> return_map = new HashMap<String,String>();
			return_map.put("name",(String)Constants.CHANNEL_CODE.get(bucket.getIntValue("key")));
			return_map.put("code",bucket.getIntValue("key")+"");
			return_map.put("value",bucket.getIntValue("doc_count")+"");
			return_list.add(return_map);
		}
		return return_list;
	}
}
