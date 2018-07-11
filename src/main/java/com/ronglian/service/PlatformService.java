package com.ronglian.service;

import java.util.List;
import java.util.Set;


import com.ronglian.common.PageResult;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformHistory;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月15日 下午4:42:46
* @description:描述
*/
public interface PlatformService {
	
	public int add(Platform platform);

	public Platform findById(String id);
	
	public List<String> findByPlatformTypeId(String platformTypeId);
	
	public Platform findByName(String name,String type,String tenantId);
	
	public PageResult<Platform> findByTenantId(String tenantId,int pageNo,int pageSize);
	
	public PageResult<Platform> find(int pageNo,int pageSize);
	
	public int update(Platform platform);
	
	public int delete(String id);
	
	public int addPlatformHistory(PlatformHistory platformHistory);
	
	public int countPlatformHistory(String platformTypeId);
	
	public List<PlatformHistory> findPlatformHistoryList(String platformTypeId,int pageNo,int pageSize);
	
	public List<PlatformHistory> findPlatformHistoryListByUserid(String platformTypeId,String username,int pageNo,int pageSize);
	
	public PlatformHistory findOnePlatformHistory(String platformId,String platformTypeId,String userid);
	
	public int updatePlatformHistory(PlatformHistory platformHistory);
	
	public List<Platform> findPlatformByIds(Set<String> ids);
}
