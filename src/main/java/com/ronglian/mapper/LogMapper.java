package com.ronglian.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.ronglian.model.Log;

@Mapper
public interface LogMapper{
	
	@Insert("insert into log (category,log_time,url,title,key_word,content) values(#{category},now(),#{url},#{title},#{keyWord},#{content})")
	public int add(Log log);
	
	@Update("update log set category = #{category},url = #{url},title = #{title},key_word = #{keyWord},content = #{content} where id = #{id}")
    public int update(Log log);
	
}
