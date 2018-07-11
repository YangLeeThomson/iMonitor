package com.ronglian.common;
/**
* @author: 黄硕/huangshuo
* @date:2018年5月3日 下午3:05:40
* @description:描述
*/
public enum ResultCode {
	
	USER_NULL("101","未传参用户名或密码"),
	
	USER_ERROR("102","用户名或密码不正确"),
	
	TIME_OUT("103","未登录或登录超时"),
	
	ERROR("000","错误"),
	/** 成功 */  
    SUCCESS("200", "成功"),  
      
    /** 没有登录 */  
    NOT_LOGIN("400", "没有登录"),  
      
    /** 发生异常 */  
    EXCEPTION("401", "发生异常"),  
      
    /** 系统错误 */  
    SYS_ERROR("402", "系统错误"),  
      
    /** 参数错误 */  
    PARAMS_ERROR("403", "参数错误 "),  
      
    /** 不支持或已经废弃 */  
    NOT_SUPPORTED("410", "不支持或已经废弃"),  
      
    /** AuthCode错误 */  
    INVALID_AUTHCODE("444", "无效的AuthCode"),  
  
    /** 太频繁的调用 */  
    TOO_FREQUENT("445", "太频繁的调用"),  
      
    /** 未知的错误 */  
    UNKNOWN_ERROR("499", "未知错误");  
      
    private String val;  
    private String msg; 
    
    private ResultCode(String value, String msg){  
        this.val = value;  
        this.msg = msg;  
    }  
      
    public String val() {  
        return val;  
    }  
  
    public String msg() {  
        return msg;  
    }  
 
}
