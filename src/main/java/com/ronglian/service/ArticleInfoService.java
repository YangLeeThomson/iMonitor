/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午4:31:32 
 */
package com.ronglian.service;

import com.ronglian.common.JsonResult;

 /** 
 * @ClassName: ArticleInfoService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午4:31:32  
 */
public interface ArticleInfoService {

	public JsonResult getArticleInfo(String id);
	
	public JsonResult getImonitorStatus(String id);
	
	public JsonResult getPlatformAnalysis(String id);
	
	public JsonResult getTransArticleList(String id,int page,int pageSize,Integer transType,Integer tort);
	
	public JsonResult getTransCurrent(String id);
	
	public JsonResult getArticleTransCounted(String id,int page,int pageSize);
}
