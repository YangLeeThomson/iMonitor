package com.ronglian.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ronglian.model.ComprehensiveNumTop;
import com.ronglian.model.PlatformTransPeriod;

public interface TransPeriodService {

	List<PlatformTransPeriod> getTransPeriod(String platformTypeId, String platformId,String groupId, Date startTime, Integer accountType);

	Map<String,Object> getComprehensiveNum(String platformTypeId, String platformId,String groupId, Date startTime,Integer accountType,int orderCode,int pageNo,int pageSize);

}
