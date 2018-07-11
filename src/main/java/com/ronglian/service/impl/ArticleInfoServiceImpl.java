/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午4:32:32 
 */
package com.ronglian.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.mapper.ArticleInfoHourMapper;
import com.ronglian.model.ArticleInfoHour;
import com.ronglian.repository.ArticleTransRepository;
import com.ronglian.repository.CopyrightMonitorRepository;
import com.ronglian.repository.SpreadTrackRepository;
import com.ronglian.service.ArticleInfoService;
import com.ronglian.service.CommonService;
import com.ronglian.utils.DateTimeUtils;
import com.ronglian.utils.JsonUtils;

 /** 
 * @ClassName: ArticleInfoServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午4:32:32  
 */
@Service
public class ArticleInfoServiceImpl implements ArticleInfoService {
	
	
	@Autowired
	private CopyrightMonitorRepository esDao;
	
	@Autowired
	private ArticleTransRepository esTransDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ArticleInfoHourMapper articleMapper;
	
	@Autowired
	private SpreadTrackRepository spreadTrackRepository;

	
	@Override
	public JsonResult getArticleTransCounted(String id, int page, int pageSize) {
		Map articleInfo = findArticleInfo(id);
		Map transInfo = listTransArticle(id, page, pageSize);
		Map data = new HashMap();
		data.put("articleInfo", articleInfo);
		data.put("transInfo", transInfo);
		return new JsonResult(ResultCode.SUCCESS, "success", data);
	}
	
	@Override
	public JsonResult getArticleInfo(String id) {
//		1、从ES服务获取      单篇文章详情
		String str = null;
		try {
			str = this.esDao.getOriginalArticle(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}		
		//2、如数据字段不匹配，进行数据装配
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		//将原生的json字符串    映射变换   为Map集合
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map<String, Object> transform3 = transform2.get(0);
		Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
		if(transform4 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map map = new HashMap();
		map.put("id", id);
		map.put("content", transform4.get("content"));
		map.put("report", transform4.get("report"));
		map.put("title", transform4.get("title"));
		map.put("source", (String)transform4.get("platformName")+"_"+(String)transform4.get("platformTypeName"));
		map.put("url", transform4.get("url"));
		Long timeStamp = (Long) transform4.get("publishTime");
		String publishTime = DateTimeUtils.formatDate(new Date(timeStamp));
		map.put("publishTime", publishTime);
		return new JsonResult(ResultCode.SUCCESS,"success",map);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleInfoService#getImonitorStatus(java.lang.String)
	 */
	@Override
	public JsonResult getImonitorStatus(String id) {
		// TODO Auto-generated method stub
//		1、从ES服务获取      单篇文章详情
		String str = null;
		try {
			str = this.esDao.getOriginalArticle(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}		
		//2、如数据字段不匹配，进行数据装配
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		//将原生的json字符串    映射变换   为Map集合
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map<String, Object> transform3 = transform2.get(0);
		Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
		if(transform4 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		if(transform4.get("publishTime") == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Long publishTimeStamp = (Long) transform4.get("publishTime");
		Date today = new Date();
		Date publishTime = new Date(publishTimeStamp);
		int leftTime = 14 - DateTimeUtils.daysOfTwo(publishTime, today);
		//从es获取统计时间
		Map map = new HashMap();
		if(leftTime > 0){
			map.put("status", 1);
			map.put("leftTime", leftTime);
		}else{
			map.put("status", 0);
			map.put("leftTime", 0);
		}
//		map.put("status", "1，检测中，0，未监测");
		return new JsonResult(ResultCode.SUCCESS,"success",map);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleInfoService#getPlatformAnalysis(java.lang.String)
	 */
	@Override
	public JsonResult getPlatformAnalysis(String id) {
		// TODO Auto-generated method stub
		String str = null;
		if(StringUtils.isBlank(id)){
			return new JsonResult(ResultCode.ERROR,"id was null",null);
		}
		try {
			str = this.esDao.getOriginalArticleListByArticleId(id);
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
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}		
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> result = new ArrayList<Map>();
		int i = 1;
		for(Map<String, Object> transform3:transform2){
			Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
//			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			//"看苏州"
			String platformId = (String) transform4.get("platformId");
			map.put("platformId", platformId);
			if(StringUtils.isNotBlank(platformId)){
				String platformName = this.commonService.getPlatformNameById(platformId);
				platformName = platformName+"_"+getPlatformTypeName(platformId);
				map.put("platformName", platformName);
			}
			//"阅读数"
			map.put("readNum",transform4.get("clickNum"));
			//"转载数"unionId
			map.put("transNum", transform4.get("transNum"));
//			String unionId = transform4.get("unionId").toString();
//			Integer transNum = getTransNum(unionId);
//			map.put("transNum",transNum);
			//"评论数"
			map.put("commentNum",transform4.get("commentNum") );
			//"发布时间格式：  2018-05-08  "
			Long timeStamp = (Long) transform4.get("publishTime");
			String publishTime = DateTimeUtils.formatDate(new Date(timeStamp));
			map.put("publishTime", publishTime);
			//"点赞数"
			map.put("appriseNum", transform4.get("thumbsNum"));
			result.add(map);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",result);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleInfoService#getTransArticleList(java.lang.String)
	 */
	@Override
	public JsonResult getTransArticleList(String id,int page,int pageSize,Integer transType,Integer tort) {
		// TODO Auto-generated method stub
		String str = null;
		String strCount = null;
		try {
			if(transType == 9 && tort == 9){
				str = this.esTransDao.getTransList(id,page,pageSize);
				strCount = this.esTransDao.getTransListTotal(id);
			}else{
				if(transType == 9){
					transType = null;
				}
				if(tort == 9){
					tort = null;
				}
				str = this.esTransDao.getTransListByTransTypeAndTort(id,page,pageSize,transType,tort);
				strCount = this.esTransDao.getTransListByTransTypeAndTort(id, 1, 10000, transType, tort);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		
		Map<String, Object> mapCount = JsonUtils.parseJSON2Map(strCount);
		System.out.println(strCount);
		
		Map<String, Object> mapCount2 = (Map<String, Object>) mapCount.get("hits");
		int count = 0;
		if(mapCount2 != null){
			List<Map> listCount = (List<Map>) mapCount2.get("hits");
			count = listCount.size();
		}
		List<Map> result = new ArrayList<Map>();
		int i = 0;
		for(Map<String, Object> transform3:transform2){
			i++;
			int num = (page-1)*pageSize + i;
			Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
//			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			//"记录时间  格式： 2018-06-26 15：32  精确到分钟"
			Long timeStamp1 = (Long) transform4.get("createTime");
			String createTime = null;
			if(timeStamp1 != null){
				createTime = DateTimeUtils.formatDate2(new Date(timeStamp1));
			}
			map.put("num", num);
			map.put("createTime",createTime);
			//"转载媒体"
			map.put("transMedia", transform4.get("crawlSource"));
			//"转载标题"
			map.put("transTitle",transform4.get("title") );
			//"原文标题"
			map.put("title",transform4.get("originTitle") );
			//"转载时间 格式： 2018-06-26 15：32  精确到分钟"
			Long timeStamp2 = (Long) transform4.get("reportTime");
			String transTime = DateTimeUtils.formatDate2(new Date(timeStamp2));
			map.put("transTime", transTime);
			//"相似度 eg：98%"
			map.put("similarity",transform4.get("transSimilarity"));
//			transType 微信转载、9，全部,1 网页转载、2微信、3微博转载、4app 5搜索引擎
			Integer key1 = null;
			if(transform4.get("channel") != null){
				key1 = Integer.parseInt(transform4.get("channel").toString());
			}
			if(key1 == 1){
				map.put("transType","网页转载");
			}
			if(key1 == 2){
				map.put("transType","微信转载");
			}
			if(key1 == 3){
				map.put("transType","微博转载");
			}
			if(key1 == 4){
				map.put("transType","app转载");
			}
			if(key1 == 5){
				map.put("transType","搜索引擎");
			}
//			版权筛选（0.版权存疑、1.注明来源、9 全部）。
			Integer key2 = null;
			if(transform4.get("isTort") != null){
				key2 = Integer.parseInt(transform4.get("isTort").toString());
			}
			if(key2 == 0){
				map.put("tort","版权存疑");
			}
			if(key2 == 1){
				map.put("tort","注明来源");
			}
			map.put("unionId", transform4.get("unionId"));
			map.put("articleId", transform4.get("articleId"));
			map.put("webpageCode", transform4.get("webpageCode"));
			map.put("url", transform4.get("webpageUrl"));
			result.add(map);
		}
		Map resultMap = new HashMap();
		resultMap.put("total", count);
		resultMap.put("transInfoList", result);
		return new JsonResult(ResultCode.SUCCESS,"success",resultMap);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleInfoService#getTransCurrent(java.lang.String)
	 */
	@Override
	public JsonResult getTransCurrent(String id) {
		// TODO Auto-generated method stub
		List<ArticleInfoHour> list = this.articleMapper.findListByUnionId(id);
		List<String> platformIdList = this.articleMapper.findIdListByUnion(id);
		if(list == null){
			return null;
		}
		String url = null;
		try {
			String articleResult = spreadTrackRepository.getArticle(id);
			JSONObject articleResultHitsObject = (JSONObject.parseObject(articleResult)).getJSONObject("hits");
			JSONArray articleResultHitsArray = articleResultHitsObject.getJSONArray("hits");
			JSONObject articleObject = articleResultHitsArray.getJSONObject(0);
			if(articleObject!= null) {
				JSONObject _source = articleObject.getJSONObject("_source");
				url = _source.getString("url");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map> resultList = new ArrayList();
		for(String platformId:platformIdList){
			String platformName = this.commonService.getPlatformNameById(platformId);
			Map result = new HashMap();
			platformName = platformName+"_"+getPlatformTypeName(platformId);
			result.put("url",url);
			result.put("platformName", platformName);
			result.put("platformId", platformId);
			List dailyList = new ArrayList();
			result.put("dailyList", dailyList);
			resultList.add(result);
		}
		List<Map> copyList = null;
		try {
			copyList = deepCopy(resultList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(ArticleInfoHour entity:list){
			String platformId = entity.getPlatform_id();
			if(entity.getPlatform_id() == null){
				continue;
			}
			String createTime = entity.getCreate_time();
			String jsonStr = entity.getJson_nums();

			for(int i=0;i<copyList.size();i++){
				if(copyList.get(i).get("platformId").toString().equals(platformId)){
					/*
					 * 解析json，取出当天的咨询数
					 */
					Map<String, Object> temp = JsonUtils.parseJSON2Map(jsonStr);
					Set<String> set = temp.keySet();
					String key = "0";
					for(String k:set){
						if(Integer.parseInt(k) > Integer.parseInt(key)){
							key = k;
						}
					}
					Map<String,Object> temp2 = (Map) temp.get(key);
					int commentNum = 0;
					int readNum = 0;
					int transNum = 0;
					int appriseNum = 0;
					if(temp2.get("commentNum") != null){
						commentNum = Integer.parseInt(temp2.get("commentNum").toString());
					}
					if(temp2.get("clickNum") != null){
						readNum = Integer.parseInt(temp2.get("clickNum").toString());
					}
					if(temp2.get("transNum") != null){
						transNum = Integer.parseInt(temp2.get("transNum").toString());
					}
					if(temp2.get("thumbsNum") != null){
						appriseNum = Integer.parseInt(temp2.get("thumbsNum").toString());
					}
					/*
					 * 入结果集
					 */
					List dailyList = (List) resultList.get(i).get("dailyList");

					Map countBody = new HashMap();
					countBody.put("commentNum", commentNum);
					countBody.put("readNum", readNum);
					countBody.put("transNum", transNum);
					countBody.put("appriseNum", appriseNum);
					countBody.put("createTime", createTime);
					dailyList.add(countBody);
					resultList.get(i).put("dailyList", dailyList);
				}
			}
		}
		return new JsonResult(ResultCode.SUCCESS, "success", resultList);
	}
	
	private  Integer getTransNum(String unionId){
		String strCount = null;
		try {
			strCount = this.esTransDao.getTransListTotal(unionId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(strCount)){
			return 0;
		}
		Map<String, Object> mapCount = JsonUtils.parseJSON2Map(strCount);
		if(mapCount.get("hits") == null){
			return 0;
		}
		Map<String, Object> mapCount2 = (Map<String, Object>) mapCount.get("hits");
		if(mapCount2.get("hits") == null){
			return 0;
		}
		List<Map> listCount = (List<Map>) mapCount2.get("hits");
		int count = listCount.size();
		return count;
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

	/* 
	 * 获取单篇文章详情
	 */
	private Map findArticleInfo(String id) {
//		1、从ES服务获取      单篇文章详情
		String str = null;
		try {
			str = this.esDao.getOriginalArticle(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return null;
		}		
		//2、如数据字段不匹配，进行数据装配
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		//将原生的json字符串    映射变换   为Map集合
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return null;
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return null;
		}
		Map<String, Object> transform3 = transform2.get(0);
		Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
		if(transform4 == null){
			return null;
		}
		Map map = new HashMap();
		map.put("id", id);
		map.put("content", transform4.get("content"));
		map.put("report", transform4.get("report"));
		map.put("title", transform4.get("title"));
		map.put("source", (String)transform4.get("platformName")+"_"+(String)transform4.get("platformTypeName"));
		map.put("url", transform4.get("url"));
		Long timeStamp = (Long) transform4.get("publishTime");
		String publishTime = DateTimeUtils.formatDate(new Date(timeStamp));
		map.put("publishTime", publishTime);
		return map;
	}
	public Map listTransArticle(String id,int page,int pageSize) {
		// TODO Auto-generated method stub
		String str = null;
		String strCount = null;
		try {
				str = this.esTransDao.getTransList(id,page,pageSize);
				strCount = this.esTransDao.getTransListTotal(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return null;
		}
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return null;
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return null;
		}
		Map<String, Object> mapCount = JsonUtils.parseJSON2Map(strCount);
		Map<String, Object> mapCount2 = (Map<String, Object>) mapCount.get("hits");
		int count = 0;
		if(mapCount2 != null){
			List<Map> listCount = (List<Map>) mapCount2.get("hits");
			count = listCount.size();
		}
		List<Map> result = new ArrayList<Map>();
		int i = 0;
		for(Map<String, Object> transform3:transform2){
			i++;
			int num = (page-1)*pageSize + i;
			Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
//			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			//"记录时间  格式： 2018-06-26 15：32  精确到分钟"
			Long timeStamp1 = (Long) transform4.get("createTime");
			String createTime = null;
			if(timeStamp1 != null){
				createTime = DateTimeUtils.formatDate2(new Date(timeStamp1));
			}
			map.put("num", num);
			map.put("createTime",createTime);
			//"转载媒体"
			map.put("transMedia", transform4.get("crawlSource"));
			//"转载标题"
			map.put("transTitle",transform4.get("title") );
			//"原文标题"
			map.put("title",transform4.get("originTitle") );
			//"转载时间 格式： 2018-06-26 15：32  精确到分钟"
			Long timeStamp2 = (Long) transform4.get("reportTime");
			String transTime = DateTimeUtils.formatDate2(new Date(timeStamp2));
			map.put("transTime", transTime);
			//"相似度 eg：98%"
			map.put("similarity",transform4.get("transSimilarity"));
//			transType 微信转载、9，全部,1 网页转载、2微信、3微博转载、4app 5搜索引擎
			Integer key1 = null;
			if(transform4.get("channel") != null){
				key1 = Integer.parseInt(transform4.get("channel").toString());
			}
			if(key1 == 1){
				map.put("transType","网页转载");
			}
			if(key1 == 2){
				map.put("transType","微信转载");
			}
			if(key1 == 3){
				map.put("transType","微博转载");
			}
			if(key1 == 4){
				map.put("transType","app转载");
			}
			if(key1 == 5){
				map.put("transType","搜索引擎");
			}
//			版权筛选（0.版权存疑、1.注明来源、9 全部）。
			Integer key2 = null;
			if(transform4.get("isTort") != null){
				key2 = Integer.parseInt(transform4.get("isTort").toString());
			}
			if(key2 == 0){
				map.put("tort","版权存疑");
			}
			if(key2 == 1){
				map.put("tort","注明来源");
			}
			map.put("unionId", transform4.get("unionId"));
			map.put("articleId", transform4.get("articleId"));
			map.put("webpageCode", transform4.get("webpageCode"));
			map.put("url", transform4.get("webpageUrl"));
			result.add(map);
		}
		Map resultMap = new HashMap();
		resultMap.put("total", count);
		resultMap.put("transInfoList", result);
		return resultMap;
	}

}
