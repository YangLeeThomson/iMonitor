package com.ronglian.mapper;

import com.ronglian.model.User;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: sunqian
 * @date:2018年6月14日 上午10:55:25
 * @description:用户表操作sql
 */
@Mapper
public interface UserMapper {
	
	@Insert("insert into users (id,username,password,tenant_id,create_time,status) values(#{id},#{username},#{password},#{tenantId},now(),#{status})")
	public int add(User user);
	
	@Select("select tenant_id,username,id,create_time from users where tenant_id=#{tenantId}")
	@Results({
		@Result(property = "create_time", column = "create_time"),
		@Result(property = "username", column = "username"),
		@Result(property = "id", column = "id"),
		@Result(property = "tenant_id", column = "tenant_id")
	})
	public List<User> listByTenantId(@Param("tenantId")String tenantId);
	
	@Select("select * from users where 1=1")
	public List<User> list();

	@Select("select * from users where username = #{username}")
	public User findByName(@Param("username") String username);

	@Select("select * from users where id=#{id}")
	public User findOne(@Param("id") String id);
	
	@Update("update users set username = #{username} where id = #{id}")
    public int update(@Param("id") String id,@Param("username") String username);

	@Delete("delete from users where id=#{id}")
	public int delete(@Param("id") String id);
}
