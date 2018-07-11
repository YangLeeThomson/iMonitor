package com.ronglian.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ronglian.utils.UUIDUtil;

import lombok.Data;

/**
* @author: sunqian
* @date: 2018年6月14日 上午11:12:11
* @description: 自定义检测组实体类
*/

@Data
public class CustomMonitorGroup {
	
	private String id;
	private String userId;
	private String groupName;
	private String platformIdList;
	private List<Platform> platformList;
	@JsonFormat(pattern="yyyy/MM/dd hh:mm")
	private Date createTime;
	@JsonIgnore  
	private Date modifyTime;
	
	public CustomMonitorGroup() {
		this.createTime=new Date();
		this.modifyTime=new Date();
		this.id=UUIDUtil.getUUID32();
	}

}
