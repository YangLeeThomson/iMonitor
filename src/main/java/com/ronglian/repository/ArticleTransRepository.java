/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.repository 
 * @author: YeohLee   
 * @date: 2018年6月20日 下午7:23:27 
 */
package com.ronglian.repository;

import java.io.IOException;

 /** 
 * @ClassName: ArticleTransRepository 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月20日 下午7:23:27  
 */
public interface ArticleTransRepository {

	String getTransList(String unionId,int page,int pageSize ) throws IOException;
	
	String getTransListByTransTypeAndTort(String unionId,int page,int pageSize,Integer transType,Integer tort) throws IOException;
	
	String getTransListTotal(String unionId) throws IOException;
	
	String getTransInfo(String webpageCode) throws IOException;
}
