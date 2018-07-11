package com.ronglian.model;

import java.util.Date;

public class FocalMediaTrans{
	private String id;
	private String unionId;
	private String platformId;
	private String originalId;
	private String mediaId;
	private String transTitle;
	private String transUrl;
	private Date transTime;
	//1首页2非首页
	private Integer publishStatus;
	private Integer homePageSeconds;
	private String originalTitle;
	private Date updateTime;
	private Double percent;
	public String getPlatformId(){
		return platformId;
	}
	public void setPlatformId(String platformId){
		this.platformId = platformId;
	}
	public String getOriginalId(){
		return originalId;
	}
	public void setOriginalId(String originalId){
		this.originalId = originalId;
	}
	public String getMediaId(){
		return mediaId;
	}
	public void setMediaId(String mediaId){
		this.mediaId = mediaId;
	}
	public String getTransTitle(){
		return transTitle;
	}
	public void setTransTitle(String transTitle){
		this.transTitle = transTitle;
	}
	public Date getTransTime(){
		return transTime;
	}
	public void setTransTime(Date transTime){
		this.transTime = transTime;
	}
	public Integer getPublishStatus(){
		return publishStatus;
	}
	public void setPublishStatus(Integer publishStatus){
		this.publishStatus = publishStatus;
	}
	public Integer getHomePageSeconds(){
		return homePageSeconds;
	}
	public void setHomePageSeconds(Integer homePageSeconds){
		this.homePageSeconds = homePageSeconds;
	}
	public FocalMediaTrans(){
		super();
	}
	public String getOriginalTitle(){
		return originalTitle;
	}
	public void setOriginalTitle(String originalTitle){
		this.originalTitle = originalTitle;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public Double getPercent(){
		return percent;
	}
	public void setPercent(Double percent){
		this.percent = percent;
	}
	public String getTransUrl(){
		return transUrl;
	}
	public void setTransUrl(String transUrl){
		this.transUrl = transUrl;
	}
	public String getUnionId(){
		return unionId;
	}
	public void setUnionId(String unionId){
		this.unionId = unionId;
	}
	public FocalMediaTrans(String id,String unionId,String platformId,String originalId,String mediaId,String transTitle,String transUrl,Date transTime,Integer publishStatus,Integer homePageSeconds,String originalTitle,Date updateTime,Double percent){
		super();
		this.id = id;
		this.unionId = unionId;
		this.platformId = platformId;
		this.originalId = originalId;
		this.mediaId = mediaId;
		this.transTitle = transTitle;
		this.transUrl = transUrl;
		this.transTime = transTime;
		this.publishStatus = publishStatus;
		this.homePageSeconds = homePageSeconds;
		this.originalTitle = originalTitle;
		this.updateTime = updateTime;
		this.percent = percent;
	}
}