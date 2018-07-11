package com.ronglian.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.Constants;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.utils.HttpUtil;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 黄硕/huangshuo
 * @date:2018年5月30日 上午12:31:21
 * @description:描述
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class GetOriginArticleClassificationJob implements Job {

	@Value("${imonitor.article.classification.url}")
	private String article_classification_url;

	@Autowired
	ElasticRepository elasticRepository;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("-----------------------原始文章计算分类启动------------------------");
		getOriginalArticleClassification();
	}

	/**
	 * 原始文章计算分类
	 */
	public void getOriginalArticleClassification() {
		Cursor<Object> curosr = redisTemplate.opsForSet().scan(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_CLASSIFICATION,ScanOptions.NONE);
		while(curosr.hasNext()) {
			String articleString = (String)curosr.next();
			JSONObject article = JSONObject.parseObject(articleString);
			try {
				String title = article.getString("title");
				String content = article.getString("content");
				if (!StringUtils.isEmpty(content)) {
					content = content.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "")
							.replaceAll("\\]", "").replaceAll("\\,", "").replaceAll("\\&", "").replaceAll("\\:", "");
					if (content.length() > 20000) {
						System.out.println(content);
						content = content.substring(0, 19999);
					}
				} else {
					continue;
				}
				JSONArray arrayToSend = new JSONArray();
				JSONObject user = new JSONObject();
				user.put("userID", "NO.1");
				arrayToSend.add(user);
				JSONArray articleArray = new JSONArray();
				JSONObject articleToSend = new JSONObject();
				articleToSend.put("title", title);
				articleToSend.put("content", content);
				articleArray.add(articleToSend);
				arrayToSend.add(articleArray);
				String result = HttpUtil.post(article_classification_url, arrayToSend.toJSONString());
				log.info("文章{}分类计算，返回值：" + result,article.getString("unionId"));
				String classification = null;
				JSONArray resultJSONArray = null;
				try {
					resultJSONArray = JSONArray.parseArray(result);
				} catch (Exception e) {
					log.error("文章分类计算，返回值：" + result, e);
					continue;
				}
				log.info(resultJSONArray.toJSONString());
				if (resultJSONArray.size() > 0) {
					JSONArray firstArray = resultJSONArray.getJSONArray(0);
					if (firstArray.size() > 0) {
						classification = (String) firstArray.get(0);
						Integer classificationCode = (Integer) Constants.CLASSIFICATION_CODE.get(classification);
						if (classificationCode == null) {
							classificationCode = 14;
						}
						// 存ES
						String query = "{    " + 
										"    \"doc\" : {    " + 
										"        \"classification\":"	+ classificationCode + 
										"    }    " + 
										"}";
						String publishDate = article.getString("publishTime");
						elasticRepository.updateES("POST","/imonitor_article-" + publishDate + "/imonitor/" + article.getString("unionId"),	query);
						
						redisTemplate.opsForSet().remove(Constants.ARTICLES_QUENES.ORIGIN_ARTICLE_CLASSIFICATION,articleString);
					}
				}
			} catch (Exception e) {
				log.error("文章分类统计", e);
			}
		}
	}

}
