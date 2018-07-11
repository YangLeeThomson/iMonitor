package com.ronglian.common.exception;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingServletRequestParameterException;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月8日 下午7:11:09
* @description:全局异常处理
*/
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public JsonResult missingServletRequestParameterExceptionHandle(Exception e) {
		log.error("请求参数异常", e);
		return new JsonResult(ResultCode.ERROR,e.getMessage());
	}
	
	@ExceptionHandler(BaseException.class)
	public JsonResult baseExceptionHandle(BaseException e) {
		String resultMessage = e.getExceptionType().getDescription()+":"+e.getExceptionDescription().getDescription();
		if(!StringUtils.isEmpty(e.getMessage())) {
			resultMessage = resultMessage+":"+e.getMessage();
		}
		log.error("业务异常:"+resultMessage, e);
	    return new JsonResult(ResultCode.ERROR,resultMessage);
	}

	@ExceptionHandler(Exception.class)
	public JsonResult defaultExceptionHandle(Exception e) {
		log.error("未处理异常", e);
	    return new JsonResult(ResultCode.UNKNOWN_ERROR,e.getMessage());
	}
}
