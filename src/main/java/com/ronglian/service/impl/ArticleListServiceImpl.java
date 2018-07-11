package com.ronglian.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.PageResult;
import com.ronglian.model.TransAndOriginalArticle;
import com.ronglian.repository.HomePageRepository;
import com.ronglian.service.ArticleListService;
import com.ronglian.service.CustomGroupService;
import com.ronglian.utils.CalendarUtil;
import com.ronglian.utils.Utils;

/**
 * 
 * @Description: 查询接口实现类
 * @author sunqian
 * @date 2018年6月15日 下午3:42:54
 */

@Service("articleListService")
public class ArticleListServiceImpl implements ArticleListService {

	@Autowired
	HomePageRepository homePageRepository;

	@Autowired
	CustomGroupService customGroupService;

	@Override
	public PageResult<TransAndOriginalArticle> findTransMediaTypeList(String platformTypeId, String platformId,
			String groupId, int mediaType, Date startTime, Integer accountType, Integer page, Integer pageSize) {

		// 初始化几个变量
		PageResult<TransAndOriginalArticle> pageResult = new PageResult<TransAndOriginalArticle>();
		String esRst = null;

		// 构造platformIdList、endTime
		List<String> platformIdList = getPlatformList(platformTypeId, platformId, groupId);
		Date endTime = getEndtimeByStarttimeAccounttype(startTime, accountType);
		// 从es查询数据
		try {
			esRst = homePageRepository.transMediaTypeList(mediaType, accountType, platformTypeId, platformIdList,
					startTime, endTime, page, pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 赋值分页对象
		pageResult.setNumber(page);
		int totalElement = JSON.parseObject(esRst).getJSONObject("hits").getIntValue("total");
		pageResult.setTotalElements(totalElement);
		pageResult.setTotalPages(Utils.totalPage(totalElement, pageSize));
		pageResult.setLimit(pageSize);

		JSONArray esJson = JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		List<TransAndOriginalArticle> content = new ArrayList<TransAndOriginalArticle>();
		for (int i = 0; i < esJson.size(); i++) {
			JSONObject element = esJson.getJSONObject(i).getJSONObject("_source");
			TransAndOriginalArticle article = new TransAndOriginalArticle();
			article.setTranMediaName(element.getString("crawlSource"));
			article.setTransTitle(element.getString("title"));
			article.setTransUrl(element.getString("transUrl"));
			article.setTransPublishTime(element.getDate("reportTime"));
			article.setWebpageCode(element.getString("webpageCode"));
			article.setOriginalPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
			article.setOriginalTitle(element.getString("originTitle"));
			article.setOriginalUrl(element.getString("originalUrl"));
			article.setUnionId(element.getString("unionId"));
			article.setArticleId(element.getString("articleId"));
			article.setTransUrl(element.getString("webpageUrl"));
			article.setOriginalPublishTime(element.getDate("originArticlePubTime"));
			
			content.add(article);
		}

		pageResult.setContent(content);

		return pageResult;
	}

	@Override
	public PageResult<TransAndOriginalArticle> findTransChannelList(String platformTypeId, String platformId,
			String groupId, int channel, Date startTime, Integer accountType, Integer page, Integer pageSize) {
		// 初始化几个变量
		PageResult<TransAndOriginalArticle> pageResult = new PageResult<TransAndOriginalArticle>();
		String esRst = null;

		// 构造platformIdList、endTime
		List<String> platformIdList = getPlatformList(platformTypeId, platformId, groupId);
		Date endTime = getEndtimeByStarttimeAccounttype(startTime, accountType);
		// 从es查询数据
		try {
			esRst = homePageRepository.transChannelList(channel, accountType, platformTypeId, platformIdList, startTime,
					endTime, page, pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 赋值分页对象
		pageResult.setNumber(page);
		int totalElement = JSON.parseObject(esRst).getJSONObject("hits").getIntValue("total");
		pageResult.setTotalElements(totalElement);
		pageResult.setTotalPages(Utils.totalPage(totalElement, pageSize));
		pageResult.setLimit(pageSize);

		JSONArray esJson = JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		List<TransAndOriginalArticle> content = new ArrayList<TransAndOriginalArticle>();
		for (int i = 0; i < esJson.size(); i++) {
			JSONObject element = esJson.getJSONObject(i).getJSONObject("_source");
			TransAndOriginalArticle article = new TransAndOriginalArticle();
			article.setTranMediaName(element.getString("crawlSource"));
			article.setTransTitle(element.getString("title"));
			article.setTransUrl(element.getString("transUrl"));
			article.setTransPublishTime(element.getDate("reportTime"));
			article.setWebpageCode(element.getString("webpageCode"));
			article.setOriginalPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
			article.setOriginalTitle(element.getString("originTitle"));
			article.setOriginalUrl(element.getString("originalUrl"));
			article.setUnionId(element.getString("unionId"));
			article.setArticleId(element.getString("articleId"));
			article.setTransUrl(element.getString("webpageUrl"));
			article.setOriginalPublishTime(element.getDate("originArticlePubTime"));
			content.add(article);
		}

		pageResult.setContent(content);

		return pageResult;
	}

	@Override
	public PageResult<TransAndOriginalArticle> findTransPlatformList(String platformId, Date startTime,
			Integer accountType, Integer page, Integer pageSize) {
		// 初始化几个变量
				PageResult<TransAndOriginalArticle> pageResult = new PageResult<TransAndOriginalArticle>();
				String esRst = null;

				// 构造platformIdList、endTime
				Date endTime = getEndtimeByStarttimeAccounttype(startTime, accountType);
				// 从es查询数据
				try {
					esRst = homePageRepository.transPlatformList(accountType, platformId, startTime, endTime, page, pageSize);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 赋值分页对象
				pageResult.setNumber(page);
				int totalElement = JSON.parseObject(esRst).getJSONObject("hits").getIntValue("total");
				pageResult.setTotalElements(totalElement);
				pageResult.setTotalPages(Utils.totalPage(totalElement, pageSize));
				pageResult.setLimit(pageSize);

				JSONArray esJson = JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
				List<TransAndOriginalArticle> content = new ArrayList<TransAndOriginalArticle>();
				for (int i = 0; i < esJson.size(); i++) {
					JSONObject element = esJson.getJSONObject(i).getJSONObject("_source");
					TransAndOriginalArticle article = new TransAndOriginalArticle();
					article.setTranMediaName(element.getString("crawlSource"));
					article.setTransTitle(element.getString("title"));
					article.setTransUrl(element.getString("transUrl"));
					article.setTransPublishTime(element.getDate("reportTime"));
					article.setWebpageCode(element.getString("webpageCode"));
					article.setOriginalPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
					article.setOriginalTitle(element.getString("originTitle"));
					article.setOriginalUrl(element.getString("originalUrl"));
					article.setUnionId(element.getString("unionId"));
					article.setArticleId(element.getString("articleId"));
					article.setTransUrl(element.getString("webpageUrl"));
					article.setOriginalPublishTime(element.getDate("originArticlePubTime"));
					content.add(article);
				}

				pageResult.setContent(content);

				return pageResult;
	}

	@Override
	public PageResult<TransAndOriginalArticle> findTransMediaOrderList(String platformTypeId, String platformId,
			String groupId, String mediaName, Date startTime, Integer accountType, Integer page, Integer pageSize) {
		// 初始化几个变量
				PageResult<TransAndOriginalArticle> pageResult = new PageResult<TransAndOriginalArticle>();
				String esRst = null;

				// 构造platformIdList、endTime
				List<String> platformIdList = getPlatformList(platformTypeId, platformId, groupId);
				Date endTime = getEndtimeByStarttimeAccounttype(startTime, accountType);
				// 从es查询数据
				try {
					esRst = homePageRepository.transMediaNameList(accountType, mediaName, platformTypeId, platformIdList, startTime, endTime, page, pageSize);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 赋值分页对象
				pageResult.setNumber(page);
				int totalElement = JSON.parseObject(esRst).getJSONObject("hits").getIntValue("total");
				pageResult.setTotalElements(totalElement);
				pageResult.setTotalPages(Utils.totalPage(totalElement, pageSize));
				pageResult.setLimit(pageSize);

				JSONArray esJson = JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
				List<TransAndOriginalArticle> content = new ArrayList<TransAndOriginalArticle>();
				for (int i = 0; i < esJson.size(); i++) {
					JSONObject element = esJson.getJSONObject(i).getJSONObject("_source");
					TransAndOriginalArticle article = new TransAndOriginalArticle();
					article.setTranMediaName(element.getString("crawlSource"));
					article.setTransTitle(element.getString("title"));
					article.setTransUrl(element.getString("transUrl"));
					article.setTransPublishTime(element.getDate("reportTime"));
					article.setWebpageCode(element.getString("webpageCode"));
					article.setOriginalPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
					article.setOriginalTitle(element.getString("originTitle"));
					article.setOriginalUrl(element.getString("originalUrl"));
					article.setUnionId(element.getString("unionId"));
					article.setArticleId(element.getString("articleId"));
					article.setTransUrl(element.getString("webpageUrl"));
					article.setOriginalPublishTime(element.getDate("originArticlePubTime"));
					content.add(article);
				}

				pageResult.setContent(content);

				return pageResult;
	}

	/**
	 * 
	 * @Description: 传入platformTypeId、platformId、groupId得到平台id列表 @param @param
	 * platformTypeId @param @param platformId @param @param groupId @param @return
	 * 参数 @return List<String> 返回类型 @throws
	 */
	public List<String> getPlatformList(String platformTypeId, String platformId, String groupId) {

		List<String> platformIdList = null;
		if (StringUtils.isBlank(platformTypeId) && StringUtils.isBlank(platformId) && StringUtils.isBlank(groupId))
			// 都为空，查询所有平台,数据层沟通过传null
			// platformIdList =platformService.findByPlatformTypeId(platformTypeId);
			if (StringUtils.isNotBlank(platformTypeId)) {
				// 根据platformTypeId查询
				// platformIdList =platformService.findByPlatformTypeId(platformTypeId);
			}
		if (StringUtils.isNotBlank(platformId)) {
			// 根据platformId查询
			platformIdList = new ArrayList<String>();
			platformIdList.add(platformId);
		}
		if (StringUtils.isNotBlank(groupId)) {
			// 根据groupId查询
			platformIdList = Arrays
					.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		return platformIdList;
	}

	/**
	 * 
	 * @Description: 根据开始时间startTime、周年月类型accountType得到结束时间 @param @param startTime
	 * 开始时间 @param @param accountType 周年月天类型 @param @return 参数 结束时间 @return Date
	 * 返回类型 @throws
	 */
	private Date getEndtimeByStarttimeAccounttype(Date startTime, Integer accountType) {
		switch (accountType) {
			case 0: {// 天
				Date endTime = CalendarUtil.plusDay(1, startTime);
				return endTime;
			}
			case 1: {// 年
				Date endTime = CalendarUtil.getNextNewYearsDay(startTime);
				return endTime;
			}
			case 2: {// 月
				Calendar cal = Calendar.getInstance();
				cal.setTime(startTime);
				int month = cal.get(Calendar.MONTH) + 1;
				int year = cal.get(Calendar.YEAR);
				Date endTime = CalendarUtil.getFirstAndLastdayOfMonth(year, month).get(1);
				endTime = CalendarUtil.plusDay(1, endTime);
				return endTime;
			}
			case 3: {// 周
				Date endTime = CalendarUtil.plusDay(7, startTime);
				return endTime;
			}
			default: {
				return null;
			}
		}
	}

}
