package com.ronglian.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ronglian.model.SpreadTimeTrendElement;

public interface SpreadTimeTrendService {

	public List<SpreadTimeTrendElement> getSpreadTimeTend(String platformTypeId,String platformId, String groupId,Date startTime, Integer accountType);

	public Map<String,Object> getMediaOrder(String platformTypeId, String platformId,String groupId, Date startTime,  Integer accountType2);

	public Map<String,Object> getMediaOrderSuper(String platformTypeId, String platformId, String groupId, Date startTime, int accountType, Integer mediaType, Integer channel, int pageNo, int pageSize);


}
