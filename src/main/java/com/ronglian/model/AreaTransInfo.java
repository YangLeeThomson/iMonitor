/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.model 
 * @author: YeohLee   
 * @date: 2018年5月29日 下午11:21:34 
 */
package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

 /** 
 * @ClassName: AreaTransInofo 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月29日 下午11:21:34  
 */
@Data
public class AreaTransInfo {

	private String articleId;
	private String originalTitle;
	private String url;
	private String transTitle;
	private String province;
	private String unionId;
	private String transMeida;
	private String platformId;
	private String platformName;
	private Integer rowNum;
//	private String platformTypeId;
//	private Date publishTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date transTime;
	
}
