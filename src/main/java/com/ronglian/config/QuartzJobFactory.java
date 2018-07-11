package com.ronglian.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月11日 下午5:44:08
* @description:Spring Boot AutowireCapableBeanFactory让不受spring管理的类具有spring自动注入的特性
*/
@Component
public class QuartzJobFactory extends AdaptableJobFactory{
	@Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
