package com.ronglian.common.exception;


/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 下午2:49:49
* @description:异常错误码
*/
public enum ExceptionDescription {
	
	UNKNOW("00000","未定义错误"),
	
	PARAMETER_LACK("10001","参数缺失"),
	
	PARAMETER_ENTITY_ISNULL("10002","参数缺失"),
	
	OBJECT_NOT_EXISTS("20001","数据不存在"),
	
	OBJECT_ALREADY_EXISTS("20002","数据已经存在");
	
	private String code;
	
	private String description;
	
	private ExceptionDescription(String code,String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
