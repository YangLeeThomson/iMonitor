package com.ronglian.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 
 * @Description: 各种列表中所用原文和转载文章实体
 * @author sunqian
 * @date 2018年6月15日 下午5:44:35
 */

@Data
public class TransAndOriginalArticle implements Serializable {

	private static final long serialVersionUID = 1L;

	// 主键id
	@JsonIgnore
	private String id;
	// webpageCode
	private String webpageCode;
	// 转载媒体
	private String tranMediaName;
	// 转载url
	private String transUrl;
	// 转载文章标题
	private String transTitle;
	// 转载文章正文
	@JsonIgnore
	private String transContent;
	// 转载文章作者
	@JsonIgnore
	private String transReporter;
	// 转载文章发布时间（utc时间戳）
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date transPublishTime;
	// 原文id
	private String unionId;
	// 原文id
	private String articleId;
	// 原文所属平台
	private String originalPlatform;
	// 原文url
	@JsonIgnore
	private String originalUrl;
	// 原文标题
	private String originalTitle;
	// 原文发布时间（utc时间戳）
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date originalPublishTime;

}
