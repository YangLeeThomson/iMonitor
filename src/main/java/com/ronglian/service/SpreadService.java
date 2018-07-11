/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年6月20日 下午10:29:46 
 */
package com.ronglian.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ronglian.model.WeekedCircle;

 /** 
 * @ClassName: SpreadService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月20日 下午10:29:46  
 */
public interface SpreadService {
	
	public List<WeekedCircle> getWeekedCircleAccounted(Date startTime,Integer accountType,String typeId,List<String> platformIdList);
	
	public List<Map> getPlatformTrans2(String platformTypeId,List<String> platformIdList,Date startTime,
			Integer accountType);
	
	public Map getSpreadAreaTransList(String platformTypeId,List<String> platformIdList,String province,Date startTime,Integer accountType,Integer pageNo,Integer pageSize);

	public Map getPlatformArticleInfo(List<String> platformIdList,String platformId,Date startTime,Integer accountType);
	
	public List<Map> getPlatformArticleNumAccounted(Date startTime,Integer accountType,String platformTypeId,List<String> platformIdList);
	
	public Map getOriginalArticleNumAccounted(Date startTime,Integer accountType,String typeId,List<String> platformIdList);
	
	public List<Map> getSpreadAreaCurrent(String platformTypeId,List<String> platformIdList,Date startTime,Integer accountType);
	
	public Integer getProvienceTransnum(String platformTypeId,List<String> platformIdList,String province,Date startTime,Integer accountType);
	
	public List<Map> getArticleListOrederByTransNum(String platformTypeId,String articleTypeId,Date startTime,Integer accountType,Integer pageNo,Integer pageSize);
	
	public List<Map> getPlatformTrans(String platformTypeId,List<String> platformIdList,Date startTime,Integer accountType);
}
