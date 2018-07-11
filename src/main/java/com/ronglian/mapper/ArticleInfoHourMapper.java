package com.ronglian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ronglian.mapper.provider.ArticleInfoHourProvider;
import com.ronglian.mapper.provider.PlatformSqlProvider;
import com.ronglian.model.ArticleInfoHour;

/**
 * @author: sunqian
 * @date:2018年6月21日 上午10:55:25
 * @description: mysql存储每篇文章点击数各数据sql
 */

@Mapper
public interface ArticleInfoHourMapper {

	
	@Insert("insert into article_info_hour (union_id,platform_id,platform_type_id,article_id,json_nums,create_time,status) values(#{union_id},#{platform_id},#{platform_type_id},#{article_id},#{json_nums},#{create_time},#{status})")
	public int add(ArticleInfoHour articleInfoHour);
	
	@Delete("delete from article_info_hour where union_id=#{union_id}")
	public int delete(@Param("union_id") String union_id);
	
	@Update("update article_info_hour set json_nums = #{json_nums} where union_id = #{union_id}")
	public int updateJsonNums(ArticleInfoHour articleInfoHour);
	
	@Select("select * from article_info_hour where 1=1")
	public List<ArticleInfoHour> list();

	@Select("select * from article_info_hour where create_time = #{create_time}")
	public List<ArticleInfoHour> findByCreateTime(@Param("create_time") String create_time);
	
	@Select("select * from article_info_hour where union_id = #{union_id}")
	public List<ArticleInfoHour> findListByUnionId(@Param("union_id") String union_id);
	
	@Select("select platform_id from article_info_hour where union_id = #{union_id} group by platform_id")
	public List<String> findIdListByUnion(@Param("union_id") String union_id);
	
	@Select("select * from article_info_hour where union_id = #{union_id} and create_time=#{create_time}")
	public ArticleInfoHour findByUnionIdAndCreateTime(@Param("union_id") String union_id,@Param("create_time") String create_time);

	@SelectProvider(type=ArticleInfoHourProvider.class,method="findByPlatfromidAndCreateTime")
	public List<ArticleInfoHour> findByPlatfromidAndCreateTime(String platformIds,String createTime);
	
	@Select("select * from article_info_hour where platform_type_id = #{platform_type_id} and create_time=#{create_time}")
	public List<ArticleInfoHour> findByPlatfromtypeidAndCreateTime(@Param("platform_type_id")String platformTypeId,@Param("create_time") String create_time);

}
