package com.ronglian.mapper.provider;
/**
* @author: sunqian
* @date:2018年5月18日 上午10:38:35
* @description:描述
*/
public class ArticleInfoHourProvider {
	
	public String findByPlatfromidAndCreateTime(String platformIds,String createTime) {
		return "select * from article_info_hour where platform_id in "+platformIds+" and create_time='"+createTime+"'";  
	}
}
