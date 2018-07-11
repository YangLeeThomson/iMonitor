package com.ronglian.model;

import java.util.Date;

import lombok.Data;

@Data
public class TransInfo {

	private String unionId; // platformTypeId_articleId
	private String originTitle; // 原文标题
	private String platformId; // 平台id
	private String platformName; // 平台名称
	private String platformTypeId; // 平台类型id
	private String platformTypeName; // 平台类型名称
	private String articleId; // 原始文章id
	private Date originArticlePubTime; // 原始文章发布时间
	private int isOrigin; // 原始文章发布时间
	
	private String title; // 标题
	private String content; // 正文
	private String crawSourceDomain; // 采集来源domain
	private String crawlSource; // 采集来源
	private String province; // 省份
	private int mediaType; // 媒体类型
	private int channel; // 渠道
	private Date reportTime; // 转载文章发布时间
	private String webpageCode; // 转载文章id
	private String webpageUrl; // 转载文章url
	private double transEsScore;// 转载文章es分数
	private double transSimilarity;// 转载文章相似度
	private String reportSource; // 发布来源
	private String screenshot; //
	private int isTort; // 是否侵权，1：版权存疑，0：注明来源

	private Date createTime; // 创建时间
	
	public TransInfo(){
		createTime=new Date();
	}

	public TransInfo(String unionId,String originTitle,String platformId,String platformName,String platformTypeId,String platformTypeName,String articleId,Date originArticlePubTime,String title,String content,String crawSourceDomain,String crawlSource,String province,int mediaType,int channel,Date reportTime,String webpageCode,String webpageUrl,float nlpScore,String reportSource,String screenshot,int isTort,Date createTime){
		super();
		this.unionId = unionId;
		this.originTitle = originTitle;
		this.platformId = platformId;
		this.platformName = platformName;
		this.platformTypeId = platformTypeId;
		this.platformTypeName = platformTypeName;
		this.articleId = articleId;
		this.originArticlePubTime = originArticlePubTime;
		this.title = title;
		this.content = content;
		this.crawSourceDomain = crawSourceDomain;
		this.crawlSource = crawlSource;
		this.province = province;
		this.mediaType = mediaType;
		this.channel = channel;
		this.reportTime = reportTime;
		this.webpageCode = webpageCode;
		this.webpageUrl = webpageUrl;
		this.transSimilarity = nlpScore;
		this.reportSource = reportSource;
		this.screenshot = screenshot;
		this.isTort = isTort;
		this.createTime = createTime;
	}
	
}