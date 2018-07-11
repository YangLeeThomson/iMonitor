package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月18日 下午11:43:53
* @description:平台对比分析
*/
public interface PlatformCompareAnalysisRepository {
	
	/**
	 * 选定平台资讯对比
	 * */
	String articleInfoCompare(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime)throws IOException;
}
