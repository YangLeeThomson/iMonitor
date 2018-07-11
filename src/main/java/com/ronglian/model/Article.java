package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午5:53:07
* @description:原始文章
*/
@Data
public class Article {
	private String unionId;
	private String articleId;
	private String url;
	private String title;
	private String content;
	private int contentNum;
	private String report;
	private String source;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date publishTime;
	private String column;
	private int status;
	private String platformId;
	private String platformName;
	private String platformTypeId;
	private String platformTypeName;
	private int commentNum;
	private int clickNum;
	private int distinctUserClickNum;
	private int thumbsNum;
	private int awardNum;
	private int shareNum;
	private int subscribeNum;
	private int transNum;
	private float comprehensive;
	private int classification;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private int isOrigin;

}
