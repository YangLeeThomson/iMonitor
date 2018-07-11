package com.ronglian.common;
/**
* @author: 黄硕/huangshuo
* @date:2018年5月24日 下午8:49:10
* @description:1官方媒体、2政府网站、3商业媒体、4自媒体、5国外媒体
*/


public enum MediaType {
	G_F_M_T("官方媒体",1),Z_F_W_Z("政府网站",2),S_Y_M_T("商业媒体",3),Z_M_T("自媒体",4),G_W_M_T("国外媒体",5);
	
	private String name;
	private int index;
	
	private MediaType(String name,int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
