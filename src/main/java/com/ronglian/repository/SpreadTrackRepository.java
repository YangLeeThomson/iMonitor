package com.ronglian.repository;

import java.io.IOException;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月28日 下午7:00:28
* @description:传播路径
*/
public interface SpreadTrackRepository {
	String getArticle(String unionId)throws IOException ;
	String getTransArticles(String unionId)throws IOException ;
}
