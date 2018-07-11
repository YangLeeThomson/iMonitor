package com.ronglian.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDistribute{
	private String key;
	private Integer distribute;
	public CommentDistribute(String key,Integer distribute){
		super();
		this.key = key;
		this.distribute = distribute;
	}
	public CommentDistribute(){
		super();
	}
	
}
