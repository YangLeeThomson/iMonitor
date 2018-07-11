package com.ronglian.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月20日 下午3:47:49
* @description:描述
*/
public interface PlatformTransChannelService {
	public List<HashMap<String,String>> transChannelProportion(int accountType,String platformTypeId,String platformId,String groupId,Date startTime,Date endTime)throws IOException;
}
