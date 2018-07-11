/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年6月20日 下午10:42:35 
 */
package com.ronglian.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.common.exception.BaseException;
import com.ronglian.common.exception.ExceptionDescription;
import com.ronglian.common.exception.ExceptionType;
import com.ronglian.model.AreaTransInfo;
import com.ronglian.model.WeekedCircle;
import com.ronglian.repository.HomePageRepository;
import com.ronglian.repository.PlatformCompareAnalysisRepository;
import com.ronglian.repository.SpreadAreadAnalysisRepository;
import com.ronglian.repository.SpreadRepository;
import com.ronglian.service.CommonService;
import com.ronglian.service.SpreadService;
import com.ronglian.utils.DateTimeUtils;
import com.ronglian.utils.JsonUtils;
import com.ronglian.utils.Utils;

 /** 
 * @ClassName: SpreadServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月20日 下午10:42:35  
 */
@Service
public class SpreadServiceImpl implements SpreadService {

	@Autowired
	private SpreadRepository spreadRepository;
	
	@Autowired
	private HomePageRepository homePageRepository;
	
	@Autowired
	private SpreadAreadAnalysisRepository spreadAreaRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PlatformCompareAnalysisRepository platformCompareRepository;
	
	/* 
	 * 获取周环比
	 */
	@Override
	public List<WeekedCircle> getWeekedCircleAccounted(Date startTime,Integer accountType,
			String typeId, List<String> platformIdList) {
		// TODO Auto-generated method stub
		List<WeekedCircle> resultList = null;
		Date endTime = null;
		Date lastStartTime = null;
		Date lastEndTime = null;
		String str = null;
		String strLastweek = null;
		endTime = getEndTime(accountType, startTime);
		int year = startTime.getYear()+1900;
		int month = startTime.getMonth()+1;
		try {
			if(accountType == 3){
				lastStartTime = DateTimeUtils.getLastWeekMonday(startTime);
				lastEndTime = DateTimeUtils.getOnedayByNum(7, lastStartTime);
			}
			if(accountType == 1){
				lastStartTime = DateTimeUtils.getYearFirst(year-1);
//				lastEndTime = DateTimeUtils.getYearLast(year-1);
				lastEndTime = startTime;
			}
			if(accountType == 2){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				lastStartTime = sdf.parse(DateTimeUtils.getLastDayOfMonth(year,month-2));
				lastStartTime = DateTimeUtils.getOnedayByNum(1, lastStartTime);
				lastEndTime = sdf.parse(DateTimeUtils.getLastDayOfMonth(year, month-1));
				lastEndTime = DateTimeUtils.getOnedayByNum(1, lastEndTime);
			}
			if(accountType == 0){
				lastEndTime = startTime;
				lastStartTime = DateTimeUtils.getOnedayByNum(-1, lastEndTime);
				
			}
			str = this.homePageRepository.originArticleProportion(accountType, typeId, platformIdList, startTime, endTime);
			strLastweek = this.homePageRepository.originArticleProportion(accountType, typeId, platformIdList, lastStartTime, lastEndTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultList = handlerMappingWeekedCircle(str,strLastweek);
		return resultList;
	}

	/*
	 *  地区转载列表
	 */
	@Override
	public Map getSpreadAreaTransList(String platformTypeId, List<String> platformIdList,
			String province, Date startTime, Integer accountType,
			Integer pageNo, Integer pageSize) {
		// 
		Date endTime = getEndTime(accountType, startTime);
		String str = null;
		Map resultMap = null;
		Integer pageStart = (pageNo - 1) * pageSize;
		try {
			str = this.spreadAreaRepository.areaTransList(province, accountType, platformTypeId, platformIdList, startTime, endTime, pageNo, pageSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return null;
		}		
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		resultMap = handlerMappingAreaTransList(str,province,pageStart);
		return resultMap;
	}

	/* 
	 * 首页传播情况统计
	 */
	@Override
	public Map getPlatformArticleInfo(List<String> platformIdList, String platformTypeId,
			Date startTime, Integer accountType) {
		// TODO Auto-generated method stub
		Date endTime = getEndTime(accountType, startTime);
		String str = null;
		String tranStr = null;
		
		try {
//			tranStr = this.homePageRepository.articleTransCount(accountType, platformTypeId, platformIdList, startTime, endTime);
			str = this.homePageRepository.spreadCondition(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return null;
		}
		Map result = null;
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		result = handlerMappingSpreadAccounted(str,tranStr);
		return result;
	}

	/* 
	 * 平台文章统计
	 */
	@Override
	public List<Map> getPlatformArticleNumAccounted(Date startTime,
			Integer accountType, String platformTypeId,List<String> platformIdList) {
		// TODO Auto-generated method stub
		Date endTime = getEndTime(accountType, startTime);
		String str = null;
		List<Map> resultList = null;
		try {
			if(StringUtils.isBlank(platformTypeId) && platformIdList == null){
				str = this.homePageRepository.platformTypeProportion(accountType, startTime, endTime);
			}else{
				str = this.homePageRepository.platformProportion(accountType, platformTypeId, platformIdList, startTime, endTime);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return null;
		}		
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		 resultList = handlerMappingPlatformArticleNumAccounted(str,platformTypeId,platformIdList);
		return resultList;
	}

	/* 
	 * 原创文章统计
	 */
	@Override
	public Map getOriginalArticleNumAccounted(Date startTime,
			Integer accountType, String typeId, List<String> platformIdList) {
		// TODO Auto-generated method stub
		Date endTime = getEndTime(accountType, startTime);
		String str = null;
		Map resultMap = null;
		try {
			str = this.homePageRepository.originArticleProportion(accountType, typeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultMap = handlerMappingOriginalArticleNum(str);
		return resultMap;
	}

	/* 
	 * 5.5传播地域分布
	 */
	@Override
	public List<Map> getSpreadAreaCurrent(String platformTypeId,
			List<String> platformIdList, Date startTime, Integer accountType) {
		// TODO Auto-generated method stub
		Date endTime = getEndTime(accountType, startTime);
		List<Map> resultList = null;
		String str = null;
		try {
			str = this.homePageRepository.spreadAreaDistribution(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultList = handlerMappingAreaCurrent(str);
		return resultList;
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.SpreadService#getProvienceTransnum(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.Integer)
	 */
	@Override
	public Integer getProvienceTransnum(String platformTypeId,
			List<String> platformIdList, String province, Date startTime,
			Integer accountType) {
		// TODO Auto-generated method stub
		Date endTime = getEndTime(accountType, startTime);
		Integer num = null;
		String str = null;
		try {
			str = this.homePageRepository.spreadAreaDistribution(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		num = handlerMapping(str,province);
		return num;
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.SpreadService#getArticleListOrederByTransNum(java.lang.String, java.lang.String, java.util.Date, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map> getArticleListOrederByTransNum(String platformTypeId,
			String articleTypeId, Date startTime, Integer accountType,
			Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.SpreadService#getPlatformTrans(java.lang.String, java.util.Date, java.lang.Integer)
	 */
	@Override
	public List<Map> getPlatformTrans2(String platformTypeId,List<String> platformIdList,Date startTime,
			Integer accountType) {
		Date endTime = getEndTime(accountType, startTime);
		List resultList = null;
		String str = null;
		try {
			str = this.homePageRepository.platformTransedBang(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultList = handlerMappingPlatformTrans(str,false);
		return resultList;
	}
	@Override
	public List<Map> getPlatformTrans(String platformTypeId,List<String> platformIdList,Date startTime,
			Integer accountType) {
		List resultList = null;
		Date endTime = getEndTime(accountType, startTime);
		String str = null;
		try {
			str = this.homePageRepository.platformTransedBang(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultList = handlerMappingPlatformTrans(str,true);
		return resultList;
	}
	
	private static Date getEndTime(int accountType,Date startTime){
		Date endTime = null;
		try {
			if(accountType == 0){
				endTime = DateTimeUtils.getOnedayByNum(1, startTime);
			}
			if(accountType == 1){
				int year = startTime.getYear()+1900;
				endTime = DateTimeUtils.getYearLast(year);
				endTime = DateTimeUtils.getOnedayByNum(1, endTime);
				System.out.println(endTime+"   "+year);
			}
			if(accountType == 2){
				int year = startTime.getYear()+1900;
				int month = startTime.getMonth()+1;
				String timeStr = DateTimeUtils.getLastDayOfMonth(year, month);
				endTime = new SimpleDateFormat("yyyy-MM-dd").parse(timeStr);
				endTime = DateTimeUtils.getOnedayByNum(1, endTime);
			}
			if(accountType == 3){
				endTime = DateTimeUtils.getOnedayByNum(7, startTime);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return endTime;
	}
	private static Integer getTransNum(String transStr){
		if(StringUtils.isBlank(transStr)){
			return 0;
		}		
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(transStr);
		if(transforMap.get("hits") == null){
			return 0;
		}
		Map<String, Object> transforMap1 = (Map<String, Object>) transforMap.get("hits");
		Object obj = transforMap1.get("total");
		if(obj == null){
			return 0;
		}
		int transNum = Integer.parseInt(obj.toString());
		return transNum;
	}
	private String getPlatformTypeName(String platformId){
		String str = null;
		String platformTypeId = this.commonService.getPlatformTypeByPlatformId(platformId);
		if(StringUtils.isBlank(platformTypeId)){
			return "未知的平台组";
		}
		str = this.commonService.getPlatformTypeNameById(platformTypeId);
		if(StringUtils.isBlank(str)){
			return "未知的平台组";
		}
		return str;
	}
	private Map handlerMappingSpreadAccounted(String str,String transStr){
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		Map<String, Object> transform11 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return null;
		}
		Map result = new HashMap();
		//作品总数
		if(transform11 != null){
			result.put("articleTotalNum", transform11.get("total"));
		}
		//阅读数
		Map readNum = (Map) transform1.get("clickNum");
		if(readNum != null){
			result.put("readNum", readNum.get("value"));
		}
		//收藏数
		Map collectionNum = (Map) transform1.get("subscribeNum");
		if(collectionNum != null){
			result.put("collectionNum", collectionNum.get("value"));
		}
		//分享数
		Map shareNum = (Map) transform1.get("shareNum");
		if(shareNum != null){
			result.put("shareNum", shareNum.get("value"));
		}
		//评论数
		Map commentNum = (Map) transform1.get("commentNum");
		if(commentNum != null){
			result.put("commentNum", commentNum.get("value"));
		}
		//点赞数
		Map rewardNum = (Map) transform1.get("thumbsNum");
		if(rewardNum != null){
			result.put("rewardNum", rewardNum.get("value"));
		}
		//打赏数
		Map thumbsNum = (Map) transform1.get("awardNum");
		if(thumbsNum != null){
			result.put("thumbsNum", thumbsNum.get("value"));
		}
		
/*		//转载数
		int insteadNum = getTransNum(transStr);
		result.put("transNum", insteadNum);*/
		Map transNum = (Map) transform1.get("transNum");
		if(transNum != null){
			result.put("transNum", transNum.get("value"));
		}
		return result;
	}
	private List handlerMappingWeekedCircle(String str,String strLastweek){
		List<WeekedCircle> resultList = new ArrayList<WeekedCircle>();
		Map nowWeekesult = new HashMap();
		if(StringUtils.isNotBlank(str)){
			Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
			Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
			Map<String, Object> transform2 = null;
			if(transform1 != null ){
				transform2 = (Map<String, Object>) transform1.get("isOrigin");
			}
			List<Map> list = null;
			if(transform2 != null){
				list = (List<Map>) transform2.get("buckets");
			}
			if(list != null){
				for(Map map:list){
					int isOrigin = Integer.parseInt(map.get("key").toString());
					if(isOrigin == 1){
						if(map.get("doc_count") != null){
							nowWeekesult.put("originalCount", Integer.parseInt(map.get("doc_count").toString()));
						}else{
							nowWeekesult.put("originalCount",0);
						}
					}
					if(isOrigin == 0){
						if(map.get("doc_count") != null){
							nowWeekesult.put("unoriginalCount", Integer.parseInt(map.get("doc_count").toString()));
						}else{
							nowWeekesult.put("unoriginalCount",0);
						}
					}
				}
			}
		}
		
		Map lastWeekesult = new HashMap();
		if(StringUtils.isNotBlank(strLastweek)){
			Map<String, Object> transforMapLastWeek = JsonUtils.parseJSON2Map(strLastweek);
			Map<String, Object> transform1LastWeek = (Map<String, Object>) transforMapLastWeek.get("aggregations");
			Map<String, Object> transform2LastWeek = null;
			if(transform1LastWeek != null){
				transform2LastWeek = (Map<String, Object>) transform1LastWeek.get("isOrigin");
			}
			List<Map> listLast = null;
			if(transform2LastWeek != null){
				listLast = (List<Map>) transform2LastWeek.get("buckets");
			}
			if(listLast != null){
				for(Map map:listLast){
					int isOrigin = Integer.parseInt(map.get("key").toString());
					if(isOrigin == 1){
						if(map.get("doc_count") != null){
							lastWeekesult.put("originalCount", Integer.parseInt(map.get("doc_count").toString()));
						}else{
							lastWeekesult.put("originalCount", 0);
						}
					}
					if(isOrigin == 0){
						if(map.get("doc_count") != null){
							lastWeekesult.put("unoriginalCount", Integer.parseInt(map.get("doc_count").toString()));
						}else{
							lastWeekesult.put("unoriginalCount",0);
						}
					}
				}
			}
		}

		boolean flag,flag2 ;
		WeekedCircle weekedCircle = new WeekedCircle();
		DecimalFormat df = new DecimalFormat("0.00%");
		if(lastWeekesult.get("originalCount") == null ){
			lastWeekesult.put("originalCount", 0);
		}
		if(lastWeekesult.get("unoriginalCount") == null){
			lastWeekesult.put("unoriginalCount", 0);
		}
		if(nowWeekesult.get("originalCount") == null){
			nowWeekesult.put("originalCount", 0);
		}
		if(nowWeekesult.get("unoriginalCount") == null){
			nowWeekesult.put("unoriginalCount", 0);
		}
		
		flag = ((Integer)lastWeekesult.get("originalCount")!= 0);
		flag2 = ((Integer)lastWeekesult.get("unoriginalCount")!= 0);
		String sequential = "-";
		if(flag){
			Double thisWeek = null;
			Double lastWeek = null;		
			thisWeek = ((Integer) (nowWeekesult.get("originalCount"))).doubleValue();
			lastWeek = ((Integer)(lastWeekesult.get("originalCount"))).doubleValue();
			if (lastWeek != 0) {
				double resultweek = (thisWeek - lastWeek) / lastWeek;
				sequential = df.format(resultweek);
			}
			weekedCircle.setSequentialtext((Integer)(nowWeekesult.get("originalCount")) - (Integer)(lastWeekesult.get("originalCount")));
		}else{
			weekedCircle.setSequentialtext(0);
		}	
		weekedCircle.setPrevWeek(lastWeekesult.get("originalCount")!=null ? (Integer)lastWeekesult.get("originalCount"):0);		
		weekedCircle.setTitle("原创文章");
		weekedCircle.setSequential(sequential);
		weekedCircle.setWeek((nowWeekesult.get("originalCount")!=null ? (Integer)nowWeekesult.get("originalCount"):0));
		
		String sequential2 = "-";
		WeekedCircle weekedCircle2 = new WeekedCircle();
		if(flag2){
			double thisWeek2 = ((Integer)nowWeekesult.get("unoriginalCount")).doubleValue();
			double lastWeek2 = ((Integer)lastWeekesult.get("unoriginalCount")).doubleValue();
			if (lastWeek2 != 0) {
				double resultweek2 = (thisWeek2 - lastWeek2)/lastWeek2;
				sequential2 = df.format(resultweek2);
			}
			weekedCircle2.setSequentialtext((Integer)(nowWeekesult.get("unoriginalCount")) - (Integer)(lastWeekesult.get("unoriginalCount")));	
		}else{
			weekedCircle2.setSequentialtext(0);
		}		
		weekedCircle2.setPrevWeek(lastWeekesult.get("unoriginalCount")!= null ?(Integer)(lastWeekesult.get("unoriginalCount")):0);		
		weekedCircle2.setWeek(nowWeekesult.get("unoriginalCount")!= null ?(Integer)(nowWeekesult.get("unoriginalCount")):0);		
		weekedCircle2.setTitle("非原创文章");
		weekedCircle2.setSequential(sequential2);
		resultList.add(weekedCircle);
		resultList.add(weekedCircle2);
		return resultList;
	}
	private List handlerMappingPlatformTrans(String str,boolean isHomepage){
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return null;
		}
		Map<String, Object> transform2 = (Map<String, Object>) transform1.get("platformId");
		if(transform2 == null){
			return null;
		}
		List<Map> transform3 = (List<Map>) transform2.get("buckets");
		if(transform3 == null){
			return null;
		}
		List resultList = new ArrayList<Map>();
		if(isHomepage && transform3.size() > 10){
			transform3 = transform3.subList(0, 10);
		}
		for(Map transform4:transform3){
			Map result = new HashMap();
			String platformId = (String)transform4.get("key");
			if(StringUtils.isNotBlank(platformId)){
				String platformName = this.commonService.getPlatformNameById(platformId);
				platformName = platformName+"_"+getPlatformTypeName(platformId);
				result.put("platformId", platformId);
				result.put("platformName", platformName);
			}
			if(transform4.get("doc_count") != null ){
				int transNum = (Integer)transform4.get("doc_count");
				result.put("transNum", transNum);
			}
			resultList.add(result);
		}
		return resultList;
	}
	private Map handlerMappingAreaTransList(String str,String province,int pageStart){
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return null;
		}
		int count = Integer.parseInt(transform1.get("total").toString());
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null){
			return null;
		}
		List<AreaTransInfo> resultList = new ArrayList<AreaTransInfo>();
		for(Map transform3:transform2){
			Map transform4 = (Map) transform3.get("_source");
			AreaTransInfo entity = new AreaTransInfo();
			entity.setArticleId(transform4.get("articleId").toString());
			entity.setUnionId(transform4.get("unionId").toString());
			if(transform4.get("originTitle") != null){
				entity.setOriginalTitle(transform4.get("originTitle").toString());
			}
			String platformId = null;
			if(transform4.get("platformId") != null){
				platformId = transform4.get("platformId").toString();
				entity.setPlatformId(platformId);
			}
			if(transform4.get("platformName") != null){
				String platformName = transform4.get("platformName").toString();
				platformName = platformName+"_"+getPlatformTypeName(platformId);
				entity.setPlatformName(platformName);
			}
			entity.setProvince(province);
			int rowNum = ++pageStart;
			entity.setRowNum(rowNum);
			if(transform4.get("crawlSource") != null){
				entity.setTransMeida(transform4.get("crawlSource").toString());
			}
			if(transform4.get("reportTime") != null){
				Long timeStamp = (Long)transform4.get("reportTime");
				Date transTime = new Date(timeStamp);
				entity.setTransTime(transTime);
			}
			if(transform4.get("title") != null){
				entity.setTransTitle(transform4.get("title").toString());
			}
			if(transform4.get("webpageUrl") != null){
				entity.setUrl(transform4.get("webpageUrl").toString());
			}
			resultList.add(entity);
		}
		Map resultMap = new HashMap();
		resultMap.put("resultList", resultList);
		resultMap.put("count", count);
		return resultMap;
	}
	private List handlerMappingPlatformArticleNumAccounted(String str,String platformTypeId,List<String> platformIdList){
		List<Map> resultList = new ArrayList<Map>();
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return null;
		}
			if(StringUtils.isBlank(platformTypeId) && platformIdList == null){
				Map<String, Object> transform2 = (Map<String, Object>) transform1.get("platformTypeId");
				if(transform2 == null){
					return null;
				}
				List<Map> list = (List<Map>) transform2.get("buckets");
				if(list == null){
					return null;
				}
				for (Map entity : list) {
					if(entity.get("key") == null || entity.get("doc_count") == null){
						continue;
					}
					String typeId = null;
					String typeName = null;
					typeId = (String)entity.get("key");
					typeName = this.commonService.getPlatformTypeNameById(typeId);
					Map result = new HashMap();
					result.put("platformTypeId", typeId);
					result.put("platformTypeName", typeName);
					result.put("count", Integer.parseInt(entity.get("doc_count").toString()));
					resultList.add(result);
				}
				}else{
					Map<String, Object> transform2 = (Map<String, Object>) transform1.get("platformId");
					if(transform2 == null){
						return null;
					}
					List<Map> list = (List<Map>) transform2.get("buckets");
					if(list == null){
						return null;
					}
						int i = 0;
						int max = list.size();
						int count = 0;
				for (Map entity : list) {
					i++;
					if (i <= 5) {
						String platformId = new String();
						String platformName = new String();
						if(entity.get("key") != null && entity.get("doc_count") != null){
							platformId = entity.get("key").toString();
							platformName = this.commonService.getPlatformNameById(platformId);
							Map result = new HashMap();
							result.put("platformId", platformId);
							result.put("platformName", platformName);
							result.put("count", Integer.parseInt(entity.get("doc_count").toString()));
							resultList.add(result);
						}
					}
					if (i > 5) {
						if(entity.get("doc_count") != null){
							count = count + Integer.parseInt(entity.get("doc_count").toString());
						}
					}
					if (i > 5 && i == max) {
						Map other = new HashMap();
						other.put("count",count);
						other.put("platformName","其他");
						resultList.add(other);
					}
				}
			}
		return resultList;
	}
	private Map handlerMappingOriginalArticleNum(String str){
		if(StringUtils.isBlank(str)){
			return null;
		}
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return null;
		}
		Map<String, Object> transform2 = (Map<String, Object>) transform1.get("isOrigin");
		if(transform2 == null){
			return null;
		}
		List<Map> list = (List<Map>) transform2.get("buckets");
		if(list == null){
			return null;
		}
		Map resultMap = new HashMap();
		for(Map map:list){
			int isOrigin = Integer.parseInt(map.get("key").toString());
			if(isOrigin == 1){
				resultMap.put("originalCount", Integer.parseInt(map.get("doc_count").toString()));
			}
			if(isOrigin == 0){
				resultMap.put("unoriginalCount", Integer.parseInt(map.get("doc_count").toString()));
			}
		}
		return resultMap;
	}
	private List handlerMappingAreaCurrent(String str){
		List<Map> resultList = new ArrayList<Map>();
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return null;
		}
		Map<String, Object> transform2 = (Map<String, Object>) transform1.get("province");
		if(transform2 == null){
			return null;
		}
		List<Map> list = (List<Map>) transform2.get("buckets");
		if(list == null){
			return null;
		}
/*		private String province;
		private Integer transNum;*/
		for(Map map:list){
			Map resultMap = new HashMap();
			String province =  map.get("key").toString();
			if("广西壮族自治区".endsWith(province)){
				province = "广西";
			}
			resultMap.put("province", province);
			resultMap.put("transNum", Integer.parseInt(map.get("doc_count").toString()));
			resultList.add(resultMap);
		}
		return resultList;
	}
	private Integer handlerMapping(String str,String province){
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return null;
		}
		Map<String, Object> transform2 = (Map<String, Object>) transform1.get("province");
		if(transform2 == null){
			return null;
		}
		List<Map> list = (List<Map>) transform2.get("buckets");
		if(list == null){
			return null;
		}
		for(Map map:list){
			if(province.equalsIgnoreCase(map.get("key").toString())){
				return Integer.parseInt(map.get("doc_count").toString());
			}
		}
		return null;
	}
}
