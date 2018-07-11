package com.ronglian.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.Constants;
import com.ronglian.model.ComprehensiveNumTop;
import com.ronglian.model.PlatformTransPeriod;
import com.ronglian.model.SpreadTimeTrendElement;
import com.ronglian.repository.HotArticleRepository;
import com.ronglian.repository.MediaTransAnalysisRepository;
import com.ronglian.repository.SpreadTrendAnalysisRepository;
import com.ronglian.service.CopyrightMonitorService;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.SpreadTimeTrendService;
//import com.ronglian.mapper.ComprehensiveNumMapper;
//import com.ronglian.mapper.TransPeriodMapper;
//import com.ronglian.model.ComprehensiveNumTop;
//import com.ronglian.model.PlatformTransPeriod;
import com.ronglian.service.TransPeriodService;
import com.ronglian.utils.CalendarUtil;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("transPeriodService")
public class TransPeriodServiceImpl implements TransPeriodService{
	
	@Autowired
	CustomGroupService customGroupService;
	
	@Autowired
	SpreadTrendAnalysisRepository spreadTrendAnalysisRepository;
	
	@Autowired
	MediaTransAnalysisRepository mediaTransAnalysisRepository;
	
	@Autowired
	SpreadTimeTrendService spreadTimeTrendService;
	
	@Autowired
	HotArticleRepository hotArticleRepository;
	
	@Autowired
	CopyrightMonitorService copyrightMonitorService;
	

	public List<PlatformTransPeriod> getTransPeriod(String platformTypeId, String platformId,String groupId, Date startTime, Integer accountType) {

		log.info("【进入】Service_getTransPeriod： "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		
		List<PlatformTransPeriod> result=new ArrayList<PlatformTransPeriod>();
		List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
		Date endTime=getEndtimeByStarttimeAccounttype(startTime,accountType);
		String esRst=null;
		try {
			esRst=mediaTransAnalysisRepository.getTransPeriod(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PlatformTransPeriod period0_1=new PlatformTransPeriod();
		period0_1.setPeriod_name("1天");
		period0_1.setPeriod_type(1);
		PlatformTransPeriod period2_3=new PlatformTransPeriod();
		period2_3.setPeriod_name("2-3天");
		period2_3.setPeriod_type(3);
		PlatformTransPeriod period4_7=new PlatformTransPeriod();
		period4_7.setPeriod_name("4-7天");
		period4_7.setPeriod_type(7);
		PlatformTransPeriod period8_14=new PlatformTransPeriod();
		period8_14.setPeriod_name("8-14天");
		period8_14.setPeriod_type(14);
		
		if(JSON.parseObject(esRst).getJSONObject("aggregations")==null ||JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("originArticlePubTime")==null
				|| JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("originArticlePubTime").getJSONArray("buckets")==null) {
			return null;//判空
		}
		JSONArray publishTimeArray=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("originArticlePubTime").getJSONArray("buckets");
		for(int i=0;i<publishTimeArray.size();i++) {
			long publishTime=publishTimeArray.getJSONObject(i).getDate("key").getTime();
			JSONArray reportTimeArray=publishTimeArray.getJSONObject(i).getJSONObject("reportTime").getJSONArray("buckets");
			for(int j=0;j<reportTimeArray.size();j++) {
				long transTime=reportTimeArray.getJSONObject(j).getDate("key").getTime();
				int transNum=reportTimeArray.getJSONObject(j).getInteger("doc_count");
				if(transTime-publishTime==0) {
					int num=period0_1.getTrans_num()+transNum;
					period0_1.setTrans_num(num);
				}else if(transTime-publishTime>=1000*60*60*24 && transTime-publishTime<1000*60*60*24*3) {
					int num=period2_3.getTrans_num()+transNum;
					period2_3.setTrans_num(num);
				}else if(transTime-publishTime>=1000*60*60*24*3 && transTime-publishTime<1000*60*60*24*7) {
					int num=period4_7.getTrans_num()+transNum;
					period4_7.setTrans_num(num);
				}else if(transTime-publishTime>=1000*60*60*24*7 && transTime-publishTime<1000*60*60*24*14) {
					int num=period8_14.getTrans_num()+transNum;
					period8_14.setTrans_num(num);
				}
			}
		}
		result.add(period0_1);
		result.add(period2_3);
		result.add(period4_7);
		result.add(period8_14);
		log.info("【结束】Service_getTransPeriod： "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		return result;
	}

	
	//综合数值排行top10接口逻辑
	@Override
	public Map<String,Object> getComprehensiveNum(String platformTypeId, String platformId,String groupId, Date startTime,
			Integer accountType,int orderCode,int pageNo,int pageSize) {
		String order="transNum";
		if(orderCode==1)
			order="transNum";
		if(orderCode==2)
			order="commentNum";
		if(orderCode==3)
			order="clickNum";
		Map<String,Object> result=new HashMap<String,Object>();
		List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
		Date topDate=startTime;
		int topTime=1;
		long start=0L;
		long end=0L;
		if(accountType != 0) {
			List<SpreadTimeTrendElement> topDateList=spreadTimeTrendService.getSpreadTimeTend(platformTypeId, platformId, groupId, startTime, accountType);
			if (topDateList!=null && topDateList.size()>0) {
				topDate=Utils.stringToDate(topDateList.get(0).getCreate_time(), Constants.DEFAULT_DATE_FORMAT_YMD);
				int topNum = topDateList.get(0).getTrans_num();
				for (SpreadTimeTrendElement spreadTimeTrendElement : topDateList) {
					if (spreadTimeTrendElement.getTrans_num() > topNum) {
						topNum = spreadTimeTrendElement.getTrans_num();
						topDate = Utils.stringToDate(spreadTimeTrendElement.getCreate_time(),
								Constants.DEFAULT_DATE_FORMAT_YMD);
					}
				} 
			}
			start=topDate.getTime();
			end=Utils.plusDay(1, topDate).getTime();
		}else {
			List<SpreadTimeTrendElement> topDateList=spreadTimeTrendService.getSpreadTimeTend(platformTypeId, platformId, groupId, startTime, accountType);
			if (topDateList!=null && topDateList.size()>0) {
				topTime = Integer.parseInt(topDateList.get(0).getCreate_time().replace("时", ""));
				int topNum = topDateList.get(0).getTrans_num();
				for (SpreadTimeTrendElement spreadTimeTrendElement : topDateList) {
					if (spreadTimeTrendElement.getTrans_num() > topNum) {
						topNum = spreadTimeTrendElement.getTrans_num();
						topTime = Integer.parseInt(spreadTimeTrendElement.getCreate_time().replaceAll("时", ""));
					}
				} 
			}
			start=topDate.getTime()+topTime*60*60*1000;
			end=topDate.getTime()+(topTime+1)*60*60*1000;
		}
		List<ComprehensiveNumTop> topArticle=new ArrayList<ComprehensiveNumTop>();
//		if(orderCode==1) {
//			String esRst=null;
//			try {
//				esRst=spreadTrendAnalysisRepository.spreadTimeTrendOneDayTop10TransNum(platformTypeId, platformIdList, topDate, start,end,order, pageNo, pageSize);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			JSONArray esJson=new JSONArray();
//			if(JSON.parseObject(esRst).getJSONObject("aggregations")!=null) {
//				esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("unionId").getJSONArray("buckets");
//			}
//			for(int i=0;i<esJson.size();i++) {
//				JSONObject element=esJson.getJSONObject(i);
//				ComprehensiveNumTop numTop=new ComprehensiveNumTop();
//				
//				String unionId=element.getString("key");
//				int transNum=element.getIntValue("doc_count");//转载数单独查询
//				numTop.setTrans_num(transNum);
//				
//				Article article=copyrightMonitorService.getOriginalArticle(unionId);
//				
//				numTop.setTitle(article.getTitle());
//				numTop.setRead_num(article.getClickNum());
//				numTop.setComment_num(article.getCommentNum());
//				numTop.setPublish_time(article.getPublishTime());
//				numTop.setPlatform(article.getPlatformName()+"_"+article.getPlatformTypeName());
//				numTop.setArticleId(article.getArticleId());
//				numTop.setUnionId(article.getUnionId());
//				topArticle.add(numTop);
//			}
//		}else {
		String esRst=null;
		try {
			esRst=spreadTrendAnalysisRepository.spreadTimeTrendOneDayTop10(platformTypeId, platformIdList, topDate, start, end, order, pageNo, pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(JSON.parseObject(esRst).getJSONObject("hits")==null || JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits")==null) {
			return null;//判空
		}
		JSONArray esJson=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
		for(int i=0;i<esJson.size();i++) {
			JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
			ComprehensiveNumTop numTop=new ComprehensiveNumTop();
			numTop.setTitle(element.getString("title"));
			numTop.setRead_num(element.getIntValue("clickNum"));
			numTop.setComment_num(element.getIntValue("commentNum"));
			numTop.setTrans_num(element.getIntValue("transNum"));
			numTop.setPublish_time(element.getDate("publishTime"));
			numTop.setPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
			numTop.setArticleId(element.getString("articleId"));
			numTop.setUnionId(element.getString("unionId"));
			topArticle.add(numTop);
		}
		String topDateString=null;
		if(accountType != 0)
			topDateString=Utils.dateToString(topDate,Constants.DEFAULT_DATE_FORMAT_YMD);
		else
			topDateString=String.valueOf(topTime)+"时";
		result.put("top_date", topDateString);
		result.put("articles", topArticle);
		log.info("【结束】Service_api_getComprehensiveNum： "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		return result;
		
	}
//备份勿删
//	@Override
//	public Map<String,Object> getComprehensiveNum(String platformTypeId, String platformId,String groupId, Date startTime,
//			Integer accountType,int orderCode,int pageNo,int pageSize) {
//		String order="transNum";
//		if(orderCode==1)
//			order="transNum";
//		if(orderCode==2)
//			order="commentNum";
//		if(orderCode==3)
//			order="clickNum";
//		Map<String,Object> result=new HashMap<String,Object>();
//		List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
//		Date topDate=startTime;
//		int topTime=1;
//		long start=0L;
//		long end=0L;
//		if(accountType != 0) {
//			List<SpreadTimeTrendElement> topDateList=spreadTimeTrendService.getSpreadTimeTend(platformTypeId, platformId, groupId, startTime, accountType);
//			topDate=Utils.stringToDate(topDateList.get(0).getCreate_time(), Constants.DEFAULT_DATE_FORMAT_YMD);
//			int topNum=topDateList.get(0).getTrans_num();
//			for (SpreadTimeTrendElement spreadTimeTrendElement : topDateList) {
//				if(spreadTimeTrendElement.getTrans_num()>topNum) {
//					topNum=spreadTimeTrendElement.getTrans_num();
//					topDate=Utils.stringToDate(spreadTimeTrendElement.getCreate_time(),Constants.DEFAULT_DATE_FORMAT_YMD);
//				}
//			}
//			start=topDate.getTime();
//			end=Utils.plusDay(1, topDate).getTime();
//		}else {
//			List<SpreadTimeTrendElement> topDateList=spreadTimeTrendService.getSpreadTimeTend(platformTypeId, platformId, groupId, startTime, accountType);
//			topTime=Integer.parseInt(topDateList.get(0).getCreate_time().replace("时", ""));
//			int topNum=topDateList.get(0).getTrans_num();
//			for (SpreadTimeTrendElement spreadTimeTrendElement : topDateList) {
//				if(spreadTimeTrendElement.getTrans_num()>topNum) {
//					topNum=spreadTimeTrendElement.getTrans_num();
//					topTime=Integer.parseInt(spreadTimeTrendElement.getCreate_time().replaceAll("时", ""));
//				}
//			}
//			start=topDate.getTime()+topTime*60*60*1000;
//			end=topDate.getTime()+(topTime+1)*60*60*1000;
//		}
//		List<ComprehensiveNumTop> topArticle=new ArrayList<ComprehensiveNumTop>();
//		if(orderCode==1) {
//			String esRst=null;
//			try {
//				esRst=spreadTrendAnalysisRepository.spreadTimeTrendOneDayTop10TransNum(platformTypeId, platformIdList, topDate, start,end,order, pageNo, pageSize);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			JSONArray esJson=new JSONArray();
//			if(JSON.parseObject(esRst).getJSONObject("aggregations")!=null) {
//				esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("unionId").getJSONArray("buckets");
//			}
//			for(int i=0;i<esJson.size();i++) {
//				JSONObject element=esJson.getJSONObject(i);
//				ComprehensiveNumTop numTop=new ComprehensiveNumTop();
//				
//				String unionId=element.getString("key");
//				int transNum=element.getIntValue("doc_count");//转载数单独查询
//				numTop.setTrans_num(transNum);
//				
//				Article article=copyrightMonitorService.getOriginalArticle(unionId);
//				
//				numTop.setTitle(article.getTitle());
//				numTop.setRead_num(article.getClickNum());
//				numTop.setComment_num(article.getCommentNum());
//				numTop.setPublish_time(article.getPublishTime());
//				numTop.setPlatform(article.getPlatformName()+"_"+article.getPlatformTypeName());
//				numTop.setArticleId(article.getArticleId());
//				numTop.setUnionId(article.getUnionId());
//				topArticle.add(numTop);
//			}
//		}else {
//			String esRst=null;
//			try {
//				esRst=spreadTrendAnalysisRepository.spreadTimeTrendOneDayTop10(platformTypeId, platformIdList, topDate, start, end, order, pageNo, pageSize);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			JSONArray esJson=JSON.parseObject(esRst).getJSONObject("hits").getJSONArray("hits");
//			for(int i=0;i<esJson.size();i++) {
//				JSONObject element=esJson.getJSONObject(i).getJSONObject("_source");
//				ComprehensiveNumTop numTop=new ComprehensiveNumTop();
//				numTop.setTitle(element.getString("title"));
//				numTop.setRead_num(element.getIntValue("clickNum"));
//				numTop.setComment_num(element.getIntValue("commentNum"));
//				
//				int transNum=hotArticleRepository.getTransNum(element.getString("unionId"));//转载数单独查询
//				numTop.setTrans_num(transNum);
//				numTop.setPublish_time(element.getDate("publishTime"));
//				numTop.setPlatform(element.getString("platformName")+"_"+element.getString("platformTypeName"));
//				numTop.setArticleId(element.getString("articleId"));
//				numTop.setUnionId(element.getString("unionId"));
//				topArticle.add(numTop);
//			}
//		}
//		String topDateString=null;
//		if(accountType != 0)
//			topDateString=Utils.dateToString(topDate,Constants.DEFAULT_DATE_FORMAT_YMD);
//		else
//			topDateString=String.valueOf(topTime)+"时";
//		result.put("top_date", topDateString);
//		result.put("articles", topArticle);
//		return result;
//	}
	

	/**
	 * 
	 * @Description: 传入platformTypeId、platformId、groupId得到平台id列表
	 * @param @param platformTypeId
	 * @param @param platformId
	 * @param @param groupId
	 * @param @return    参数  
	 * @return List<String>    返回类型  
	 * @throws
	 */
	public List<String> getPlatformList(String platformTypeId, String platformId, String groupId) {
		
		List<String> platformIdList=null;
		if(StringUtils.isBlank(platformTypeId) && StringUtils.isBlank(platformId) && StringUtils.isBlank(groupId))
			//都为空，查询所有平台,数据层沟通过传null
			//platformIdList =platformService.findByPlatformTypeId(platformTypeId);
		if(StringUtils.isNotBlank(platformTypeId)) {
			//根据platformTypeId查询
			//platformIdList =platformService.findByPlatformTypeId(platformTypeId);
		}
		if(StringUtils.isNotBlank(platformId)) {
			//根据platformId查询
			platformIdList=new ArrayList<String>();
			platformIdList.add(platformId);
		}
		if(StringUtils.isNotBlank(groupId)) {
			//根据groupId查询
			platformIdList =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));
		}
		return platformIdList;
	}

	/**
	 * 
	 * @Description: 根据开始时间startTime、周年月类型accountType得到结束时间
	 * @param @param startTime 开始时间
	 * @param @param accountType 周年月天类型
	 * @param @return    参数  结束时间
	 * @return Date    返回类型  
	 * @throws
	 */
	private Date getEndtimeByStarttimeAccounttype(Date startTime, Integer accountType) {
		switch (accountType)
		{
			case 0:{//天
				Date endTime =CalendarUtil.plusDay(1,startTime);
				return endTime;
			}
		    case 1:{//年
		    	Date endTime =  CalendarUtil.getNextNewYearsDay(startTime);
		    	return endTime;
		    }
		    case 2:{//月
		    	Calendar cal = Calendar.getInstance();
				cal.setTime(startTime);
				int month = cal.get(Calendar.MONTH)+1;
				int year= cal.get(Calendar.YEAR);
				Date endTime =  CalendarUtil.getFirstAndLastdayOfMonth(year, month).get(1);
				endTime=CalendarUtil.plusDay(1,endTime);
		    	return endTime;
		    }
		    case 3:{//周
		    	Date endTime =CalendarUtil.plusDay(7,startTime);
		    	return endTime;
		    }
		    default:{
		    	return null;
		    }
		}
	}

}
