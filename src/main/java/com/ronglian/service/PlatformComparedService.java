/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午4:56:19 
 */
package com.ronglian.service;

import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.ronglian.common.JsonResult;

 /** 
 * @ClassName: PlatformComparedService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午4:56:19  
 */
public interface PlatformComparedService {

	public JsonResult addPlatformGroup(Map map);
	
	public JsonResult deletePlatformGroupCompare(String groupId);
	
	public JsonResult searchPlatformGroupCompare(String keyword);
	
	public JsonResult getPlatformGroupCompare(String userId);
	
	public JsonResult findPlatformGroupCompare(String userId,Date today,Integer accountType);
}
