package com.ronglian.service;
/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午8:09:55
* @description:描述
*/

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ArticlePublishTrendService {
	List<HashMap<String,String>> articlePublishTrend(int accountType,String platformTypeId,String platformId,String groupId,Date bTime,Date eTime) throws IOException;
}
