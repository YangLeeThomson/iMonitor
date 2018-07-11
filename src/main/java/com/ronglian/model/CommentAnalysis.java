package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommentAnalysis{
	private String unionId;
	/*private Map<String,Double> ner;
	private Map<String,Integer> typicalOpinion;
	private Map<String,Double> nerCloud;
	private Map<String,Double> nounCloud;
	private Map<String,Double> adjCloud;
	private Map<Integer,Double> emotionPer;*/
	/**
	 *数据类型：
	 * 1典型意见,2,3,4,5,
	 */
	private Integer type;
	private Double score;
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date analysisTime;
	private String content;
	private Integer num;
	public String getUnionId(){
		return unionId;
	}
	public void setUnionId(String unionId){
		this.unionId = unionId;
	}
	public Integer getType(){
		return type;
	}
	public void setType(Integer type){
		this.type = type;
	}
	public Double getScore(){
		return score;
	}
	public void setScore(Double score){
		this.score = score;
	}
	public Date getAnalysisTime(){
		return analysisTime;
	}
	public void setAnalysisTime(Date analysisTime){
		this.analysisTime = analysisTime;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public CommentAnalysis(){
		super();
	}
	public Integer getNum(){
		return num;
	}
	public void setNum(Integer num){
		this.num = num;
	}
	public CommentAnalysis(String unionId,Integer type,Double score,Date analysisTime,String content,Integer num){
		super();
		this.unionId = unionId;
		this.type = type;
		this.score = score;
		this.analysisTime = analysisTime;
		this.content = content;
		this.num = num;
	}
}