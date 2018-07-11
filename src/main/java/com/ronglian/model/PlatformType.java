package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月17日 下午3:46:33
* @description:描述
*/
@Data
public class PlatformType {
	private String id;
	private String name;
	@JsonIgnore  
	private String tenantId;
	@JsonIgnore  
	private Date createTime;
	@JsonIgnore  
	private Integer status;
}
