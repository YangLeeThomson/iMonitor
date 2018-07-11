/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午5:19:48 
 */
package com.ronglian.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.mapper.CommonMapper;
import com.ronglian.mapper.PlatformComparedMapper;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformComparedGroup;
import com.ronglian.repository.HomePageRepository;
import com.ronglian.repository.PlatformCompareAnalysisRepository;
import com.ronglian.service.CommonService;
import com.ronglian.service.PlatformComparedService;
import com.ronglian.utils.CalendarUtil;
import com.ronglian.utils.JsonUtils;

 /** 
 * @ClassName: PlatformComparedServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午5:19:48  
 */
@Service
@Transactional
public class PlatformComparedServiceImpl implements PlatformComparedService {

	/* (non-Javadoc)
	 * @see com.ronglian.service.PlatformComparedService#addPlatformGroup(java.util.Map)
	 */
	@Autowired
	private PlatformComparedMapper groupMapper;
	
	@Autowired
	private CommonService commonService;
	
//	@Autowired
//	private HomePageRepository homePageRepository;
	
	@Autowired
	private PlatformCompareAnalysisRepository platformCompareAnalysisRepository;
	
	@Override
	public JsonResult addPlatformGroup(Map map) {
		// TODO Auto-generated method stub
		
		String userId = (String) map.get("userId");
		this.groupMapper.deletePlatformGroupCompareByUserId(userId);
		List<String> platformIdList = (List<String>) map.get("platformIdList");
		String groupId = (String) map.get("groupId");
		Date createTime = new Date();
		for(String platformId:platformIdList){
			PlatformComparedGroup platformGroup = new PlatformComparedGroup();
			platformGroup.setGroupId(groupId);
			platformGroup.setCreateTime(createTime);
			platformGroup.setUserId(userId);
			platformGroup.setPlatformId(platformId);
			this.groupMapper.insertPlatformGroup(platformGroup);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",groupId);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.PlatformComparedService#deletePlatformGroupCompare(java.lang.String)
	 */
	@Override
	public JsonResult deletePlatformGroupCompare(String groupId) {
		// TODO Auto-generated method stub
		int num = this.groupMapper.deletePlatformGroupCompare(groupId);
		return new JsonResult(ResultCode.SUCCESS,"success",num);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.PlatformComparedService#searchPlatformGroupCompare(java.lang.String)
	 */
	@Override
	public JsonResult searchPlatformGroupCompare(String keyword) {
		// TODO Auto-generated method stub
		List<Platform> list = null;
		list = this.groupMapper.searchPlatformGroupCompare(keyword);
		return new JsonResult(ResultCode.SUCCESS,"success",list);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.PlatformComparedService#getPlatformGroupCompare(java.lang.String)
	 */
	@Override
	public JsonResult getPlatformGroupCompare(String userId) {
		// TODO Auto-generated method stub
		List result = new ArrayList();
		List<PlatformComparedGroup> list = this.groupMapper.getPlatformGroupCompare(userId);
		//有一个each循环，数据size不超过3个
		for(PlatformComparedGroup group:list){
			Map map = new HashMap();
			map.put("groupId", group.getGroupId());
			map.put("platformId", group.getPlatformId());
			map.put("platformName", group.getPlatformName());
			result.add(map);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",result);
	}

	
	@Override
	public JsonResult findPlatformGroupCompare(String userId, Date today,
			Integer accountType) {
		// 准备数据
		List<String> platformIdList = this.groupMapper.getPlatformIdList(userId);
		if(platformIdList == null || platformIdList.size() < 2){
			return new JsonResult(ResultCode.ERROR,"error","you have not put more than 2 platform");
		}
		Date endTime = getEndtimeByStarttimeAccounttype(today,accountType);
		String str = null;
		//从ES检索数据
		try {
			str = this.platformCompareAnalysisRepository.articleInfoCompare(accountType, null, platformIdList, today, endTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		/* 
		 * 解析并装配   ES 返回的烂数据格式
		 */
		//将原生的json字符串    映射变换   为Map集合
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		//取Map集合数据

		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("aggregations");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map<String, Object> transform2 = (Map<String, Object>) transform1.get("platformId");
		if(transform2 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform3 = (List<Map>) transform2.get("buckets");
		List<Map> result = new ArrayList<Map>();
		if(transform3 == null || transform3.size() == 0){
			
			for(String platId: platformIdList){
				Map tata = new HashMap();
				tata.put("platformId", platId);
				if(StringUtils.isNotBlank(platId)){
					String platformName = this.commonService.getPlatformNameById(platId);
					platformName = platformName+"_"+getPlatformTypeName(platId);
					tata.put("platformName", platformName);	
				}
				tata.put("readNum", 0);
				tata.put("transNum", 0);
				tata.put("collectNum", 0);
				tata.put("shareNum", 0);
				tata.put("commentNum", 0);
				tata.put("appriseNum", 0);
				tata.put("awardNum", 0);
				result.add(tata);
			}
			return new JsonResult(ResultCode.SUCCESS,"success",result);
		}
		
		for(Map temp:transform3){
			Map map = new HashMap();
			String platformId = (String) temp.get("key");
			map.put("platformId", platformId);
			if(StringUtils.isNotBlank(platformId)){
				String platformName = this.commonService.getPlatformNameById(platformId);
				platformName = platformName+"_"+getPlatformTypeName(platformId);
				map.put("platformName", platformName);	
			}
			//文章总数
			map.put("articleNum",temp.get("doc_count"));
			//阅读数
			Map readNum = (Map) temp.get("clickNum");
			map.put("readNum",readNum!=null?readNum.get("value"):0);
//			if(readNum != null){
//				map.put("readNum", readNum.get("value"));
//			}
			//转载数
			Map transNum = (Map) temp.get("transNum");
			map.put("transNum",transNum !=null?transNum.get("value"):0);
//			if(transNum != null){
//				map.put("transNum", transNum.get("value"));
//			}
//			String transStr = null;
//			try {
//				List searchList = new ArrayList();
//				searchList.add(platformId);
//				transStr = this.homePageRepository.articleTransCount(accountType, null, searchList, today, endTime);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Integer transNum = getTransNum(transStr);
//			map.put("transNum", transNum);
			
			//收藏数
			Map collectNum = (Map) temp.get("subscribeNum");
			if(collectNum != null){
				map.put("collectNum", collectNum.get("value"));
			}
			//分享数
			Map shareNum = (Map) temp.get("shareNum");
			if(shareNum != null){
				map.put("shareNum", shareNum.get("shareNum"));
			}			
			//评论数
			Map commentNum = (Map) temp.get("commentNum");
			if(commentNum != null){
				map.put("commentNum", commentNum.get("value"));
			}
			//点赞数
			Map appriseNum = (Map) temp.get("awardNum");
			if(appriseNum != null){
				map.put("appriseNum", appriseNum.get("value"));
			}
			//打赏数
			Map awardNum = (Map) temp.get("thumbsNum");
			if(awardNum != null){
				map.put("awardNum", awardNum.get("value"));
			}
			result.add(map);
		}
		
		if( result.size() < platformIdList.size()){
			List<Map> circleList = null;
			try {
				circleList = deepCopy(result);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<platformIdList.size();i++){
				String id = platformIdList.get(i);
				int counter = 0;
				for(Map leftData:circleList){
					counter++;
					String leftId = leftData.get("platformId").toString();
					if(id.equals(leftId)){
						break;
					}
					if(counter == circleList.size() && !leftData.get("platformId").toString().equals(id)){
						Map dataMap = new HashMap();
						dataMap.put("platformId", id);
						if(StringUtils.isNotBlank(id)){
							String name = this.commonService.getPlatformNameById(id);
							dataMap.put("platformName", name+"_"+getPlatformTypeName(id));	
							dataMap.put("articleNum",0);
							dataMap.put("readNum", 0);
							dataMap.put("transNum", 0);
							dataMap.put("collectNum", 0);
							dataMap.put("shareNum", 0);
							dataMap.put("commentNum", 0);
							dataMap.put("appriseNum", 0);
							dataMap.put("awardNum", 0);
						}
						result.add(dataMap);
					}
				}
			}
		
		}
		return new JsonResult(ResultCode.SUCCESS,"success",result);
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
				return startTime = CalendarUtil.plusDay(1,startTime);
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
	private static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {  
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
	    ObjectOutputStream out = new ObjectOutputStream(byteOut);  
	    out.writeObject(src);  

	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
	    ObjectInputStream in = new ObjectInputStream(byteIn);  
	    @SuppressWarnings("unchecked")  
	    List<T> dest = (List<T>) in.readObject();  
	    return dest;  
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

}
