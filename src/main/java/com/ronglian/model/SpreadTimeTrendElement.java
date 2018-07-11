package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 
 * @Description: 传播时间趋势 节点实体
 * @author sunqian 
 * @date 2018年5月25日 下午2:09:39
 */

@Data
public class SpreadTimeTrendElement {
	@JsonIgnore
	private String platform_id;  //平台id
	@JsonIgnore
	private String platform_type_id;  //平台组id
	@JsonIgnore
	private String json_nums;
	private float comprehensive_num; //综合数值
	private int read_num; //阅读数
	private int trans_num; //转发数
	private int comment_num;//评论数
	private String create_time;//时间

}
