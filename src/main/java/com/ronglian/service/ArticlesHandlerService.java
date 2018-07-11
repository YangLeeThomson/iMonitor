package com.ronglian.service;

import java.util.List;

import com.ronglian.model.OriginalArticle;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月16日 下午6:45:48
* @description:文章
*/
public interface ArticlesHandlerService {

	public void handleOriginalArticle(List<OriginalArticle> articles);
	
	public void pushToSimilarityService(); 
	
	public void articleInfo(String jsonData);
}
