package com.ronglian.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月15日 下午3:39:08
* @description:描述
*/
@Data
public class Platform {
	private String id;
	
	private String name;
	
	private String serial;
	
	private String platformTypeId;
	
	private String platformTypeName;
	
	private String tenantId;
	
	private Date createTime;
	
	private Integer status;
	
	private Integer weight;
}
