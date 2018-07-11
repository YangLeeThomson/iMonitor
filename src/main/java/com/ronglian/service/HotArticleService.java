package com.ronglian.service;

import java.io.IOException;
import java.util.Date;

import com.ronglian.common.PageResult;
import com.ronglian.model.HotArticle;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月19日 下午7:44:31
* @description:热门文章
*/
public interface HotArticleService {

	public PageResult<HotArticle> findPageList(int accountType,String platformTypeId,String platformId,String groupId,String isOriginal,String orderFiled,Date bTime,Date eTime, int pageNo, int pageSize) throws IOException;
}
