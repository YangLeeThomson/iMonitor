package com.ronglian.service;

import java.util.Date;

import com.ronglian.common.PageResult;
import com.ronglian.model.TransAndOriginalArticle;

/**
 * 
 * @Description: 查询接口实现类
 * @author sunqian 
 * @date 2018年6月15日 下午3:42:54
 */


public interface ArticleListService {

	PageResult<TransAndOriginalArticle> findTransMediaTypeList(String platformTypeId, String platformId, String groupId, int mediaType,
			Date startTime, Integer accountType, Integer page, Integer pageSize);

	PageResult<TransAndOriginalArticle> findTransChannelList(String platformTypeId, String platformId, String groupId, int channel,
			Date startTime, Integer accountType, Integer page, Integer pageSize);

	PageResult<TransAndOriginalArticle> findTransPlatformList(String platformId, Date startTime, Integer accountType, Integer page,
			Integer pageSize);

	PageResult<TransAndOriginalArticle> findTransMediaOrderList(String platformTypeId, String platformId, String groupId,
			String mediaName, Date startTime, Integer accountType, Integer page, Integer pageSize);




}
