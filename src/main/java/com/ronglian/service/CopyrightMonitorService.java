package com.ronglian.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.PageResult;
import com.ronglian.model.Article;
import com.ronglian.model.OriginalArticle;
import com.ronglian.model.TransArticle;

public interface CopyrightMonitorService {

	Article getOriginalArticle(String unionId);

	TransArticle getTransArticle(String webpageCode);

	JSONArray getTitleList(String titleWord,int page,int pageSize);

	PageResult<JSONObject> getArticleList(String titleWord, int page, int pageSize);

}
