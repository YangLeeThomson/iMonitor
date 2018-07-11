package com.ronglian.pagerank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class ArticlePageRank{
	private String id;
	private String unionId;
	private String articleId;
	private String platformTypeId;
	private BigDecimal pageRank;
	private BigDecimal relativeScore;
	private Date publishTime;
	private String url;
	private Integer mediaType;
	private String title;
	private String reportSource;
	public ArticlePageRank(String id,String unionId,String articleId,String platformTypeId,BigDecimal pageRank,BigDecimal relativeScore,Date publishTime,String url,String title,Integer mediaType,String reportSource){
		super();
		this.id=id;
		this.unionId = unionId;
		this.articleId = articleId;
		this.platformTypeId = platformTypeId;
		this.pageRank = pageRank;
		this.relativeScore = relativeScore;
		this.publishTime = publishTime;
		this.url = url;
		this.mediaType = mediaType;
		this.title = title;
		this.reportSource = reportSource;
	}
}