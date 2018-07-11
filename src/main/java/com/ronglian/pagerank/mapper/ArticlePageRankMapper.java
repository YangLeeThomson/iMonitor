package com.ronglian.pagerank.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.ronglian.pagerank.model.ArticlePageRank;


@Mapper
public interface ArticlePageRankMapper{
	@Insert("insert into article_page_rank (union_id,article_id,platform_type_id,page_rank,relative_score,publish_time,url,media_type,title,report_source) values(#{unionId},#{articleId},#{platformTypeId},#{pageRank},#{relativeScore},#{publishTime},#{url},#{mediaType},#{title},#{reportSource})")
	public int add(ArticlePageRank articlePageRank);
	
	@Delete("delete from article_page_rank where id=#{id}")
	public int delete(@Param("id") String id);
	
	@Select("select * from article_page_rank where union_id=#{unionId} ")
	@Results({
		@Result(property = "unionId", column = "union_id"),
		@Result(property = "articleId", column = "article_id"),
		@Result(property = "platformTypeId", column = "platform_type_id"),
		@Result(property = "pageRank", column = "page_rank"),
		@Result(property = "relativeScore", column = "relative_score"),
		@Result(property = "publishTime", column = "publish_time"),
		@Result(property = "url", column = "url"),
		@Result(property = "mediaType", column = "media_type"),
		@Result(property = "title", column = "title"),
		@Result(property = "reportSource", column = "report_source")
	})
	public List<ArticlePageRank> findByUnionId(@Param("unionId") String unionId);
}