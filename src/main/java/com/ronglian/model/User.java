package com.ronglian.model;

import java.util.Date;

import com.ronglian.utils.UUIDUtil;

import lombok.Data;

/**
* @author: sunqian
* @date: 2018年6月14日 上午10:12:11
* @description: 用户实体类
*/
@Data
public class User{
	
	private String id;
	private String username;
	private String password;
	private String tenant_id;
	private Date create_time;
	private int status;
	
	public User() {
		this.id=UUIDUtil.getUUID32();
	}
}
