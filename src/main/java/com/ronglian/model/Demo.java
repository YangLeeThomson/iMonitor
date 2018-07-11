package com.ronglian.model;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月8日 上午10:50:11
* @description:demo
*/
public class Demo{
	
	private int id;
	private String name;
	
	public Demo() {
		super();
	}
	public Demo(int id,String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
