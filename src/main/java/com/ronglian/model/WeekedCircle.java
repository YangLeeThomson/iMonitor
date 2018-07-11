/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.model 
 * @author: YeohLee   
 * @date: 2018年5月30日 上午1:25:47 
 */
package com.ronglian.model;

import lombok.Data;

 /** 
 * @ClassName: WeekedCircle 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月30日 上午1:25:47  
 */
@Data
public class WeekedCircle {

	private String title;
//	: '原创文章',
    private String sequential;
//    : '34.67%',
    private Integer sequentialtext;
//    : '57',
    private Integer prevWeek;
//    : '46',
    private Integer week;
//    : '103'
}
