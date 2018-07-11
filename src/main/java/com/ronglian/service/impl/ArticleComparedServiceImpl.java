/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午2:38:31 
 */
package com.ronglian.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.mapper.ArticleComparedMapper;
import com.ronglian.model.ArticleComparedGroup;
import com.ronglian.repository.ArticleTransRepository;
import com.ronglian.repository.CopyrightMonitorRepository;
import com.ronglian.service.ArticleComparedService;
import com.ronglian.utils.DateTimeUtils;
import com.ronglian.utils.JsonUtils;

 /** 
 * @ClassName: ArticleComparedServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午2:38:31  
 */
@Service
public class ArticleComparedServiceImpl implements ArticleComparedService {

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleComparedService#addGroup(com.ronglian.model.ArticleComparedGroup)
	 */
	@Autowired
	private ArticleComparedMapper articleComparedMapper;
	
	@Autowired
	private ArticleTransRepository transEsDao;
	
	@Autowired
	private CopyrightMonitorRepository esDao;
	@Override
	public int addGroup(Map group) {
		// TODO Auto-generated method stub
		String userId = (String) group.get("userId");
		List<String> idList = (List<String>) group.get("idList");
		String groupId = (String) group.get("groupId");
		Date createTime = new Date();
		/* 
		 * 对createTime进行format
		 */
		for(String id:idList){
			ArticleComparedGroup comparedGroup = new ArticleComparedGroup();
			comparedGroup.setGroupId(groupId);
			comparedGroup.setUserId(userId);
			comparedGroup.setArticleId(id);
			comparedGroup.setCreateTime(createTime);
			this.articleComparedMapper.insertArticleGroup(comparedGroup);
		}
		return idList.size();
	}

	@Override
	public int delGroup(String groupId) {
		// TODO Auto-generated method stub
		return this.articleComparedMapper.deleteGroup(groupId);
	}
	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleComparedService#getGroupListByUserId(java.lang.String)
	 */
	@Override
	public List<String> getGroupListByUserId(String userId) {
		// TODO Auto-generated method stub
		
		return this.articleComparedMapper.selectGroupIdListByUserId(userId);
	}
	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleComparedService#getArticleIdListByGroupId(java.lang.String)
	 */
	@Override
	public List<String> getArticleIdListByGroupId(String groupId) {
		// TODO Auto-generated method stub
		return this.articleComparedMapper.selectArticleIdListByGroupId(groupId);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleComparedService#getArticleListByTitle(java.lang.String)
	 */
	@Override
	public JsonResult getArticleListByTitle(String keyword) {
		String str = null;
		//1.从es获取文章列表（显示不超过5条）
		try {
			str = this.esDao.getTitleList(keyword, 1, 5);
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
			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			map.put("id", id);
			map.put("articleId", transform4.get("articleId"));
			map.put("url", transform4.get("url"));
			map.put("title", transform4.get("title"));
			map.put("num", i++);
			result.add(map);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",result);
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.ArticleComparedService#getArticleCompared(java.lang.String)
	 */
	@Override
	public JsonResult getArticleCompared(String userId) {
		// TODO Auto-generated method stub
		List<ArticleComparedGroup> groupList = this.articleComparedMapper.selectGroupListByUserId(userId);
		if(groupList == null || groupList.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List resultList = new ArrayList();
//		遍历组内文章
		for(ArticleComparedGroup group:groupList){
		Map resultMap = new HashMap();
		resultMap.put("createTime", group.getCreateTime());
		resultMap.put("groupId", group.getGroupId());
		List<String> articleList = this.articleComparedMapper.selectArticleIdListByGroupId(group.getGroupId());
		String str = null;
		try {
			str = this.esDao.getOriginalArticleList(articleList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> transforMap = null;
		transforMap = JsonUtils.parseJSON2Map(str);
		List<Map> list = new ArrayList<Map>();
		boolean status = true;
		for(String newsId:articleList){
			//判断group组内文章是否存在外部文章，外部文章的articleId必须均以“outsidelink”开头。
			if(StringUtils.isNotBlank(newsId) && newsId.startsWith("outsidelink")){
			status = false;
			Map map = new HashMap();
			map.put("articleId", newsId);
			list.add(map);
			}
		}
		/* 
		 * 解析并装配   ES 返回的数据格式
		 */
		if(StringUtils.isBlank(str)){
			resultMap.put("info", list);
			resultList.add(resultMap);
			continue;
		}
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			resultMap.put("info", list);
			resultList.add(resultMap);
			continue;
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			resultMap.put("info", list);
			resultList.add(resultMap);
			continue;
		}
		int i = 1;
		for(Map<String, Object> transform3:transform2){
			Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			//"文章标题"
			map.put("title",transform4.get("title"));
			//文章url
			map.put("url",transform4.get("url"));
			//"媒体来源  凤凰网  腾讯新闻"
			if(transform4.get("platformName") != null && transform4.get("platformTypeName") != null){
				map.put("mediaSource",transform4.get("platformName").toString()+"_"+transform4.get("platformTypeName").toString() );	
			}
			//"发布时间 格式 ： 2018-06-14"
			Long timeStamp = (Long) transform4.get("publishTime");
			String publishTime = DateTimeUtils.formatDate(new Date(timeStamp));
			map.put("publishTime", publishTime);
			//"转载数"
			map.put("transNum",transform4.get("transNum") );
/*			String unionId = transform4.get("unionId").toString();
			Integer transNum = getTransNum(unionId);*/
//			map.put("transNum",transNum);
			//"阅读数"
			map.put("readNum", transform4.get("clickNum"));
			//"评论数"
			map.put("commentNum", transform4.get("commentNum"));
			//"点赞数"
			map.put("appriseNum", transform4.get("thumbsNum"));
			//文章id
			map.put("unionId", transform4.get("unionId"));
			//联合主键id
			map.put("articleId", transform4.get("articleId"));
			list.add(map);
		}
		resultMap.put("info", list);
		/*
		 * 计算监测状态和剩余时间
		 */
		if(status == true){
			Map temp = list.get(0);
			Date today = new Date();
			Date publishTime = null;
				try {
					publishTime = new SimpleDateFormat("yyyy-MM-dd").parse(temp.get("publishTime").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			int leftTime = 14 - DateTimeUtils.daysOfTwo(publishTime, today);
			if(leftTime > 0){
				resultMap.put("imonitorStatus", 1);
				resultMap.put("leftTime", leftTime);
			}else{
				resultMap.put("imonitorStatus", 0);
				resultMap.put("leftTime", 0);
			}
		}
		resultList.add(resultMap);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",resultList);
	}
	private  Integer getTransNum(String unionId){
		String strCount = null;
		try {
			strCount = this.transEsDao.getTransListTotal(unionId);
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
}
