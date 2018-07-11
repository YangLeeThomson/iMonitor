package com.ronglian.model;

import java.util.Date;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
@Data
public class Log{
	private Integer id;
	private Integer category;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date logTime;
	private String url;
	private  String title;
	private String keyWord;
	private String content;
	public Log(Integer category,String url,String title,String keyWord,String content){
		super();
		this.category = category;
		this.url = url;
		this.title = title;
		this.keyWord = keyWord;
		this.content = content;
	}
	
}
