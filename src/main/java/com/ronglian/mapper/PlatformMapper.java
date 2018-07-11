package com.ronglian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ronglian.mapper.provider.PlatformSqlProvider;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformHistory;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月15日 下午3:39:20
* @description:描述
*/
@Mapper
public interface PlatformMapper {
	
	@Select("select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where id=#{id}")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public Platform findById(@Param("id") String id);

	@Select("select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where name=#{name} and platform_type_name=#{type} and tenant_id=#{tenantId}")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public Platform findByName(@Param("name") String name,@Param("type") String type,@Param("tenantId") String tenantId);
	
	@Insert("insert into platform (id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight) values(#{id},#{name},#{serial},#{platformTypeId},#{platformTypeName},#{tenantId},NOW(),#{status},#{weight})")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public int add(Platform platform);
	
	@Select("select count(id) from platform where status=0")
	public int allCount();
	
	@Select("select count(id) from platform where status=0 and tenant_id=#{tenantId}")
	public int tenantCount(@Param("tenantId") String tenantId);
	
	@SelectProvider(type=PlatformSqlProvider.class,method="pageListSql")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<Platform> findPageList(Integer pageNo,Integer pageSize);
	
	@SelectProvider(type=PlatformSqlProvider.class,method="findByTenantIdPageListSql")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<Platform> findByTenantIdPageList(String tenantId,Integer pageNo,Integer pageSize);
	
	@Update("update platform set name=#{name},serial=#{serial},platform_type_id=#{platformTypeId},platform_type_name=#{platformTypeName},status=#{status},weight=#{weight} where id = #{id}")
	public int update(Platform platform);
	
	@Delete("delete from platform where id=#{id}")
	public int delete(@Param("id") String id);
	
	@Select("select count(platform_type_id) from platform_history where platform_type_id=#{platformTypeId}")
	public int countPlatformHistory(@Param("platformTypeId") String platformTypeId);
	
	@Insert("insert into platform_history (platform_id,platform_type_id,platform_name,platform_type_name,create_time,user_id) values(#{platformId},#{platformTypeId},#{platformName},#{platformTypeName},NOW(),#{userId})")
	public int addPlatformHistory(PlatformHistory platformHistory);
	
	@SelectProvider(type=PlatformSqlProvider.class,method="findPlatformHistoryListSql")
	@Results({
		@Result(property = "platformId", column = "platform_id"),
		@Result(property = "platformName", column = "platform_name"),
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<PlatformHistory> findPlatformHistoryList(String platformTypeId,int pageNo,int pageSize);
	
	@Select("select platform_id,platform_type_id,platform_name,platform_type_name,create_time from platform_history where platform_id=#{platformId} and platform_type_id=#{platformTypeId} and user_id=#{user_id}")
	@Results({
		@Result(property = "platformId", column = "platform_id"),
		@Result(property = "platformName", column = "platform_name"),
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "createTime", column = "create_time")
	})
	public PlatformHistory findOnePlatformHistory(@Param("platformId")  String platformId,@Param("platformTypeId")  String platformTypeId,@Param("user_id")  String userid);
	
	@Update("update platform_history set platform_name=#{platformName},platform_type_name=#{platformTypeName},create_time=NOW() where platform_id=#{platformId} and platform_type_id=#{platformTypeId} and user_id=#{userId}")
	public int updatePlatformHistory(PlatformHistory platformHistory);

	@SelectProvider(type=PlatformSqlProvider.class,method="findPlatformHistoryListByUserid")
	public List<PlatformHistory> findPlatformHistoryListByUserid(String platformTypeId, String userid, int pageNo,int pageSize);
	
	@SelectProvider(type=PlatformSqlProvider.class,method="findPlatformByIds")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "name", column = "name"),
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<Platform> findPlatformByIds(String platformIds);
	//public List<Platform> findPlatformByIds(Set<String> ids);

	@Select("select id,name,serial,platform_type_id,platform_type_name,tenant_id,create_time,status,weight from platform where platform_type_id=#{platformTypeId}")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name"),
		@Result(property = "tenantId", column = "tenant_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<Platform> findByPlatformTypeId(String platformTypeId);
}
