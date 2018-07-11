package com.ronglian.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.PageResult;
import com.ronglian.model.HotArticle;
import com.ronglian.repository.HotArticleRepository;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.HotArticleService;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: 黄硕/huangshuo
 * @date:2018年6月19日 下午7:45:27
 * @description:描述
 */
@Slf4j
@Service("hotArticleService")
public class HotArticleServiceImpl implements HotArticleService {

	@Autowired
	private HotArticleRepository hotArticleRepository;

	@Autowired
	private CustomGroupService customGroupService;
	
//	@Autowired
//	private CopyrightMonitorService copyrightMonitorService;

	public PageResult<HotArticle> findPageList(int accountType, String platformTypeId, String platformId,
			String groupId, String original, String orderFiled, Date bTime, Date eTime, int pageNo, int pageSize)
			throws IOException {
		PageResult<HotArticle> pageResult = new PageResult<>();
		List<HotArticle> content = new ArrayList<>();
		if ("0".equals(orderFiled)) {
			orderFiled = "transNum";
		} else if ("1".equals(orderFiled)) {
			orderFiled = "commentNum";
		} else if ("2".equals(orderFiled)) {
			orderFiled = "clickNum";
		} else if ("3".equals(orderFiled)) {
			orderFiled = "thumbsNum";
		} else {
			orderFiled = "publishTime";
		}
		List<String> platformIdList = null;
		if (!StringUtils.isEmpty(platformId)) {
			platformIdList = Arrays.asList(platformId);
		}
		if (!StringUtils.isEmpty(groupId)) {
			platformIdList = Arrays
					.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		int isOriginal = 0;
		if ("0".equals(original)) {
			isOriginal = 3;
		} else if ("1".equals(original)) {
			isOriginal = 0;
		} else if ("2".equals(original)) {
			isOriginal = 1;
		}
		String jsonString = hotArticleRepository.hotArticleList(accountType, platformTypeId, platformIdList, bTime,
				eTime, isOriginal, orderFiled, pageNo, pageSize);
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		log.info("HotArticleService.findPageList(" + accountType + "," + platformTypeId + "," + platformIdList + ","
				+ platformId + "," + groupId + "," + isOriginal + "," + orderFiled + "," + bTime + "," + eTime + ","
				+ pageNo + "," + pageSize + ") take time:" + jsonObject.getInteger("took"));
		JSONObject hits = jsonObject.getJSONObject("hits");
		if (hits != null) {
			int totalCount = hits.getInteger("total");
			if (totalCount > 0) {
				pageResult.setTotalElements(totalCount);
				int totalPages = Utils.totalPage(totalCount, pageSize);
				pageResult.setTotalPages(totalPages);
				pageResult.setNumber(pageNo);
				pageResult.setLimit(pageSize);
				JSONArray hotArticles = hits.getJSONArray("hits");
				if (hotArticles.size() > 0) {
					int seq = 1;
					for (int i = 0; i < hotArticles.size(); i++) {
						JSONObject _source = ((JSONObject) hotArticles.get(i)).getJSONObject("_source");
						HotArticle hotArticle = new HotArticle();
						hotArticle.setUnionId(_source.getString("unionId"));
						hotArticle.setPlatformId(_source.getString("platformId"));
						hotArticle.setPlatformName(_source.getString("platformName")+"_"+_source.getString("platformTypeName"));
						hotArticle.setPlatformTypeId(_source.getString("platformTypeId"));
						String platformTypeName = _source.getString("platformTypeName");
						hotArticle.setPlatformTypeName(platformTypeName);
						hotArticle.setArticleId(_source.getString("articleId"));
						hotArticle.setTitle(_source.getString("title"));
						hotArticle.setTransNum(_source.getIntValue("transNum"));
						hotArticle.setCommentNum(_source.getIntValue("commentNum"));
						hotArticle.setClickNum(_source.getIntValue("clickNum"));
						hotArticle.setThumbsNum(_source.getIntValue("thumbsNum"));
						hotArticle.setPublishTime(_source.getDate("publishTime"));
						if("微信".equals(platformTypeName)||"微博".equals(platformTypeName)) {
							hotArticle.setAuthor("");
						}else {
							hotArticle.setAuthor(_source.getString("report"));
						}
						hotArticle.setInx((pageNo - 1) * pageSize + seq);
						seq += 1;
						content.add(hotArticle);
					}

				}
				pageResult.setContent(content);
			} else {
				pageResult.setTotalElements(0);
				pageResult.setTotalPages(0);
				pageResult.setNumber(pageNo);
				pageResult.setLimit(pageSize);
				pageResult.setContent(null);
			}

		}
		return pageResult;
	}
	
//备份勿删
//	public PageResult<HotArticle> findPageList(int accountType, String platformTypeId, String platformId,
//			String groupId, String original, String orderFiled, Date bTime, Date eTime, int pageNo, int pageSize)
//					throws IOException {
//		PageResult<HotArticle> pageResult = new PageResult<>();
//		List<HotArticle> content = new ArrayList<>();
//		if ("0".equals(orderFiled)) {
//			orderFiled = "transNum";
//		} else if ("1".equals(orderFiled)) {
//			orderFiled = "commentNum";
//		} else if ("2".equals(orderFiled)) {
//			orderFiled = "clickNum";
//		} else if ("3".equals(orderFiled)) {
//			orderFiled = "thumbsNum";
//		} else {
//			orderFiled = "publishTime";
//		}
//		List<String> platformIdList = null;
//		if (!StringUtils.isEmpty(platformId)) {
//			platformIdList = Arrays.asList(platformId);
//		}
//		if (!StringUtils.isEmpty(groupId)) {
//			platformIdList = Arrays
//					.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
//		}
//		int isOriginal = 0;
//		if ("0".equals(original)) {
//			isOriginal = 3;
//		} else if ("1".equals(original)) {
//			isOriginal = 0;
//		} else if ("2".equals(original)) {
//			isOriginal = 1;
//		}
//		if (!"transNum".equals(orderFiled)) {
//			String jsonString = hotArticleRepository.hotArticleList(accountType, platformTypeId, platformIdList, bTime,
//					eTime, isOriginal, orderFiled, pageNo, pageSize);
//			JSONObject jsonObject = JSONObject.parseObject(jsonString);
//			log.info("HotArticleService.findPageList(" + accountType + "," + platformTypeId + "," + platformIdList + ","
//					+ platformId + "," + groupId + "," + isOriginal + "," + orderFiled + "," + bTime + "," + eTime + ","
//					+ pageNo + "," + pageSize + ") take time:" + jsonObject.getInteger("took"));
//			JSONObject hits = jsonObject.getJSONObject("hits");
//			if (hits != null) {
//				int totalCount = hits.getInteger("total");
//				if (totalCount > 0) {
//					pageResult.setTotalElements(totalCount);
//					int totalPages = Utils.totalPage(totalCount, pageSize);
//					pageResult.setTotalPages(totalPages);
//					pageResult.setNumber(pageNo);
//					pageResult.setLimit(pageSize);
//					JSONArray hotArticles = hits.getJSONArray("hits");
//					if (hotArticles.size() > 0) {
//						int seq = 1;
//						for (int i = 0; i < hotArticles.size(); i++) {
//							JSONObject _source = ((JSONObject) hotArticles.get(i)).getJSONObject("_source");
//							HotArticle hotArticle = new HotArticle();
//							hotArticle.setUnionId(_source.getString("unionId"));
//							hotArticle.setPlatformId(_source.getString("platformId"));
//							hotArticle.setPlatformName(_source.getString("platformName")+"_"+_source.getString("platformTypeName"));
//							hotArticle.setPlatformTypeId(_source.getString("platformTypeId"));
//							String platformTypeName = _source.getString("platformTypeName");
//							hotArticle.setPlatformTypeName(platformTypeName);
//							hotArticle.setArticleId(_source.getString("articleId"));
//							hotArticle.setTitle(_source.getString("title"));
//							int transNum=hotArticleRepository.getTransNum(_source.getString("unionId"));//转载数单独查询
//							hotArticle.setTransNum(transNum);
//							hotArticle.setCommentNum(_source.getIntValue("commentNum"));
//							hotArticle.setClickNum(_source.getIntValue("clickNum"));
//							hotArticle.setThumbsNum(_source.getIntValue("thumbsNum"));
//							hotArticle.setPublishTime(_source.getDate("publishTime"));
//							if("微信".equals(platformTypeName)) {
//								hotArticle.setAuthor("");
//							}else {
//								hotArticle.setAuthor(_source.getString("report"));
//							}
//							hotArticle.setInx((pageNo - 1) * pageSize + seq);
//							seq += 1;
//							content.add(hotArticle);
//						}
//						
//					}
//					pageResult.setContent(content);
//				} else {
//					pageResult.setTotalElements(0);
//					pageResult.setTotalPages(0);
//					pageResult.setNumber(pageNo);
//					pageResult.setLimit(pageSize);
//					pageResult.setContent(null);
//				}
//				
//			}
//			
//		} else {
//			String jsonString = hotArticleRepository.hotArticleListTransNum(accountType, platformTypeId, platformIdList, bTime,
//					eTime, isOriginal, orderFiled, pageNo, pageSize);
//			JSONObject jsonObject = JSONObject.parseObject(jsonString);
//			log.info("HotArticleService.findPageList(" + accountType + "," + platformTypeId + "," + platformIdList + ","
//					+ platformId + "," + groupId + "," + isOriginal + "," + orderFiled + "," + bTime + "," + eTime + ","
//					+ pageNo + "," + pageSize + ") take time:" + jsonObject.getInteger("took"));
//			JSONObject hits = jsonObject.getJSONObject("hits");
//			if (hits != null) {
//				int totalCount = hits.getInteger("total");
//				if (totalCount > 0) {
//					pageResult.setTotalElements(totalCount);
//					int totalPages = Utils.totalPage(totalCount, pageSize);
//					pageResult.setTotalPages(totalPages);
//					pageResult.setNumber(pageNo);
//					pageResult.setLimit(pageSize);
//					JSONArray hotArticles = jsonObject.getJSONObject("aggregations").getJSONObject("unionId").getJSONArray("buckets");
//					if (hotArticles.size() > 0) {
//						int seq = 1;
//						for (int i = (pageNo-1)*pageSize; i < hotArticles.size(); i++) {
//							JSONObject _source = hotArticles.getJSONObject(i);
//							Article article=copyrightMonitorService.getOriginalArticle(_source.getString("key"));
//							
//							HotArticle hotArticle = new HotArticle();
//							hotArticle.setUnionId(_source.getString("key"));
//							hotArticle.setPlatformId(article.getPlatformId());
//							hotArticle.setPlatformName(article.getPlatformName()+"_"+article.getPlatformTypeName());
//							hotArticle.setPlatformTypeId(article.getPlatformTypeId());
//							hotArticle.setPlatformTypeName(article.getPlatformTypeName());
//							hotArticle.setArticleId(article.getArticleId());
//							hotArticle.setTitle(article.getTitle());
//							hotArticle.setTransNum(_source.getIntValue("doc_count"));
//							hotArticle.setCommentNum(article.getCommentNum());
//							hotArticle.setClickNum(article.getClickNum());
//							hotArticle.setThumbsNum(article.getThumbsNum());
//							hotArticle.setPublishTime(article.getPublishTime());
//							hotArticle.setAuthor(article.getReport());
//							hotArticle.setInx((pageNo - 1) * pageSize + seq);
//							seq += 1;
//							content.add(hotArticle);
//						}
//						
//					}
//					pageResult.setContent(content);
//				} else {
//					pageResult.setTotalElements(0);
//					pageResult.setTotalPages(0);
//					pageResult.setNumber(pageNo);
//					pageResult.setLimit(pageSize);
//					pageResult.setContent(null);
//				}
//				
//			}
//			
//		}
//		
//		return pageResult;
//	}
}
