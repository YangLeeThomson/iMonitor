/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.controller 
 * @author: YeohLee   
 * @date: 2018年6月26日 下午2:24:31 
 */
package com.ronglian.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.service.ScreenShotService;

 /** 
 * @ClassName: ScreenShotController 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月26日 下午2:24:31  
 */
@RestController
@Slf4j
@RequestMapping("/page")
public class ScreenShotController {

	@Autowired
	private ScreenShotService screenService;
	
	/** 
	* @Title: modifyArticleScreenshot 
	* @Description: 截屏接口 
	* @param unionId
	* @param screenshot
	* @return JsonResult
	* @author YeohLee
	* @date 2018年6月26日下午2:57:58
	*/ 
	@RequestMapping(value="/screen/shot",method=RequestMethod.GET)
	public JsonResult modifyArticleScreenshot(@RequestParam("webpageCode") String webpageCode,@RequestParam("screenshot") String screenshot){
		return this.screenService.modifyScreenShot(webpageCode, screenshot);
	}
}
