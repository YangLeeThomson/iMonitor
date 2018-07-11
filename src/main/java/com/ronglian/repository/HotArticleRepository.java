package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午12:05:25
* @description:热门文章
*/
public interface HotArticleRepository {
	/**
	 * 热门文章
	 * 
	 * */
	String hotArticleList(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int isOrigin,String orderField,int pageNo,int pageSize)throws IOException ;
	
	String hotArticleListTransNum(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime,int isOrigin,String orderField,int pageNo,int pageSize)throws IOException ;
	
	public int getTransNum(String unionId);
}

