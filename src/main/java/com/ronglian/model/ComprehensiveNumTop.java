package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ComprehensiveNumTop {
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publish_time;
	private String title;
	private String platform;
	private int trans_num;
	private int comment_num;
	private int read_num;
	private float comprehensive_num;
	private String articleId;
	private String unionId;
}
