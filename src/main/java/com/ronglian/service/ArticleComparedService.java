/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午2:35:55 
 */
package com.ronglian.service;

import java.util.List;
import java.util.Map;

import com.ronglian.common.JsonResult;

 /** 
 * @ClassName: ArticleComparedService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午2:35:55  
 */
public interface ArticleComparedService {

	public int addGroup(Map group);
	
	public int delGroup(String groupId);
	
	public List<String> getGroupListByUserId(String userId);
	
	public List<String> getArticleIdListByGroupId(String groupId);
	
	public JsonResult getArticleListByTitle(String keyword);
	
	public JsonResult getArticleCompared(String userId);
}
