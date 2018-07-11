package com.ronglian.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午3:29:24
* @description:转载媒体类型占比
*/
public interface PlatformTransMediaTypeService {

	List<HashMap<String,String>> transMediaTypeProportion(int accountType,String platformTypeId,String platformId,String groupId,Date startTime,Date endTime)throws IOException;
}
