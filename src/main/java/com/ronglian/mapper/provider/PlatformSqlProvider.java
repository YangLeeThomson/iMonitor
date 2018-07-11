package com.ronglian.mapper.provider;


/**
* @author: 黄硕/huangshuo
* @date:2018年5月15日 下午4:22:49
* @description:描述
*/
public class PlatformSqlProvider {
	public String pageListSql(Integer pageNo,Integer pageSize){  
		return "select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where status=0 order by weight desc limit "+(pageNo-1)*pageSize+","+pageSize;  
	}
	
	public String findByTenantIdPageListSql(String tenantId,Integer pageNo,Integer pageSize){  
		return "select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where status=0 and tenant_id='"+tenantId+"' order by weight desc limit "+(pageNo-1)*pageSize+","+pageSize;  
	}
	
	public String findPlatformHistoryListSql(String platformTypeId,int pageNo,int pageSize) {
		return "select platform_id,platform_type_id,platform_name,platform_type_name,create_time from platform_history where platform_type_id='"+platformTypeId+"' order by create_time desc limit "+(pageNo-1)*pageSize+","+pageSize;
	}
	
	public String findPlatformHistoryListByUserid(String platformTypeId,String userid,int pageNo,int pageSize) {
		return "select platform_id platformId,platform_type_id platformTypeId,platform_name platformName,platform_type_name platformTypeName,create_time createTime from platform_history where platform_type_id='"+platformTypeId+"' and user_id='"+userid+"' order by create_time desc limit "+(pageNo-1)*pageSize+","+pageSize;
	}
	
	public String findPlatformByIds(String platformIds){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(platformIds);
		/*if(ids!=null&&ids.size()>0){
			for(String id:ids){
				sb.append(id);
				sb.append(',');
			}
			sb.substring(0,sb.length()-1);
		}*/
		sb.append(")");
		return "select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where id in "+sb.toString();
	}
}
