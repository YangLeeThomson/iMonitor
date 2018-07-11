package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @Description: 转载时段排行实体
 * @author sunqian 
 * @date 2018年5月28日 下午5:43:31
 */
@Data
public class PlatformTransPeriod {
	
	@JsonIgnore
	private String platform_id;
	@JsonIgnore
	private String platform_type_id;
	private int period_type;
	@JsonIgnore
	private Date publish_time;
	@JsonIgnore
	private Date report_time;
	private int trans_num;
	
	//前端用
	private String period_name;


}
