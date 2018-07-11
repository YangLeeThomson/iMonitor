/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年5月27日 下午11:57:57 
 */
package com.ronglian.service;

import java.util.List;

import com.ronglian.model.Platform;

 /** 
 * @ClassName: CommonService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月27日 下午11:57:57  
 */
public interface CommonService {

	/**
	 * 
	* @Title: putPlatformIntoRedis 
	* @Description: TODO  void
	* @author YeohLee
	* @date 2018年5月28日上午12:11:07
	 */
	public void putPlatformIntoRedis();
	
	public void putPlatformTypeIntoRedis();
	
	public void putPlatformMappingType();
	
	public String getPlatformNameById(String platformId);
	
	public String getPlatformTypeNameById(String platformTypeId);
	
	public String getPlatformTypeByPlatformId(String platformId);
	
	public List<Platform> getAllPlatform();
	
}
