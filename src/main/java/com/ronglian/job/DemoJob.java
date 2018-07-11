package com.ronglian.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月11日 下午6:13:59
* @description:描述
*/
@Slf4j
@Component
@DisallowConcurrentExecution
public class DemoJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		log.info("***************test job----jobGroup={}----------cronExpression={}" ,context.getJobDetail().getJobDataMap().getString("jobGroup"),context.getJobDetail().getJobDataMap().getString("cronExpression"));
	}

}
