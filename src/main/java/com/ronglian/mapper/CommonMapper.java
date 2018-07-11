/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.mapper 
 * @author: YeohLee   
 * @date: 2018年5月28日 上午1:32:55 
 */
package com.ronglian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.ronglian.model.Platform;

 /** 
 * @ClassName: CommonMapper 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月28日 上午1:32:55  
 */
@Mapper
public interface CommonMapper {

	@Select("select id,name from platform ")
	public List<Platform> selectPlatform();
	
	@Select("select platform_type_name,platform_type_id from platform ")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name")
	})
	public List<Platform> selectPlatformType();
	
	@Select("select DISTINCT platform_type_name,platform_type_id from platform  where platform_type_id = #{platformTypeId} ")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "platformTypeName", column = "platform_type_name")
	})
	public Platform selectPlatformTypeByTypeId(String platformTypeId);
	
	@Select("select id,platform_type_id from platform ")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "id", column = "id")
	})
	public List<Platform> selectPlatformMappingType();
	
	@Select("select DISTINCT id,platform_type_id from platform where id = #{platformId} ")
	@Results({
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "id", column = "id")
	})
	public Platform selectPlatformMappingTypeById(String platformId);
	
}
