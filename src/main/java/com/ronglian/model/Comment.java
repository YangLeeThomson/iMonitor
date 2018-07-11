package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Comment{
	/**
	 * id为articleId+评论的index
	 */
	private String id;
	private String userName;
	private String region;
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publishTime;
	private Integer upNum;
	private Integer downNum;
	private String unionId;
	private String content;
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getRegion(){
		return region;
	}
	public void setRegion(String region){
		this.region = region;
	}
	public Date getPublishTime(){
		return publishTime;
	}
	public void setPublishTime(Date publishTime){
		this.publishTime = publishTime;
	}
	public Integer getUpNum(){
		return upNum;
	}
	public void setUpNum(Integer upNum){
		this.upNum = upNum;
	}
	public Integer getDownNum(){
		return downNum;
	}
	public void setDownNum(Integer downNum){
		this.downNum = downNum;
	}
	public String getUnionId(){
		return unionId;
	}
	public void setUnionId(String unionId){
		this.unionId = unionId;
	}
	public Comment(){
		super();
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public Comment(String id,String userName,String region,Date publishTime,Integer upNum,Integer downNum,String unionId,String content){
		super();
		this.id = id;
		this.userName = userName;
		this.region = region;
		this.publishTime = publishTime;
		this.upNum = upNum;
		this.downNum = downNum;
		this.unionId = unionId;
		this.content = content;
	}
	public Comment(String id,String userName,Date publishTime,String unionId,String content){
		super();
		this.id = id;
		this.userName = userName;
		this.publishTime = publishTime;
		this.unionId = unionId;
		this.content = content;
	}
	
}