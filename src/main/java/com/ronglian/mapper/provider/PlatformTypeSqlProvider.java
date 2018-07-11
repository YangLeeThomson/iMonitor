package com.ronglian.mapper.provider;
/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 上午10:38:35
* @description:描述
*/
public class PlatformTypeSqlProvider {
	public String pageListSql(Integer pageNo,Integer pageSize){  
		return "select id,name,tenant_id,create_time,status from platform_type where status=0 order by id limit "+(pageNo-1)*pageSize+","+pageSize;  
	}
	
	public String findByTenantIdPageListSql(String tenantId,Integer pageNo,Integer pageSize) {
		return "select id,name,tenant_id,create_time,status from platform_type where status=0 and tenant_id='"+tenantId+"' order by id limit "+(pageNo-1)*pageSize+","+pageSize;  
	}
}
