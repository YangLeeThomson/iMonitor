package com.ronglian.repository;

import java.io.IOException;
import java.util.List;

public interface CopyrightMonitorRepository {

	String getOriginalArticle(String unionId) throws IOException;
	
	String getOriginalArticleList(List<String> unionIdList) throws IOException;

	String getTransArticle(String webpageCode) throws IOException;

	String getTitleList(String titleWord,int page,int pageSize) throws IOException;		
	
	String getOriginalArticleListByArticleId(String articleId) throws IOException;

}
