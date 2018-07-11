package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午2:44:01
* @description:传播地域分析
*/
public interface SpreadAreadAnalysisRepository {
	
	/**
	 * 地区转载列表
	 * */
	String areaTransList(String areaName,int accountType, String platformTypeId, List<String> platformIdList,
			Date startTime, Date endTime, int pageNo, int pageSize) throws IOException;

}
