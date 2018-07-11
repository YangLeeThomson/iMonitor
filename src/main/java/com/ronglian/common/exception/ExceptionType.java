package com.ronglian.common.exception;
/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 下午3:19:38
* @description:描述
*/
public enum ExceptionType {
	
	PLATFORM("10001","平台模块异常"),
	
	PLATFORM_TYPE("20001","平台类型模块异常"),
	
	ARTICLE_PLATFORM_MAPPING("30001","文章平台映射一场"),
	
	JOB("40001","定时任务模块异常"),
	
	PLATFORM_MEDIA_TYPE("50001","平台类型模块异常"),
	
	PLATFORM_TRANS("60001","文章转载模块异常"),
	PLATFORM_TRANS_CLASSIFICATION("60002","文章转载分类模块异常"),
	
	WEEKED_CIRCLE("70001","周环比结果不存在");
	private String exceptionType;
	
	private String description;
	
	private ExceptionType(String exceptionType,String description) {
		this.exceptionType = exceptionType;
		this.description = description;
	}
	
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String discription) {
		this.description = discription;
	}
	
	
}
