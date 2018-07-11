/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service 
 * @author: YeohLee   
 * @date: 2018年6月26日 下午2:45:11 
 */
package com.ronglian.service;

import com.ronglian.common.JsonResult;

 /** 
 * @ClassName: ScreenShotService 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月26日 下午2:45:11  
 */
public interface ScreenShotService {

	public JsonResult modifyScreenShot(String unionId,String screenShot);
}
