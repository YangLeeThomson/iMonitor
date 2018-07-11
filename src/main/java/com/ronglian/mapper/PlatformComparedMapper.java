/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.mapper 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午5:21:03 
 */
package com.ronglian.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronglian.common.JsonResult;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformComparedGroup;

 /** 
 * @ClassName: PlatformComparedMapper 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午5:21:03  
 */
@Mapper
public interface PlatformComparedMapper {

	 @Insert("insert into platform_compare_group(group_id,user_id,platform_id,create_time)"
	            + " values "
	            + "(#{groupId},#{userId},#{platformId},#{createTime})") 
	public int insertPlatformGroup(PlatformComparedGroup platformGroup);
	
	 @Delete("delete from platform_compare_group "
				+ " where group_id = #{groupId} ")
	public int deletePlatformGroupCompare(String groupId);
	 
	 @Delete("delete from platform_compare_group where user_id=#{userId}")
	public int deletePlatformGroupCompareByUserId(String userId);
	 
	 
/*	 
		private String id;
	
	private String name;
	
	@JsonIgnore  
	private String serial;
	
	private String platformTypeId;
	
	private String platformTypeName;
	
	@JsonIgnore  
	private String tenantId;
	
	@JsonIgnore  
	private Date createTime;
	
	@JsonIgnore  
	private Integer status;
	
	@JsonIgnore  
	private Integer weight; 
	 
	 */
		@Select("select id,name,platform_type_id,platform_type_name from platform p where p.name like CONCAT('%',#{keyword},'%')")
		@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "name", column = "name"),
			@Result(property = "platformTypeId", column = "platform_type_id"),
			@Result(property = "platformTypeName", column = "platform_type_name")
		})
	public List<Platform> searchPlatformGroupCompare(String keyword);
		
		@Select("select g.* , p.name  from platform_compare_group g , platform p where p.id = g.platform_id and user_id=#{userId}  ")
		@Results({
			@Result(property = "platformId", column = "platform_id"),
			@Result(property = "createTime", column = "create_time"),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "platformName", column = "name"),
			@Result(property = "groupId", column = "group_id")
		})
	public List<PlatformComparedGroup> getPlatformGroupCompare(String userId);
		
	@Select("select g.platform_id  from platform_compare_group g , platform p where p.id = g.platform_id and g.user_id=#{userId}  ")
	public List<String> getPlatformIdList(String userId);
}
