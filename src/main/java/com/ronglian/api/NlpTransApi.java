package com.ronglian.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.Constants;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.Article;
import com.ronglian.model.TransInfo;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.CopyrightMonitorService;
import com.ronglian.utils.MD5Util;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 接收nlp转载文章接口
 * @author sunqian
 * @date 2018年6月21日 下午6:41:51
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class NlpTransApi {
	
	@Autowired
	ElasticRepository elasticRepository;
	
	@Autowired
	CopyrightMonitorService copyrightMonitorService;


	/**
	 * 接收nlp转载文章接口
	 */
	
	@RequestMapping("/NlpTransApi")
	public JsonResult nlpTransApi(@RequestBody String numRst) {
		log.debug("--接收到nlp转载文章" );

		JsonResult jsonResult = new JsonResult();
//		if (!paramVerification(time, key)) {
//			log.debug("接口请求合法性验证未通过！");
//			jsonResult.setCode(ResultCode.PARAMS_ERROR);
//			jsonResult.setMessage("非法的接口调用！");
//			return jsonResult;
//		}
		if (!StringUtils.isEmpty(numRst)) {
			JSONArray data=JSON.parseArray(numRst);
			log.info("从imonitor相似相关接口返回：共{}篇文章", data.size());
			Map<String,TransInfo> toEs=new HashMap<String,TransInfo>();
			if(data.getJSONObject(0)==null) {
				jsonResult.setCode(ResultCode.UNKNOWN_ERROR);
				jsonResult.setMessage("调用失败,发来文章数组为空！");
				return jsonResult;
			}
			String unionId=data.getJSONObject(0).getString("unionId");
			int transNumTotal=data.getJSONObject(0).getIntValue("transNumTotal");
			Article originalArticle=copyrightMonitorService.getOriginalArticle(unionId);
			if(originalArticle!=null) {
				Date originalPublishTime=originalArticle.getPublishTime();//得到原文发布时间，更新原文表转载数用
				for (int i = 0; i < data.size(); i++) {
					try {
						JSONObject transArticle = data.getJSONObject(i);
						TransInfo transInfo = new TransInfo();
						transInfo.setUnionId(unionId);
						transInfo.setPlatformId(originalArticle.getPlatformId());
						transInfo.setPlatformName(originalArticle.getPlatformName());
						transInfo.setPlatformTypeId(originalArticle.getPlatformTypeId());
						transInfo.setPlatformTypeName(originalArticle.getPlatformTypeName());
						transInfo.setOriginTitle(originalArticle.getTitle());
						transInfo.setArticleId(originalArticle.getArticleId());
						transInfo.setOriginArticlePubTime(originalArticle.getPublishTime());
						transInfo.setIsOrigin(originalArticle.getIsOrigin());
						
						transInfo.setTitle(transArticle.getString("title"));
						transInfo.setContent(transArticle.getString("content"));
						transInfo.setCrawSourceDomain(transArticle.getString("crawSourceDomain"));
						transInfo.setCrawlSource(transArticle.getString("crawlSource"));
						transInfo.setProvince(transArticle.getString("province"));
						transInfo.setMediaType(transArticle.getIntValue("mediaType"));
						transInfo.setChannel(transArticle.getIntValue("channel"));
						transInfo.setReportTime(transArticle.getDate("reportTime"));
						transInfo.setWebpageCode(transArticle.getString("webpageCode"));
						transInfo.setCreateTime(new Date());
						transInfo.setTransEsScore(transArticle.getDoubleValue("ESscore"));
						transInfo.setTransSimilarity(transArticle.getDoubleValue("similarity"));
						transInfo.setReportSource(transArticle.getString("reportSource"));
						transInfo.setScreenshot(transArticle.getString("screenshot"));
						transInfo.setWebpageUrl(transArticle.getString("transUrl"));
						transInfo.setIsTort(transArticle.getIntValue("isTort"));
						
						toEs.put(transArticle.getString("webpageCode"), transInfo);
						
						//存转载表
						elasticRepository.bulkAdd("imonitor_trans_info-"+Utils.dateToString(originalArticle.getPublishTime(),Constants.DEFAULT_DATE_FORMAT_YMD2), "imonitor", toEs);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						jsonResult.setCode(ResultCode.UNKNOWN_ERROR);
						jsonResult.setMessage("调用失败！");
						return jsonResult;
					}  catch (Exception e) {
						e.printStackTrace();
						jsonResult.setCode(ResultCode.UNKNOWN_ERROR);
						jsonResult.setMessage("调用失败！");
						return jsonResult;
					}
				}
				//更新转载数到原文表
				String query = "{    " + 
						"    \"doc\" : {    " + 
						"        \"transNum\":"+transNumTotal+"    " + 
						"    }    " + 
						"}";
				String publishDate=Utils.dateToString(originalPublishTime, Constants.DEFAULT_DATE_FORMAT_YMD2);
				try {
					@SuppressWarnings("unused")
					String res=elasticRepository.updateES("POST","imonitor_article-"+publishDate+"/imonitor/"+unionId, query);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			
			}else {
				jsonResult.setCode(ResultCode.UNKNOWN_ERROR);
				jsonResult.setMessage("调用失败,未找到原文！");
				return jsonResult;
			}
		}else {
			jsonResult.setCode(ResultCode.UNKNOWN_ERROR);
			jsonResult.setMessage("调用失败！");
			return jsonResult;
		}

		return jsonResult;
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
}
