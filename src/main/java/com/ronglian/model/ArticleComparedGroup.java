/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.model 
 * @author: YeohLee   
 * @date: 2018年6月19日 下午2:40:22 
 */
package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

 /** 
 * @ClassName: ArticleComparedGroup 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月19日 下午2:40:22  
 */
@Data
public class ArticleComparedGroup {

	private String groupId;
	private String userId;
	private String articleId;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date createTime;
}
