package com.ronglian.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.Constants;
import com.ronglian.mapper.ArticleInfoHourMapper;
import com.ronglian.model.Article;
import com.ronglian.model.ArticleInfoHour;
import com.ronglian.model.OriginalArticle;
import com.ronglian.model.Platform;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.ArticlesHandlerService;
import com.ronglian.service.CopyrightMonitorService;
import com.ronglian.service.PlatformService;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午6:51:23
* @description:文章处理
*/
@Slf4j
@Service("articlesHandlerService")
public class ArticlesHandlerServiceImpl implements ArticlesHandlerService {

	@Autowired
	private PlatformService platformService;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	private ElasticRepository elasticRepository; 
	
	@Autowired
	ArticleInfoHourMapper articleInfoHourMapper;
	
	@Autowired
	CopyrightMonitorService copyrightMonitorService;

	@Value("${imoniter.interface.nlp.imonitornlpurl}")
	private String nlpUrl;
	
	@Async
	@Override
	public void handleOriginalArticle(List<OriginalArticle> articles) {
		// TODO Auto-generated method stub
		List<Article> articleList = new ArrayList<Article>();
		for(OriginalArticle originalArticle:articles) {
			String originApp = null;
			String articleId = null;
			String publishDate = null;
			String unionId = null;
			try {
				originApp = originalArticle.getOrigin_app();
				articleId = originalArticle.getMedia_id();
				unionId = originApp+"_"+articleId;
				if(originalArticle.getPublic_time()==null) {
					throw new Exception("文章发布时间为空！");
				}
				publishDate = Utils.dateToString(originalArticle.getPublic_time(),Constants.DEFAULT_DATE_FORMAT_YMD);
				
				int mediaStatus = originalArticle.getMedia_status();
				if(mediaStatus == 0) {
					//新增去重，hset，key:日期-origin-articles-distinct,hashkey:originApp+"_"+articleId
					if(redisTemplate.opsForHash().hasKey(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE,unionId)) {
						//如果已经存在
						log.info("文章已经存在："+unionId);
						continue;
					}
				}
				//转换成es对应字段
				String platformName = Constants.PLATFORM_NAME.get(originApp);
				if("3".equals(originApp)||"4".equals(originApp)) {//如果类型是微信或微博，名称为column
					platformName = originalArticle.getColumn();
				}
				if(StringUtils.isEmpty(platformName)){
					throw new Exception("分类不存在，origin_app:"+originApp);
				}
				Platform platform = platformService.findByName(platformName,Constants.PLATFORM_TYPE_NAME.get(originApp), "1");
				if(platform == null) {
					//错误情况，把文章信息保存到redis，以便重试，队列名称：日期-origin-articles-error
					throw new Exception("平台信息不存在!");
				}
				String platformTypeId = platform.getPlatformTypeId();
				Article article = new Article();
				article.setUnionId(unionId);
				article.setArticleId(articleId);
				article.setUrl(originalArticle.getOrigin_url());
				article.setTitle(originalArticle.getTitle());
				article.setContent(originalArticle.getContent());
				article.setColumn(originalArticle.getColumn());
				article.setReport(originalArticle.getRepoter());
				article.setSource(originalArticle.getSource());
				article.setPublishTime(originalArticle.getPublic_time());
				article.setStatus(originalArticle.getMedia_status());
				article.setPlatformId(platform.getId());
				article.setPlatformName(platformName);
				article.setPlatformTypeId(platformTypeId);
				article.setPlatformTypeName(platform.getPlatformTypeName());
				article.setCreateTime(new Date());
				article.setCommentNum(originalArticle.getCommentNum());
				article.setThumbsNum(originalArticle.getThumbsNum());
				article.setTransNum(originalArticle.getTransNum());
				if("原创".equals(originalArticle.getSource())||"3".equals(originApp)||"4".equals(originApp)) {//如果类型为微信或者标明原创
					article.setIsOrigin(1);
				}else {
					article.setIsOrigin(0);
				}
				if(mediaStatus == 0) {
					Map<String,Article> articleMap = new HashMap<String,Article>();
					articleMap.put(unionId,article);
					log.info("imonitor_article-"+Utils.dateToString(article.getPublishTime(),Constants.DEFAULT_DATE_FORMAT_YMD2));
					elasticRepository.bulkAdd("imonitor_article-"+Utils.dateToString(article.getPublishTime(),Constants.DEFAULT_DATE_FORMAT_YMD2), "imonitor",articleMap);
					JSONObject data = new JSONObject();
					data.put("originApp", originApp);
					data.put("articleId", articleId);
					data.put("publishTime", Utils.dateToString(new Date(),Constants.DATE_FORMAT_YMDHMS));
					//如果是新增，把origin_app+articleId存入redis,用于下次去重，并设置过期时间
					if(!redisTemplate.hasKey(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE)) {
						//设置过期时间
						//{\"originApp\":\""+originApp+"\",\"articleId\":\""+articleId+"\",\"publishTime\":\""+Utils.dateToString(new Date(),Constants.DATE_FORMAT_YMDHMS)+"\"}"
						redisTemplate.opsForHash().put(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE,unionId,data.toJSONString());
						redisTemplate.expire(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE, 14, TimeUnit.DAYS);
					}else {
						redisTemplate.opsForHash().put(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_DISTINCE,unionId,data.toJSONString());
					}
					
					//分类计算队列
					JSONObject classificationContent = new JSONObject();
					classificationContent.put("title",originalArticle.getTitle());
					classificationContent.put("unionId",unionId);
					classificationContent.put("content",originalArticle.getContent());
					classificationContent.put("publishTime",Utils.dateToString(originalArticle.getPublic_time(),Constants.DEFAULT_DATE_FORMAT_YMD2));
					redisTemplate.opsForSet().add(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_CLASSIFICATION,classificationContent.toString());
					
					//文章信息放入redis，过期时间为14天,重点监测媒体
					JSONObject focalMediaContent = new JSONObject();
					focalMediaContent.put("title",originalArticle.getTitle());
					focalMediaContent.put("content",originalArticle.getContent());
					focalMediaContent.put("originApp",originApp);
					focalMediaContent.put("platformId",platform.getId());
					focalMediaContent.put("platformTypeId",platformTypeId);
					focalMediaContent.put("articleId",originalArticle.getMedia_id());
					focalMediaContent.put("unionId",unionId);
					focalMediaContent.put("publishDate",publishDate);
					if(!redisTemplate.hasKey(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_FOCALMEDIA)) {
						redisTemplate.opsForSet().add(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_FOCALMEDIA, focalMediaContent.toString());
						redisTemplate.expire(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_FOCALMEDIA, 14, TimeUnit.DAYS);
					}else {
						redisTemplate.opsForSet().add(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_FOCALMEDIA, focalMediaContent.toString());
					}
					articleList.add(article);
				}else {
					//更新操作
					JSONObject articleObject = (JSONObject) JSONObject.toJSON(article);
					articleObject.remove("unionId");
					articleObject.remove("commentNum");
					articleObject.remove("clickNum");
					articleObject.remove("distinctUserClickNum");
					articleObject.remove("thumbsNum");
					articleObject.remove("awardNum");
					articleObject.remove("shareNum");
					articleObject.remove("subscribeNum");
					articleObject.remove("transNum");
					articleObject.remove("comprehensive");
					articleObject.remove("classification");
					String query = "{\r\n" + 
							"    \"doc\" : "+articleObject.toJSONString()+
							"}";
					elasticRepository.updateES("POST","/"+Constants.ES_INDEX_IMONITOR_ARTICLE_PERFIX+Utils.dateToString(originalArticle.getPublic_time(),Constants.DEFAULT_DATE_FORMAT_YMD2)+"/imonitor/"+unionId,query);
				}
				//相似相关队列
				JSONObject data = new JSONObject();
				data.put("title",originalArticle.getTitle());
				data.put("content",originalArticle.getContent());
				data.put("union_id",unionId);
				data.put("publish_time",originalArticle.getPublic_time().getTime());
				data.put("media_status",mediaStatus);
				redisTemplate.opsForSet().add(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_TRANS,data.toJSONString());
			}catch (Exception e) {
				// TODO: handle exception
				try {
					if(!redisTemplate.hasKey(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_ERROR)) {
						redisTemplate.opsForHash().put(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_ERROR,unionId,JSONObject.toJSON(originalArticle).toString());
						redisTemplate.expire(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_ERROR,14, TimeUnit.DAYS);
					}else {
						redisTemplate.opsForHash().put(publishDate+"_"+Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_ERROR,unionId,JSONObject.toJSON(originalArticle).toString());
					}
				}catch (Exception e2) {
					// TODO: handle exception
					log.error("原始文章处理失败!",e2);
				}
				log.error("原始文章处理失败!",e);
			}
			log.info("-----------------------------process end------------------------------");
		}
	}

	@Override
	public void pushToSimilarityService() {
		// TODO Auto-generated method stub
		Cursor<Object> curosr = redisTemplate.opsForSet().scan(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_TRANS,ScanOptions.NONE);
		while(curosr.hasNext()) {
			try {
				String content = (String)curosr.next();
				JSONObject art=JSONObject.parseObject(content);
				String text=html2Text(art.getString("content"));
				if(StringUtils.isEmpty(text)) {
					redisTemplate.opsForSet().remove(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_TRANS,content);
					log.info("发送原文：原文ID为空！！union_id:{}",JSONObject.parseObject(content).getString("union_id"));
					continue;
				}
				log.info("发送原文：union_id:{}",JSONObject.parseObject(content).getString("union_id"));
				String result = HttpUtil.post(nlpUrl,content);
				log.info("返回值："+result);
				JSONObject jsonResult = (JSONObject)JSONObject.parse(result);
				String code = jsonResult.getString("code");
				if("200".equals(code)) {
					redisTemplate.opsForSet().remove(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_TRANS,content);
				}
			}catch (Exception e) {
				// TODO: handle exception
				log.error("文章推送至相似相关失败");
			}
			
		}
	}

	@Override
	public void articleInfo(String jsonData) {
		// TODO Auto-generated method stub
		JSONObject relust = JSON.parseObject(jsonData);
		String unionId = relust.getString("unionId");
		String publishDate = relust.getString("publishDate");
		int transNum=getTransNum(unionId);
		String query = "{    " + 
				"    \"doc\" : {    " + 
				"        \"commentNum\":"+relust.getString("comments_num")	+",    " + 
				"        \"clickNum\":0," + 
				"        \"distinctUserClickNum\":0," + 
				"        \"thumbsNum\":"+relust.getString("attitudes_num")+",    " + 
				"        \"awardNum\":0,    " + 
				"        \"shareNum\":0,    " + 
				"        \"subscribeNum\":0,    " + 
				"        \"transNum\":"+transNum+",    " + 
				"        \"realTransNum\":"+relust.getString("repost_num")+",    " + 
				"        \"comprehensive\":0.0" + 
				"    }    " + 
				"}";
		try {
			@SuppressWarnings("unused")
			String res=elasticRepository.updateES("POST","imonitor_article-"+publishDate.replace("-", ".")+"/imonitor/"+unionId, query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject newNums=JSONObject.parseObject(query).getJSONObject("doc");
		Article art=copyrightMonitorService.getOriginalArticle(unionId);
		String platformId=art.getPlatformId();
		String platformTypeId=art.getPlatformTypeId();
		addUpdateMysql(platformId,platformTypeId,newNums,unionId,publishDate);
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
		String endpoint = Utils.endpointCreater("imonitor_trans_info-",1, null, null);
		String queryString="{\r\n" + 
				"  \"size\": 0,\r\n" + 
				"  \"query\": {\r\n" + 
				"    \"bool\": {\r\n" + 
				"      \"must\": [\r\n" + 
				"{\r\n" + 
				"          \"query_string\": {\r\n" + 
				"            \"query\": \"unionId:"+unionId+"\",\r\n" + 
				"            \"analyze_wildcard\": true\r\n" + 
				"          }\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"must_not\": []\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  \"_source\": {\r\n" + 
				"    \"excludes\": []\r\n" + 
				"  },\r\n" + 
				"  \"aggs\": {\r\n" + 
				"    \"unionId\": {\r\n" + 
				"      \"terms\": {\r\n" + 
				"        \"field\": \"unionId\",\r\n" + 
				"        \"size\": 300000,\r\n" + 
				"        \"order\": {\r\n" + 
				"          \"_count\": \"desc\"\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		//log.info("queryString:"+queryString);
		try {
			String rst= elasticRepository.queryES("GET", endpoint, queryString);
			JSONArray temp=JSON.parseObject(rst).getJSONObject("aggregations").getJSONObject("unionId").getJSONArray("buckets");
			if(temp.size()>0)
				transNum=temp.getJSONObject(0).getIntValue("doc_count");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transNum;
	}
	
	public static String html2Text(String str) {
		Pattern pattern = Pattern.compile("<[^>]*>");//  <[.[^<]]*>
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll("");
		return str;
	}

}
