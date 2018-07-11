package com.ronglian.mapper;

import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.model.User;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: sunqian
 * @date:2018年6月14日 上午10:55:25
 * @description:用户表操作sql
 */
@Mapper
public interface CustomerGroupMapper {
	
	@Insert("insert into custom_monitor_group (id,user_id,group_name,platform_id_list,create_time,modify_time) values(#{id},#{userId},#{groupName},#{platformIdList},now(),now())")
	public int add(CustomMonitorGroup customMonitorGroup);
	
	@Select("select id,user_id userId,group_name groupName,platform_id_list platformIdList,create_time createTime,modify_time modifyTime from custom_monitor_group where user_id=#{user_id} order by create_time DESC")
	public List<CustomMonitorGroup> listByUserId(@Param("user_id") String userId);

	@Select("select id,user_id userId,group_name groupName,platform_id_list platformIdList,create_time createTime,modify_time modifyTime  from custom_monitor_group where group_name = #{group_name}")
	public CustomMonitorGroup findByGroupName(@Param("group_name") String groupName);

	@Select("select id,user_id userId,group_name groupName,platform_id_list platformIdList,create_time createTime,modify_time modifyTime  from custom_monitor_group where id=#{id}")
	public CustomMonitorGroup findByGroupId(@Param("id") String id);
	
	@Update("update custom_monitor_group set group_name = #{groupName},platform_id_list = #{platformIdList},modify_time=now() where id = #{id}")
    public int update(CustomMonitorGroup customMonitorGroup);

	@Delete("delete from custom_monitor_group where id=#{id}")
	public int delete(@Param("id") String id);
}
