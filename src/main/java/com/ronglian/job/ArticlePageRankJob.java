package com.ronglian.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronglian.common.Constants;
import com.ronglian.model.TransInfo;
import com.ronglian.pagerank.mapper.ArticlePageRankMapper;
import com.ronglian.pagerank.model.ArticlePageRank;
import com.ronglian.pagerank.service.ArticlePageRankService;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.Utils;

import java.math.BigDecimal;

/**
 * 传播力相关的定时任务，暂不用。
 * @author liuhanbo
 *
 */

@Slf4j
@Component
@DisallowConcurrentExecution
public class ArticlePageRankJob implements Job{
	
	@Autowired
	private ArticlePageRankService articlePageRankService;
	
	@Value("${imoniter.interface.pagerank.url}")
	private String pageRankUrl;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private ArticlePageRankMapper articlePageRankMapper;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		try{
			updateArticlePageRank();
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void updateArticlePageRank() throws IOException, InterruptedException{
		List<String> dateList = Utils.getDateList(null,Constants.MONITOR_DAY_LENGTH,Constants.DEFAULT_DATE_FORMAT_YMD);
		List<String> unionIds =new LinkedList<String>();
		ObjectMapper mapper = new ObjectMapper();
		List<TransInfo> transInfos=null;
		int count=0;
		for (String publishDate : dateList) {
			Set<Object> articles = redisTemplate.opsForHash().keys(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE);
			Iterator<Object> it = articles.iterator();
			while (it.hasNext()) {
				count++;
				unionIds.add((String)it.next());
				it.remove();
				if (count < 50 && unionIds.size() > 0) {
					continue;
				}
				transInfos=articlePageRankService.findTransInfoByUnionId(unionIds);
				Iterator<TransInfo> tansInfoIt = transInfos.iterator();
				Map<String,ArticlePageRank> pageRankMap = new HashMap<String,ArticlePageRank>();
				List<Map<String,Object>> data=new LinkedList<Map<String,Object>>();	
				Map<String, Object> requestBody=new HashMap<String, Object>();
				//int count=0;
				while(tansInfoIt.hasNext()){
					Map<String,Object> oneData=new HashMap<String,Object>();
					TransInfo oneTrans =tansInfoIt.next();
					pageRankMap.put(oneTrans.getWebpageUrl(),new ArticlePageRank(oneTrans.getWebpageCode(),oneTrans.getUnionId(),
							oneTrans.getArticleId(),oneTrans.getPlatformTypeId(),
							null,BigDecimal.valueOf(oneTrans.getTransSimilarity()),
							oneTrans.getCreateTime(),oneTrans.getWebpageUrl(),
							oneTrans.getTitle(),oneTrans.getMediaType(),
							oneTrans.getReportSource()));
					oneData.put("url",oneTrans.getWebpageUrl());
					oneData.put("publishTime",oneTrans.getReportTime());
					data.add(oneData);
					tansInfoIt.remove();
					requestBody.put("info", "imonitor");
					requestBody.put("data", data);
					String reqJson = mapper.writeValueAsString(requestBody);
					
					/*String time = String.valueOf(System.currentTimeMillis() / 1000);
					String suffixedTime = time + "HI8i921&";
					String md5Time = MD5Util.encodeByMD5(suffixedTime);
					String key = md5Time.substring(0, 8);
					String url = pageRankUrl + "?time=" + time + "&key=" + key;*/
					
					String numRst = HttpUtil.post(pageRankUrl, reqJson);
					if (StringUtils.isNotEmpty(numRst)) {
						JSONObject relust = JSON.parseObject(numRst);
						if(relust.getInteger("code")!=200){
							log.info("--调用传播力接口失败--request:{},response:{}", reqJson, numRst);
							continue;
						}
						JSONArray rstList =relust.getJSONArray("data");
						for (int i = 0; i < rstList.size(); i++) {
							JSONObject numJson = rstList.getJSONObject(i);
							ArticlePageRank articlePageRank=pageRankMap.get(numJson.getString("url"));
							articlePageRank.setPageRank(numJson.get("pageRank")!=null? new BigDecimal(numJson.get("pageRank").toString()): null);
							articlePageRankMapper.delete(articlePageRank.getId());
							articlePageRankMapper.add(articlePageRank);
						}
					}
					Thread.sleep(1000 * 10);
				}
			}
		}
		
		
	}
}