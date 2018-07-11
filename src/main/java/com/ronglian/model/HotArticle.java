package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月28日 上午2:23:09
* @description:热门作品
*/
@Data
public class HotArticle {
	private String unionId;
	private int inx;
	private String articleId;
	private String platformId;
	private String platformName;
	private String platformTypeId;
	private String platformTypeName;
	private String title;
	private int transNum;
	private int commentNum;
	private int clickNum;
	private int thumbsNum;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publishTime;
	private String author;
}
