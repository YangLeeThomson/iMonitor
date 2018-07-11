/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.repository 
 * @author: YeohLee   
 * @date: 2018年6月21日 上午11:14:48 
 */
package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

 /** 
 * @ClassName: SpreadRepository 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月21日 上午11:14:48  
 */
public interface SpreadRepository {

	public String getPlatformTransOrder(int accountType,String platformTypeId,List<String> platformIdList,Date startTime,Date endTime) throws IOException;
}
