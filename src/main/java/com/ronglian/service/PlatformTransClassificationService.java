package com.ronglian.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午3:49:45
* @description:描述
*/
public interface PlatformTransClassificationService {

	List<HashMap<String,String>> transArticleClassificationProportion(int accountType,String platformTypeId,String platformId,String groupId,Date startTime,Date endTime)throws IOException;
	
	List<HashMap<String,String>> transArticleClassificationTop10(int classification,String other,int accountType,String platformTypeId,String platformId,String groupId,Date startTime,Date endTime,int pageNo,int pageSize)throws IOException;
}
