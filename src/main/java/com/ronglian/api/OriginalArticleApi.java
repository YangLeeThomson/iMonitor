package com.ronglian.api;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.OriginalArticle;
import com.ronglian.model.ReqArticle;
import com.ronglian.service.ArticlesHandlerService;
import com.ronglian.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 苏州台推送原始文章数据接口
 * @author sunqian
 * @date 2018年5月9日 下午4:41:51
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class OriginalArticleApi {

//	@Autowired
//	OriginalArticleService OriginalArticleService;
//	
//	@Autowired
//	PlatformTypeMapper platformTypeMapper;
	
//	@Autowired
//	private RedisTemplate<Object, Object> redisTemplate;

//	@Value("${imoniter.interface.suzhou.url}")
//	private String suzhouUrl;
	
	@Autowired
	private ArticlesHandlerService articlesHandlerService;

//	/**
//	 * 接收苏州台原始文章内容的接口
//	 */
//	
//	@RequestMapping("/OriginalArticles")
//	public JsonResult getOriginalArticles(@RequestParam("time") String time, @RequestParam("key") String key,
//			@RequestBody ReqArticle reqArticle) {
//		OriginalArticle oa0=reqArticle.getData().get(0);
//		log.debug("--接收到苏州台原始文章:文章总数{} 第一条文章信息{}", reqArticle.getData().size(),"media_id "+oa0.getMedia_id()+" origin_app "+oa0.getOrigin_app()+
//				" public_time "+ oa0.getPublic_time() );
//
//		JsonResult jsonResult = new JsonResult();
//
//		if (!paramVerification(time, key)) {
//			log.debug("接口请求合法性验证未通过！");
//			jsonResult.setCode(ResultCode.PARAMS_ERROR);
//			jsonResult.setMessage("非法的接口调用！");
//			return jsonResult;
//		}
//		List<OriginalArticle> articleList = reqArticle.getData();
//		for (OriginalArticle article : articleList) {
//			try {
//				String whenS="1970-01-01 08:00:01";//mysql中Timestamp无法存储当前时间以前的日期
//				SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//				Date when = sdf.parse(whenS);
//				if(article.getPublic_time().before(when)) {
//					article.setPublic_time(when);
//				}
////				jsonResult = OriginalArticleService.handleOriginalArticle(article);
//			} catch (ParseException e) {
//				e.printStackTrace();
//				continue;
//			}
//		}
//
//		return jsonResult;
//	}
	
	@RequestMapping("/originArticles")
	public JsonResult articles(@RequestBody ReqArticle reqArticle,
			@RequestParam("time") String time, 
			@RequestParam("key") String key){
		log.info("request data:"+reqArticle.toString());
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<OriginalArticle> originalArticleList = reqArticle.getData();
		if(!originalArticleList.isEmpty()) {
			//插入ES
			articlesHandlerService.handleOriginalArticle(originalArticleList);
		}else {
			return new JsonResult(ResultCode.ERROR,"数据为空!");
		}
		return new JsonResult(ResultCode.SUCCESS,"成功");
	}

	/**
	 * @Description: 校验苏州台传来的key、time值是否合法
	 * @param time
	 *            time参数
	 * @param key
	 *            校验值
	 * @return boolean
	 */
	private boolean paramVerification(String time, String key) {
		if (time == null || key == null)
			return false;

		long currentSecond = System.currentTimeMillis() / 1000;
		long requestSecond = Long.parseLong(time);
		if (currentSecond - requestSecond > 60 * 30) {
			return false;// 如果请求的时间距离现在超过30分，不通过
		}

		String suffixedTime = time + "HI8i921&";
		String md5Time = MD5Util.encodeByMD5(suffixedTime);
		String serverKey = md5Time.substring(0, 8);
		if (!serverKey.equals(key)) {
			return false;
		}

		return true;
	}

	@RequestMapping("/articleInfo")
	public JsonResult articleInfo(@RequestBody String jsonData,
			@RequestParam("time") String time, 
			@RequestParam("key") String key){
		log.info("jsonData:"+jsonData);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		if (StringUtils.isNotEmpty(jsonData)) {
			articlesHandlerService.articleInfo(jsonData);
		}
		
		return new JsonResult(ResultCode.SUCCESS,"成功");
	}
	
}
