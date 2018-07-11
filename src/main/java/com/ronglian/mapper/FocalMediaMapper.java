package com.ronglian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.ronglian.mapper.provider.FocalMediaSqlProvider;
import com.ronglian.model.FocalMedia;

/**
* @author: 刘瀚博
* @date:2018年6月16日 上午10:28:20
* @description:重点媒体Mapper
*/
@Mapper
public interface FocalMediaMapper{
	@SelectProvider(type=FocalMediaSqlProvider.class,method="findFocalMediaByIds")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "name", column = "name"),
		@Result(property = "typeName", column = "type_name")
	})
	public List<FocalMedia> findFocalMediaByIds(String ids);
}
