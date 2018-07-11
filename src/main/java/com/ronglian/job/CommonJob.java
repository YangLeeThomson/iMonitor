/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.job 
 * @author: YeohLee   
 * @date: 2018年6月28日 下午3:19:17 
 */
package com.ronglian.job;

import lombok.extern.slf4j.Slf4j;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ronglian.service.CommonService;

 /** 
 * @ClassName: CommonJob 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年6月28日 下午3:19:17  
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class CommonJob implements Job{

	@Autowired
	private CommonService commonService;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		log.info("-----------------添加redis缓存任务启动-----------------------");
		commonService.putPlatformIntoRedis();
		commonService.putPlatformMappingType();
		commonService.putPlatformTypeIntoRedis();
	}

}
