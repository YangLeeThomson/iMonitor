package com.ronglian.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import com.ronglian.mapper.ArticleInfoHourMapper;
import com.ronglian.model.ArticleInfoHour;
import com.ronglian.model.MediaOrderElement;
import com.ronglian.model.SpreadTimeTrendElement;
import com.ronglian.repository.HomePageRepository;
import com.ronglian.repository.MediaTransAnalysisRepository;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.PlatformService;
import com.ronglian.service.SpreadTimeTrendService;
import com.ronglian.utils.CalendarUtil;
import com.ronglian.utils.Utils;


@Service("spreadTimeTrendService")
public class SpreadTimeTrendServiceImpl implements SpreadTimeTrendService{
	
	@Autowired
	HomePageRepository homePageRepository;
	
	@Autowired
	MediaTransAnalysisRepository mediaTransAnalysisRepository;
	
	@Autowired
	PlatformService platformService;

	@Autowired
	CustomGroupService customGroupService; 
	
	@Autowired
	ArticleInfoHourMapper articleInfoHourMapper;
	
	//传播时间趋势接口实现
	@Override
	public List<SpreadTimeTrendElement> getSpreadTimeTend(String platformTypeId,String platformId,String groupId, Date startTime, Integer accountType) {
		List<SpreadTimeTrendElement> result=new ArrayList<SpreadTimeTrendElement>();
		
		if(accountType!=0) {//年月周查询，直接查询es
			String esRst=null;
			List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
			Date endTime=getEndtimeByStarttimeAccounttype(startTime,accountType);
			try {
				esRst=homePageRepository.spreadDateTrend(accountType, platformTypeId, platformIdList, startTime, endTime);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(JSON.parseObject(esRst).getJSONObject("aggregations")==null || JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime")==null
					|| JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets")==null) {
				return null;
			}
			JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets");
			JSONObject dateReadCountMap = new JSONObject();
			for(int i=0;i<esJson.size();i++) {
				JSONObject element=esJson.getJSONObject(i);
				JSONObject timeTrend=new JSONObject();
				String dateKey = Utils.dateToString(element.getDate("key"),Constants.DEFAULT_DATE_FORMAT_YMD);
				timeTrend.put("create_time",dateKey);
				timeTrend.put("read_num",element.getJSONObject("clickNum").getIntValue("value"));
				timeTrend.put("comment_num",element.getJSONObject("commentNum").getIntValue("value"));
				timeTrend.put("trans_num",element.getJSONObject("transNum").getIntValue("value"));
				timeTrend.put("comprehensive_num",element.getJSONObject("comprehensive").getFloatValue("value"));
				dateReadCountMap.put(dateKey,timeTrend);
			}
		    List<String> dateList=new ArrayList<String>();
			try {
				dateList = Utils.getDatesBetweenTwoDate(Utils.dateToString(startTime, Constants.DEFAULT_DATE_FORMAT_YMD), Utils.dateToString(Utils.plusDay(-1, endTime), Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			for(int i=0;i<dateList.size();i++) {
				SpreadTimeTrendElement element =new SpreadTimeTrendElement();
				String date=dateList.get(i);
				element.setCreate_time(date);
				int read_num=0; //阅读数
				int trans_num=0; //转发数
				int comment_num=0;//评论数
				if(dateReadCountMap.getJSONObject(date)!=null) {
					comment_num=dateReadCountMap.getJSONObject(date).getIntValue("comment_num");
					read_num=dateReadCountMap.getJSONObject(date).getIntValue("read_num");
					trans_num=dateReadCountMap.getJSONObject(date).getIntValue("trans_num");
				}
				element.setRead_num(read_num);
				element.setComment_num(comment_num);
				element.setTrans_num(trans_num);
				result.add(element);
			}
		}else {//天，查询mysql
			List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
			String esRst=null;
			JSONObject dateReadCountMap = new JSONObject();
			try {
				esRst=homePageRepository.spreadDateTrendOneDay(accountType, platformTypeId, platformIdList, startTime, Utils.plusDay(1, startTime));
				if(JSON.parseObject(esRst).getJSONObject("aggregations")!=null && JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime")!=null &&
						JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets")!=null) {
					JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets");
					for(int i=0;i<esJson.size();i++) {
						JSONObject element=esJson.getJSONObject(i);
						dateReadCountMap.put(element.getString("key_as_string"), element);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i=1;i<25;i++) {
				SpreadTimeTrendElement element =new SpreadTimeTrendElement();
				String hour=i+"";
				if(i<10) {
					hour="0"+hour;
				}
				element.setCreate_time(hour+"时");
				int read_num=0; //阅读数
				int trans_num=0; //转发数
				int comment_num=0;//评论数
				if(dateReadCountMap.getJSONObject(hour)!=null) {
					read_num=dateReadCountMap.getJSONObject(hour).getJSONObject("clickNum").getIntValue("value");
					comment_num=dateReadCountMap.getJSONObject(hour).getJSONObject("commentNum").getIntValue("value");
					trans_num = dateReadCountMap.getJSONObject(hour).getJSONObject("transNum").getIntValue("value");
				}
				element.setRead_num(read_num);
				element.setComment_num(comment_num);
				element.setTrans_num(trans_num);
				result.add(element);
			}
			
		}
//备份勿删
//		@Override
//		public List<SpreadTimeTrendElement> getSpreadTimeTend(String platformTypeId,String platformId,String groupId, Date startTime, Integer accountType) {
//			List<SpreadTimeTrendElement> result=new ArrayList<SpreadTimeTrendElement>();
//			
//			if(accountType!=0) {//年月周查询，直接查询es
//				String esRst=null;
//				String esRstTransCount = null;
//				List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
//				Date endTime=getEndtimeByStarttimeAccounttype(startTime,accountType);
//				try {
//					esRst=homePageRepository.spreadDateTrend(accountType, platformTypeId, platformIdList, startTime, endTime);
//					esRstTransCount = homePageRepository.spreadDateTrendTransCount(accountType, platformTypeId, platformIdList, startTime, endTime);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets");
//				JSONArray esTransCountJson=JSON.parseObject(esRstTransCount).getJSONObject("aggregations").getJSONObject("originArticlePubTime").getJSONArray("buckets");
//				JSONObject dateTransCountMap = new JSONObject();
//				for(int i=0;i<esTransCountJson.size();i++) {
//					JSONObject element = esTransCountJson.getJSONObject(i);
//					dateTransCountMap.put(element.getString("key_as_string"), element.getIntValue("doc_count"));
//				}
//				JSONObject dateReadCountMap = new JSONObject();
//				for(int i=0;i<esJson.size();i++) {
//					JSONObject element=esJson.getJSONObject(i);
//					JSONObject timeTrend=new JSONObject();
//					String dateKey = Utils.dateToString(element.getDate("key"),Constants.DEFAULT_DATE_FORMAT_YMD);
//					timeTrend.put("create_time",dateKey);
//					timeTrend.put("read_num",element.getJSONObject("clickNum").getIntValue("value"));
//					timeTrend.put("comment_num",element.getJSONObject("commentNum").getIntValue("value"));
//					timeTrend.put("trans_num",dateTransCountMap.getIntValue(dateKey));
//					timeTrend.put("comprehensive_num",element.getJSONObject("comprehensive").getFloatValue("value"));
//					dateReadCountMap.put(dateKey,timeTrend);
//				}
//				List<String> dateList=new ArrayList<String>();
//				try {
//					dateList = Utils.getDatesBetweenTwoDate(Utils.dateToString(startTime, Constants.DEFAULT_DATE_FORMAT_YMD), Utils.dateToString(Utils.plusDay(-1, endTime), Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD);
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				for(int i=0;i<dateList.size();i++) {
//					SpreadTimeTrendElement element =new SpreadTimeTrendElement();
//					String date=dateList.get(i);
//					element.setCreate_time(date);
//					int read_num=0; //阅读数
//					int trans_num=0; //转发数
//					int comment_num=0;//评论数
//					if(dateTransCountMap.get(date)!=null) {
//						trans_num=dateTransCountMap.getIntValue(date);
//					}
//					if(dateReadCountMap.getJSONObject(date)!=null) {
//						comment_num=dateReadCountMap.getJSONObject(date).getIntValue("comment_num");
//						read_num=dateReadCountMap.getJSONObject(date).getIntValue("read_num");
//					}
//					element.setRead_num(read_num);
//					element.setComment_num(comment_num);
//					element.setTrans_num(trans_num);
//					result.add(element);
//				}
//			}else {//天，查询mysql
//				List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
//				String esRst=null;
//				String transCount = null;
//				JSONObject dateTransCountMap = new JSONObject();
//				JSONObject dateReadCountMap = new JSONObject();
//				try {
//					esRst=homePageRepository.spreadDateTrendOneDay(accountType, platformTypeId, platformIdList, startTime, Utils.plusDay(1, startTime));
//					transCount = homePageRepository.spreadDateTrendTransCountOneDay(accountType, platformTypeId, platformIdList, startTime, Utils.plusDay(1,startTime));
//					if(JSON.parseObject(transCount).getJSONObject("aggregations")!=null) {
//						JSONArray esTransCountJson=JSON.parseObject(transCount).getJSONObject("aggregations").getJSONObject("originArticlePubTime").getJSONArray("buckets");
//						for(int i=0;i<esTransCountJson.size();i++) {
//							JSONObject element = esTransCountJson.getJSONObject(i);
//							dateTransCountMap.put(element.getString("key_as_string"), element.getIntValue("doc_count"));
//						}
//					}
//					if(JSON.parseObject(esRst).getJSONObject("aggregations")!=null) {
//						JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("publishTime").getJSONArray("buckets");
//						for(int i=0;i<esJson.size();i++) {
//							JSONObject element=esJson.getJSONObject(i);
//							dateReadCountMap.put(element.getString("key_as_string"), element);
//						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				for(int i=1;i<25;i++) {
//					SpreadTimeTrendElement element =new SpreadTimeTrendElement();
//					String hour=i+"";
//					if(i<10) {
//						hour="0"+hour;
//					}
//					element.setCreate_time(hour+"时");
//					int read_num=0; //阅读数
//					int trans_num=0; //转发数
//					int comment_num=0;//评论数
//					if(dateReadCountMap.getJSONObject(hour)!=null) {
//						read_num=dateReadCountMap.getJSONObject(hour).getJSONObject("clickNum").getIntValue("value");
//						comment_num=dateReadCountMap.getJSONObject(hour).getJSONObject("commentNum").getIntValue("value");
//					}
//					if(dateTransCountMap.get(hour)!=null) {
//						trans_num=dateTransCountMap.getIntValue(hour);
//					}
//					element.setRead_num(read_num);
//					element.setComment_num(comment_num);
//					element.setTrans_num(trans_num);
//					result.add(element);
//				}
//				
//			}
//		}
		
		
		return result;
	}

	//媒体转载排行接口实现
	@Override
	public Map<String,Object> getMediaOrder(String platformTypeId, String platformId,String groupId, Date startTime,Integer accountType) {
		Map<String,Object> rst=new HashMap<String,Object>();
		List<MediaOrderElement> result=new ArrayList<MediaOrderElement>();
		int mediaCover=0;
		
		String esRst=null;
		List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
		Date endTime=getEndtimeByStarttimeAccounttype(startTime,accountType);
		try {
			esRst=homePageRepository.TransMediaBang(accountType, platformTypeId, platformIdList, startTime, endTime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(JSON.parseObject(esRst).getJSONObject("aggregations")==null ||JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource")==null
				|| JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource").getJSONArray("buckets")==null) {
			return null;//判空
		}
		JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource").getJSONArray("buckets");
		mediaCover=esJson.size();
		for(int i=0;i<esJson.size() && i<10;i++) {
			JSONObject element=esJson.getJSONObject(i);
			MediaOrderElement mediaOrder=new MediaOrderElement();
			mediaOrder.setMedia_name(element.getString("key"));
			mediaOrder.setTrans_num(element.getIntValue("doc_count"));
			result.add(mediaOrder);
		}
		
		rst.put("mediaList", result);
		rst.put("mediaCover", mediaCover);
		return rst;
	}
	
	//转载媒体分析，媒体排行接口，多参数版
	@Override
	public Map<String,Object> getMediaOrderSuper(String platformTypeId, String platformId, String groupId, Date startTime,
			int accountType, Integer mediaType, Integer channel, int pageNo, int pageSize) {
		Map<String,Object> rst=new HashMap<String,Object>();
		List<MediaOrderElement> result=new ArrayList<MediaOrderElement>();
		int mediaCover=0;
		
		String esRst=null;
		List<String> platformIdList= getPlatformList(platformTypeId, platformId, groupId);
		Date endTime=getEndtimeByStarttimeAccounttype(startTime,accountType);
		try {
			esRst=mediaTransAnalysisRepository.TransMediaBangSuper(mediaType, channel, accountType, platformTypeId, platformIdList, startTime, endTime,pageNo,pageSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(JSON.parseObject(esRst).getJSONObject("aggregations")==null ||JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource")==null
				|| JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource").getJSONArray("buckets")==null) {
			return null;//判空
		}
		JSONArray esJson=JSON.parseObject(esRst).getJSONObject("aggregations").getJSONObject("crawlSource").getJSONArray("buckets");
		mediaCover=esJson.size();
		for(int i=(pageNo-1)*pageSize;i<pageNo*pageSize && i<esJson.size();i++) {
			JSONObject element=esJson.getJSONObject(i);
			MediaOrderElement mediaOrder=new MediaOrderElement();
			mediaOrder.setMedia_name(element.getString("key"));
			mediaOrder.setTrans_num(element.getIntValue("doc_count"));
			mediaOrder.setOrder(i+1);
			result.add(mediaOrder);
		}
		
		rst.put("mediaList", result);
		rst.put("mediaCover", mediaCover);
		
		rst.put("pageNo", pageNo);
		rst.put("pageSize", pageSize);
		int totalCount=mediaCover;
		rst.put("totalPage", Utils.totalPage(totalCount, pageSize));
		rst.put("totalCount", totalCount);
		return rst;
	}
	
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
