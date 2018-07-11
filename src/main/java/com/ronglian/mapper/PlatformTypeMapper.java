package com.ronglian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.ronglian.mapper.provider.PlatformTypeSqlProvider;
import com.ronglian.model.PlatformType;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 上午10:19:01
* @description:描述
*/
@Mapper
public interface PlatformTypeMapper {
	
	@Insert("insert into platform_type (id,name,tenant_id,create_time,status) values(#{id},#{name},#{tenantId},NOW(),0)")
	public int add(PlatformType platformType);
	
	
	@Select("select count(id) from platform_type where status=0")
	public int allCount();
	
	
	@SelectProvider(type=PlatformTypeSqlProvider.class,method="pageListSql")
	@Results({
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<PlatformType> findPageList(Integer pageNo,Integer pageSize);
	
	
	@Select("select count(id) from platform_type where status=0 and tenant_id=#{tenantId}")
	public Integer tenantCount(@Param("tenantId") String tenantId);
	
	
	@SelectProvider(type=PlatformTypeSqlProvider.class,method="findByTenantIdPageListSql")
	@Results({
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<PlatformType> findByTenantIdPageList(String tenantId,Integer pageNo,Integer pageSize);
	
	
	@Select("select id,name,tenant_id,create_time,status from platform_type where id=#{id}")
	@Results({
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public PlatformType findById(@Param("id") String id);
	

	@Select("select id,name,tenant_id,create_time,status from platform_type where name=#{name} and tenant_id=#{tenantId}")
	@Results({
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public PlatformType findByName(@Param("name") String name,@Param("tenantId")String tenantId);
}
