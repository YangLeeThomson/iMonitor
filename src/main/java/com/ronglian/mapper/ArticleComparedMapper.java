/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.mapper 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午3:02:13 
 */
package com.ronglian.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronglian.model.ArticleComparedGroup;

 /** 
 * @ClassName: ArticleComparedMapper 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午3:02:13  
 */
@Mapper
public interface ArticleComparedMapper {

	 @Insert("insert into article_compare_group(group_id,user_id,article_id,create_time)"
	            + " values "
	            + "(#{groupId},#{userId},#{articleId},#{createTime})") 
	public int insertArticleGroup(ArticleComparedGroup group);
	/* 
	 	private String groupId;
	private String userId;
	private String articleId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
	*/
	 @Delete("delete from article_compare_group "
				+ " where group_id = #{groupId} "
				)
	public int deleteGroup(String groupId);
	 
	@Select("select group_id,create_time from article_compare_group where user_id = #{userId} group by group_id,create_time order by create_time desc")
	@Results({
		@Result(property = "groupId", column = "group_id"),
		@Result(property = "createTime", column = "create_time")
	})
	public List<ArticleComparedGroup> selectGroupListByUserId(String userId);
		
	@Select("select group_id from article_compare_group where user_id = #{userId} ")
	public List<String> selectGroupIdListByUserId(String userId);
	
	@Select("select article_id from article_compare_group where group_id = #{groupId} ")
	public List<String> selectArticleIdListByGroupId(String groupId);
}
