package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


/**
 * 
 * @Description: 转载文章实体，用在版权监测页面
 * @author public
 * @date 2018年6月15日 下午5:15:20
 */
@Data
public class TransArticle{

	// webpageCode
	private String webpageCode;
	// 转载媒体
	private String mediaName;
	// 转载文章标题
	private String transTitle;
	// 转载url
	private String transUrl;
	// 转载文章发布时间（utc时间戳）
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date transPublishTime;
	// 转载文章正文
	private String transContent;
	// 转载文章正文字数
	private int transContentNum;
	// 转载文章作者
	private String transReporter;
	// 转载文章标注来源
	private String transAnnotationSource;
	// 转载文章截图地址
	private String transPictureUrl;
	// 转载文章截图时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date transPictureTime;
	// 转载文章es分数
	private double transEsScore;
	// 转载文章相似度
	private double transSimilarity;
	// 是否侵权，1：版权存疑，0：注明来源
	private int isTort; 

}
