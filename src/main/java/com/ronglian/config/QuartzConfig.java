package com.ronglian.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author: 黄硕/huangshuo
 * @date:2018年5月10日 下午2:07:36
 * @description:定时任务配置
 */
@Configuration
public class QuartzConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
	private String schedulerInstanceName;
	
	@Value("${spring.quartz.properties.org.quartz.scheduler.instanceId}")
	private String schedulerInstanceId;
	
	@Value("${spring.quartz.properties.org.quartz.jobStore.misfireThreshold}")
	private String jobStoreMisfireThreshold;

	@Value("${spring.quartz.properties.org.quartz.jobStore.class}")
	private String jobStoreClass;

	@Value("${spring.quartz.properties.org.quartz.jobStore.driverDelegateClass}")
	private String jobStoreDriverDelegateClass;
	
	@Value("${spring.quartz.properties.org.quartz.jobStore.dataSource}")
	private String jobStoreDataSource;
	
	@Value("${spring.quartz.properties.org.quartz.jobStore.tablePrefix}")
	private String jobStoreTablePrefix;

	@Value("${spring.quartz.properties.org.quartz.jobStore.isClustered}")
	private String jobStoreIsClustered;
	
	@Value("${spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval}")
	private String jobStoreClusterCheckinInterval;
	
	@Value("${spring.quartz.properties.org.quartz.jobStore.useProperties}")
	private String jobStoreUseProperties;
	
	@Value("${spring.quartz.properties.org.quartz.threadPool.class}")
	private String threadPoolClass;
	
	@Value("${spring.quartz.properties.org.quartz.threadPool.threadCount}")
	private String threadPoolThreadCount;
	
	@Value("${spring.quartz.properties.org.quartz.threadPool.threadPriority}")
	private String threadPoolThreadPriority;
	
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.driver}")
//	private String dataSourceDriver;
//	
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.url}")
//	private String dataSourceUrl;
//	
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.user}")
//	private String dataSourceUser;
//	
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.password}")
//	private String dataSourcePassword;
//	
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.maxConnections}")
//	private String dataSourceMaxConnections;
//
//	@Value("${spring.quartz.properties.org.quartz.dataSource.imonitor.validationQuery}")
//	private String dataSourceValidationQuery;
//	
	

	/**
	 *   * 设置属性   * @return   * @throws IOException   
	 */
	private Properties quartzProperties() throws IOException {
		Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", schedulerInstanceName);
        prop.put("org.quartz.scheduler.instanceId", schedulerInstanceId);
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.jmx.export", "true");

        prop.put("org.quartz.jobStore.class", jobStoreClass);
        prop.put("org.quartz.jobStore.driverDelegateClass", jobStoreDriverDelegateClass);
        prop.put("org.quartz.jobStore.tablePrefix", jobStoreTablePrefix);
        prop.put("org.quartz.jobStore.isClustered", jobStoreIsClustered);

        prop.put("org.quartz.jobStore.clusterCheckinInterval", jobStoreClusterCheckinInterval);
        prop.put("org.quartz.jobStore.dataSource", jobStoreDataSource);
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        prop.put("org.quartz.jobStore.misfireThreshold", jobStoreMisfireThreshold);
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE");

        prop.put("org.quartz.threadPool.class", threadPoolClass);
        prop.put("org.quartz.threadPool.threadCount", threadPoolThreadCount);
        prop.put("org.quartz.threadPool.threadPriority", threadPoolThreadPriority);
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");

//        prop.put("org.quartz.dataSource.imonitor.driver", dataSourceDriver);
//        prop.put("org.quartz.dataSource.imonitor.URL", dataSourceUrl);
//        prop.put("org.quartz.dataSource.imonitor.user", dataSourceUser);
//        prop.put("org.quartz.dataSource.imonitor.password", dataSourcePassword);
//        prop.put("org.quartz.dataSource.imonitor.maxConnections", dataSourceMaxConnections);

        prop.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        prop.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");
        return prop;
	}
	
	@Bean
    public SchedulerFactoryBean schedulerFactoryBean (QuartzJobFactory quartzJobFactory) throws Exception {
        SchedulerFactoryBean factoryBean=new SchedulerFactoryBean();
        factoryBean.setJobFactory(quartzJobFactory);
//        factoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        factoryBean.setQuartzProperties(quartzProperties());
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
        Scheduler scheduler=schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }

//	@Bean(name = "schedulerFactoryBean")
//	public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dialogJobTrigger") Trigger cronJobTrigger) throws IOException { 
//		SchedulerFactoryBean factory = new SchedulerFactoryBean(); 
//		// this allows to update triggers in DB when updating settings in config file: 
//		//用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了 
//		factory.setOverwriteExistingJobs(true); 
//		//用于quartz集群,加载quartz数据源 
//		factory.setDataSource(dataSource); 
//		//QuartzScheduler 延时启动，应用启动完10秒后 QuartzScheduler 再启动 
//		factory.setStartupDelay(10);
//		//用于quartz集群,加载quartz数据源配置 
//		factory.setQuartzProperties(quartzProperties());
//		factory.setAutoStartup(true);
//		factory.setApplicationContextSchedulerContextKey("applicationContext");
//		//注册触发器 
//		factory.setTriggers(cronJobTrigger);//直接使用配置文件
//		//  factory.setConfigLocation(new FileSystemResource(this.getClass().getResource("/quartz.properties").getPath()));
//		return factory; 
//	}
//	
//	/**
//	 * 创建触发器工厂   
//	 * @param jobDetail   
//	 * @param cronExpression   
//	 * @return   
//	 */
//	private static CronTriggerFactoryBean dialogStatusTrigger(JobDetail jobDetail, String cronExpression) {
//		CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
//		factoryBean.setJobDetail(jobDetail);
//		factoryBean.setCronExpression(cronExpression);
//		return factoryBean;
//	}
//	
//	/**
//	 * 加载job   
//	 * @return   
//	 */
//	@Bean(name = "updateDialogStatusJobDetail")
//	public JobDetailFactoryBean updateDialogStatusJobDetail() {
//		return createJobDetail(InvokingJobDetailDetailFactory.class, "updateDialogStatusGroup", "dialogJob");
//	}
//
//	/**
//	 * 加载触发器   
//	 * @param jobDetail   
//	 * @return   
//	 */
//	@Bean(name = "dialogJobTrigger")
//	public CronTriggerFactoryBean dialogStatusJobTrigger(@Qualifier("updateDialogStatusJobDetail") JobDetail jobDetail) {
//		return dialogStatusTrigger(jobDetail, "0 0 0/1 * * ?");
//	}
//
//	/**
//	 *   * 创建job工厂   * @param jobClass   * @param groupName   * @param targetObject
//	 *   * @return   
//	 */
//	private static JobDetailFactoryBean createJobDetail(Class<?> jobClass, String groupName, String targetObject) {
//		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
//		factoryBean.setJobClass(jobClass);
//		factoryBean.setDurability(true);
//		factoryBean.setRequestsRecovery(true);
//		factoryBean.setGroup(groupName);
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("targetObject", targetObject);
//		map.put("targetMethod", "execute");
//		factoryBean.setJobDataAsMap(map);
//		return factoryBean;
//	}

	
}
