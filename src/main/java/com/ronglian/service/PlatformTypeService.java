package com.ronglian.service;

import com.ronglian.common.PageResult;
import com.ronglian.model.PlatformType;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 上午10:24:40
* @description:描述
*/
public interface PlatformTypeService {
	
	public int add(PlatformType platformType);
	
	public PlatformType findByName(String tenantId,String name);
	
	public PageResult<PlatformType> find(Integer pageNo,Integer pageSize);

	public PageResult<PlatformType> findByTenantId(String tenantId,Integer pageNo,Integer pageSize);
}
