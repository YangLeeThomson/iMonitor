package com.ronglian.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ronglian.service.ArticlesHandlerService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月23日 下午3:00:44
* @description:描述
*/
@Slf4j
@Component
@DisallowConcurrentExecution
public class GetTransArticleJob implements Job {
	
	@Autowired
	private ArticlesHandlerService articlesHandlerService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		log.info("-------------推送原始文章至相似相关任务开始执行---------------------------------");
		articlesHandlerService.pushToSimilarityService();
	}

}
