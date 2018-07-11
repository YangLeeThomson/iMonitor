/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年6月26日 下午2:49:03 
 */
package com.ronglian.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ronglian.common.Constants;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.repository.ArticleTransRepository;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.ScreenShotService;
import com.ronglian.utils.DateTimeUtils;
import com.ronglian.utils.JsonUtils;
import com.ronglian.utils.Utils;

 /** 
 * @ClassName: ScreenShotServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月26日 下午2:49:03  
 */
@Service
public class ScreenShotServiceImpl implements ScreenShotService {

	@Autowired
	private ElasticRepository esDao;
	
	@Autowired
	private ArticleTransRepository esTransDao;
	
	/* (non-Javadoc)
	 * @see com.ronglian.service.ScreenShotService#modifyScreenShot(java.lang.String, java.lang.String)
	 */
	@Override
	public JsonResult modifyScreenShot(String webpageCode, String screenShot) {
		// TODO Auto-generated method stub
		String str = null;
		try {
			str = this.esTransDao.getTransInfo(webpageCode);
			System.out.println("测试请求数据："+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.isBlank(str)){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		Map<String, Object> transforMap = JsonUtils.parseJSON2Map(str);
		Map<String, Object> transform1 = (Map<String, Object>) transforMap.get("hits");
		if(transform1 == null){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> transform2 = (List<Map>) transform1.get("hits");
		if(transform2 == null || transform2.size() == 0){
			return new JsonResult(ResultCode.SUCCESS,"success",null);
		}
		List<Map> result = new ArrayList<Map>();
		int i = 1;
		for(Map<String, Object> transform3:transform2){
			Map<String, Object> transform4 = (Map<String, Object>) transform3.get("_source");
//			String id = (String) transform3.get("_id");
			if(transform4 == null){
				continue;
			}
			Map map = new HashMap();
			//"记录时间  格式： 2018-06-26 15：32  精确到分钟"
			Long timeStamp1 = (Long) transform4.get("originArticlePubTime");
			Date originArticlePubTime = new Date(timeStamp1);
			map.put("screenshot",screenShot );
			JSONObject articleObject = (JSONObject) JSONObject.toJSON(map);
			String query = "{\r\n" + 
					"    \"doc\" : "+articleObject.toJSONString()+
					"}";
			try {
				this.esDao.updateES("POST","/"+Constants.ES_INDEX_IMONITOR_TRANS_INFO_PERFIX+Utils.dateToString(originArticlePubTime,Constants.DEFAULT_DATE_FORMAT_YMD2)+"/imonitor/"+webpageCode,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new JsonResult();
	}

}
