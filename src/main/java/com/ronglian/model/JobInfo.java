package com.ronglian.model;

import java.util.Date;

import lombok.Data;
/**
* @author: 黄硕/huangshuo
* @date:2018年5月11日 下午6:19:35
* @description:描述
*/
@Data
public class JobInfo {
	private String jobName;
    private String jobGroup;
    private String jobDescription;
    private String jobStatus;
    private String cronExpression;
    private String createTime;

    private Date previousFireTime;
    private Date nextFireTime;
}
