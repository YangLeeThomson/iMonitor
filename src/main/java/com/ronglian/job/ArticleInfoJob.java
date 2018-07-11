package com.ronglian.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ronglian.mapper.ArticleInfoHourMapper;
import com.ronglian.mapper.PlatformMapper;
import com.ronglian.mapper.PlatformTypeMapper;
import com.ronglian.model.Article;
import com.ronglian.model.ArticleInfoHour;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.CopyrightMonitorService;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.MD5Util;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 获取文章点击数定时任务
 * @author sunqian
 * @date 2018年6月21日 下午3:07:25
 */


@Slf4j
@Component
@DisallowConcurrentExecution
public class ArticleInfoJob implements Job{
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	PlatformMapper platformMapper;
	
	@Autowired
	PlatformTypeMapper platformTypeMapper;
	
	@Autowired
	ArticleInfoHourMapper articleInfoHourMapper;
	
	@Autowired
	ElasticRepository elasticRepository;
	
	@Autowired
	CopyrightMonitorService copyrightMonitorService;

	@Value("${imoniter.interface.suzhou.url}")
	private String suzhouUrl;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		getClickNums();
	}

	public void getClickNums() {
		List<String> dateList = Utils.getDateList(null,Constants.MONITOR_DAY_LENGTH,Constants.DEFAULT_DATE_FORMAT_YMD);
		for (String publishDate : dateList) {
			try {
			Set<Object> articles = redisTemplate.opsForHash().keys(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE);
			Iterator<Object> it = articles.iterator();
			int count = 0;
			List<Map<String, Object>> articleList = new ArrayList<Map<String, Object>>();
			while (it.hasNext()) {
				try {
					Map<String, Object> requestBody = new HashMap<String, Object>();
					String unionId = (String) it.next();
					
//					OriginArticle originArticle=(OriginArticle) redisTemplate.opsForHash().get("iMonitor:originArticle:id", articleId);
//					long publishTime=originArticle.getPublic_time().getTime();
//					long nowTime=System.currentTimeMillis();
//					if(nowTime-publishTime > 14*24*60*60*1000) {
//						redisTemplate.opsForHash().delete("iMonitor:originArticle:id", articleId);
//						continue;
//					}
					String hashValue=(String) redisTemplate.opsForHash().get(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE, unionId);
					JSONObject article=JSONObject.parseObject(hashValue);
					
					article.put("origin_app", article.get("originApp"));
					article.put("media_id",  article.get("articleId"));
					articleList.add(article);
					count++;
					
					it.remove();
					if (count < 50 && articles.size() > 0) {
						continue;
					}
					requestBody.put("count", count);
					requestBody.put("data", articleList);
					
					String time = String.valueOf(System.currentTimeMillis() / 1000);
					String suffixedTime = time + "HI8i921&";
					String md5Time = MD5Util.encodeByMD5(suffixedTime);
					String key = md5Time.substring(0, 8);
					String url = suzhouUrl + "?time=" + time + "&key=" + key;
					
					ObjectMapper mapper = new ObjectMapper();
					String reqJson = mapper.writeValueAsString(requestBody);
					String numRst = HttpUtil.post(url, reqJson);
					if (StringUtils.isNotEmpty(numRst)) {
						JSONObject relust = JSON.parseObject(numRst);
						JSONArray rstList =relust.getJSONArray("data");
						log.info("--调用点击数接口--本次提交文章数：{},有结果的文章数{}", articleList.size(), rstList.size());
						count = 0;
						articleList = new ArrayList<Map<String, Object>>();
						if (rstList.size() < 1) {
							continue;
						}
						for (int i = 0; i < rstList.size(); i++) {
							try {
								JSONObject numJson = rstList.getJSONObject(i);
								String originApp= numJson.getString("origin_app");
								String mediaId=numJson.getString("media_id");
								// 从es查询转载数
								int transNum=getTransNum(originApp+"_"+mediaId);
								float comprehensive_num=numJson.getIntValue("click_nums")/1000f+numJson.getIntValue("click_nums")/100f+numJson.getIntValue("comment_nums")/10f; //综合数值计算
								String query = "{    " + 
										"    \"doc\" : {    " + 
										"        \"commentNum\":"+numJson.getIntValue("comment_nums")	+",    " + 
										"        \"clickNum\":"+numJson.getIntValue("click_nums")+",    " + 
										"        \"distinctUserClickNum\":"+numJson.getIntValue("distinct_user_click_nums")+",    " + 
										"        \"thumbsNum\":"+numJson.getIntValue("thumbs_nums")+",    " + 
										"        \"awardNum\":"+numJson.getIntValue("award_nums")+",    " + 
										"        \"shareNum\":"+numJson.getIntValue("share_nums")+",    " + 
										"        \"subscribeNum\":"+numJson.getIntValue("collect_nums")+",    " + 
										"        \"transNum\":"+transNum+",    " + 
										"        \"comprehensive\":"+comprehensive_num+"    " + 
										"    }    " + 
										"}";
								@SuppressWarnings("unused")
								String res=elasticRepository.updateES("POST","imonitor_article-"+publishDate.replace("-", ".")+"/imonitor/"+originApp+"_"+mediaId, query);
								
								JSONObject newNums=JSONObject.parseObject(query).getJSONObject("doc");
								Article art=copyrightMonitorService.getOriginalArticle(originApp+"_"+mediaId);
								String platformId=art.getPlatformId();
								String platformTypeId=art.getPlatformTypeId();
								addUpdateMysql(platformId,platformTypeId,newNums,originApp+"_"+mediaId,publishDate);
							}catch(IOException e) {
								e.printStackTrace();
								continue;
							}catch(Exception e) {
								e.printStackTrace();
								continue;
							}
							
						}
					}
					Thread.sleep(1000 * 1);// 隔3s访问一次才会有结果
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			} 
		} catch (Exception e) {
			log.error("ArticleInfoJob 异常", e);
			continue;
		}
		}
	}

	/**
	 * 
	 * @Description: 将最新的点击数数据按小时保存到mysql
	 * @param @param query es中的点击数json
	 * @param @param unionid     
	 * @return void    返回类型  
	 * @throws
	 */
	private void addUpdateMysql(String platformId,String platformTypeId,JSONObject newNums, String unionid,String createTime) {
		ArticleInfoHour articleInfoHour=articleInfoHourMapper.findByUnionIdAndCreateTime(unionid,Utils.dateToString(new Date(), Constants.DEFAULT_DATE_FORMAT_YMD));
		if(articleInfoHour==null) {
			articleInfoHour=new ArticleInfoHour();
			articleInfoHour.setUnion_id(unionid);
			articleInfoHour.setPlatform_id(platformId);
			articleInfoHour.setPlatform_type_id(platformTypeId);
			JSONObject toSave=new JSONObject();
			Calendar c = Calendar.getInstance(); // 获取当前年
			c.setTime(new Date());
			int hour = c.get(Calendar.HOUR_OF_DAY);
			toSave.put(hour+"", newNums);
			articleInfoHour.setJson_nums(toSave.toJSONString());
			
			articleInfoHourMapper.add(articleInfoHour);
		}else {
			JSONObject toSave=JSON.parseObject(articleInfoHour.getJson_nums());
			Calendar c = Calendar.getInstance(); // 获取当前年
			c.setTime(new Date());
			int hour = c.get(Calendar.HOUR_OF_DAY);
			toSave.put(hour+"", newNums);
			articleInfoHour.setJson_nums(toSave.toJSONString());
			
			articleInfoHourMapper.updateJsonNums(articleInfoHour);
		}
		
	}

	private int getTransNum(String unionId) {
		int transNum=0;
		String endpoint = Utils.endpointCreater("imonitor_article-",1, null, null);
		String queryString = "{ \r\n" + 
				"  \"query\": {\r\n" + 
				"	\"match\": {\r\n" + 
				"	  \"unionId\": {\r\n" + 
				"		\"query\": \""+unionId+"\",\r\n" + 
				"		\"type\": \"phrase\"\r\n" + 
				"	 }\r\n" + 
				"	}\r\n" + 
				"  }\r\n" + 
				"}";
//		log.info("queryString:"+queryString);
		 
		try {
			String rst= elasticRepository.queryES("GET", endpoint, queryString);
			JSONArray esArray=JSON.parseObject(rst).getJSONObject("hits").getJSONArray("hits");
			if(esArray.size()>0 && esArray.getJSONObject(0).getJSONObject("_source")!=null)
				transNum=esArray.getJSONObject(0).getJSONObject("_source").getIntValue("transNum");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transNum;
	}
	
}
