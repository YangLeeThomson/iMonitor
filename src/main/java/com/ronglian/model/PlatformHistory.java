package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月4日 上午10:27:14
* @description:平台浏览记录
*/
@Data
public class PlatformHistory {
	private String platformId;
	private String platformTypeId;
	private String platformName;
	private String platformTypeName;
	@JsonIgnore  
	private String userId;
	@JsonIgnore  
	private Date createTime;
	
}
