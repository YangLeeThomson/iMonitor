package com.ronglian.common.exception;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
 * <p>Description: 自定义异常基类 </p> 
 * <p>Author:huang/黄硕</p>
 */
public class BaseException extends RuntimeException{

	/**
	 * <p>Description: </p>
	 * <p>Author:huang/黄硕</p>
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = - 5544771150110827762L;
	
	/**
	 * <p>Description:异常分类 </p>
	 * <p>Author:huang/黄硕</p>
	 * @Fields errorcode 
	 */
	private ExceptionType exceptionType;

	/**
	 * <p>Description: 异常描述 </p>
	 * <p>Author:huang/黄硕</p>
	 * @Fields errorcode 
	 */
	private ExceptionDescription exceptionDescription;
	
	/**
	 * <p>Description:其他信息 </p>
	 * <p>Author:huang/黄硕</p>
	 * @Fields errorcode 
	 */
	private String message;
	
	
	/**
	 * <p>Description: 用以封装异常结构链 </p>
	 * <p>Author:huang/黄硕</p>
	 * @Fields cause 
	 */
	private Throwable cause;
	
	public Throwable getCause(){
		return cause;
	}

	public void setCause(Throwable cause){
		this.cause = cause;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public ExceptionDescription getExceptionDescription() {
		return exceptionDescription;
	}

	public void setExceptionDescription(ExceptionDescription exceptionDescription) {
		this.exceptionDescription = exceptionDescription;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BaseException(){
		super();
	}

	public BaseException(Throwable cause){
		super();
		this.cause = cause;
	}

	public BaseException(ExceptionType exceptionType,ExceptionDescription exceptionDescription){
		super();
		this.exceptionType = exceptionType;
		this.exceptionDescription = exceptionDescription;
	}
	
	public BaseException(ExceptionType exceptionType,ExceptionDescription exceptionDescription,String message){
		super();
		this.exceptionType = exceptionType;
		this.exceptionDescription = exceptionDescription;
		this.message = message;
	}

	public BaseException(ExceptionType exceptionType,ExceptionDescription exceptionDescription,String message,Throwable cause){
		super();
		this.exceptionType = exceptionType;
		this.exceptionDescription = exceptionDescription;
		this.message = message;
		this.cause = cause;
	}
}
