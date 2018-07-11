package com.ronglian.model;

public class FocalMedia{
	private String id;
	private String name;
	private String typeName;
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public FocalMedia(){
		super();
	}
	public String getTypeName(){
		return typeName;
	}
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	public FocalMedia(String id,String name,String typeName){
		super();
		this.id = id;
		this.name = name;
		this.typeName = typeName;
	}
}