/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.model 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午5:22:25 
 */
package com.ronglian.model;

import java.util.Date;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

 /** 
 * @ClassName: PlatformComparedGroup 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午5:22:25  
 */
@Data
public class PlatformComparedGroup {

	private String groupId;
	private String userId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
	private String platformId;
	private String platformName;
}
