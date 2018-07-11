package com.ronglian.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.job.JobManager;
import com.ronglian.model.Demo;
import com.ronglian.model.JobInfo;
import com.ronglian.service.DemoService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年4月27日 上午9:49:07
* @description:demo
*/

@RestController
@Slf4j
@RequestMapping("/demo")
public class DemoController{
	
	@Autowired  
	private DemoService demoService;
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
    private RedisTemplate<Object,Object> redisTemplate;
	
	
	@Autowired
    private JobManager jobManager;
	
	/**
	 * 测试服务正常启动
	 * */
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
    public JsonResult hello() {
		log.debug("*****************************current time:{}*************************",+System.currentTimeMillis());
        return new JsonResult(ResultCode.SUCCESS,"hello world",null);
    }
	
	/**
	 * 测试session共享,需要把项目打成jar包，分别部署到多台机器上（>2台，并用nginx负载），
	 * 请求nginx服务器地址，观察后台打印session id是否一致
	 * */
    @RequestMapping(value = "/session/id",method = RequestMethod.GET)
    public JsonResult getSession(HttpServletRequest request){
        log.debug("session id:" + request.getSession().getId());
        return new JsonResult(ResultCode.SUCCESS,"success",request.getSession().getId());
    }
	
	/**
	 * 测试redis存储String : String,
	 * 先存储到redis，在从redis中取出，封装成返回值
	 * 参数：
	 * 	key:键
	 * 	value:值
	 * 
	 * */
	@RequestMapping(value = "/redis/string/{key}/{value}",method = RequestMethod.GET)
    public JsonResult redisDemo(@PathVariable("key") String key,@PathVariable("value") String value) {
		log.debug("key:"+key+",value:"+value);
		stringRedisTemplate.opsForValue().set(key,value);
		Map<String,String> result = new HashMap<String,String>();
		result.put("get_from_redis_by_"+key,stringRedisTemplate.opsForValue().get(key));
        return new JsonResult(ResultCode.SUCCESS,"success",result);
    }
	

	/**
	 * 测试redis存储对象类型
	 * 参数：
	 * 	demo实体
	 * */
	@RequestMapping(value = "/redis/entity",method = RequestMethod.POST)
    public JsonResult redisDemo(@RequestBody Demo demo) {
		redisTemplate.opsForValue().set("demo",demo);
		Map<String,Demo> result = new HashMap<String,Demo>();
		result.put("get_from_redis_by_demo",(Demo) redisTemplate.opsForValue().get("demo"));
        return new JsonResult(ResultCode.SUCCESS,"success",result);
    }
	
	/**
	 * 测试redi缓存读取，
	 * 首先尝试从缓存中读取，读取失败后，会把从数据库读取到的数据存储到缓存，下次直接从缓存中读取数据，
	 * 详细代码参照service层
	 * 参数：
	 * 	key:键
	 * */
	@RequestMapping(value = "/redis/cache",method = RequestMethod.GET)
    public JsonResult redisCache(@RequestParam("key") String key) {
		log.debug("/redis/cache----key:"+key);
		Demo demo = demoService.findDemoFromRedis(key);
        return new JsonResult(ResultCode.SUCCESS,"success",demo);
    }
    

    /**
	 * 测试新增定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/add",method = RequestMethod.POST)
    public JsonResult addJob(@RequestBody JobInfo jobInfo) throws Exception{
    	log.debug("/job/add----jobName:"+jobInfo.getJobName()+",jobGroup:"+jobInfo.getJobGroup());
    	jobManager.add(jobInfo.getJobName(),jobInfo.getJobGroup(),jobInfo.getCronExpression(),jobInfo.getJobDescription());
    	return new JsonResult(ResultCode.SUCCESS,"add success",jobInfo.toString());
    }
    
    /**
	 * 测试查询定时任务列表
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/list/{page}/{size}",method = RequestMethod.GET)
    public JsonResult listJob(@PathVariable("page") int page,@PathVariable("size") int size) throws Exception{
    	log.info("/job/list----page:"+page+",size:"+size);
    	log.info("【进入】Controller_listJob： "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
    	return new JsonResult(ResultCode.SUCCESS,"add success",jobManager.list(page,size));
    }
    
    /**
	 * 测试修改定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/update",method = RequestMethod.POST)
    public JsonResult updateJob(@RequestBody JobInfo jobInfo) throws Exception{
    	log.debug("/job/update----jobName:"+jobInfo.getJobName()+",jobGroup:"+jobInfo.getJobGroup());
    	jobManager.update(jobInfo.getJobName(),jobInfo.getJobGroup(),jobInfo.getCronExpression(),jobInfo.getJobDescription());
    	return new JsonResult(ResultCode.SUCCESS,"update success",jobInfo.toString());
    }
    
    /**
	 * 测试删除定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/delete/{jobName}/{jobGroup}",method = RequestMethod.GET)
    public JsonResult deleteJob(@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup) throws Exception{
    	log.debug("/job/delete----jobName:"+jobName+",jobGroup:"+jobGroup);
    	jobManager.delete(jobName,jobGroup);
    	return new JsonResult(ResultCode.SUCCESS,"delete success");
    }
    
    /**
	 * 测试暂停定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/pause/{jobName}/{jobGroup}",method = RequestMethod.GET)
    public JsonResult pauseJob(@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup) throws Exception{
    	log.debug("/job/pause----jobName:"+jobName+",jobGroup:"+jobGroup);
    	jobManager.pause(jobName,jobGroup);
    	return new JsonResult(ResultCode.SUCCESS,"pause success");
    }
    
    /**
	 * 测试重启定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/resume/{jobName}/{jobGroup}",method = RequestMethod.GET)
    public JsonResult resumeJob(@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup) throws Exception{
    	log.debug("/job/resume----jobName:"+jobName+",jobGroup:"+jobGroup);
    	jobManager.resume(jobName,jobGroup);
    	return new JsonResult(ResultCode.SUCCESS,"resume success");
    }
    
    /**
	 * 测试立即执行定时任务
     * @throws Exception 
	 * */
    @RequestMapping(value = "/job/trigger/{jobName}/{jobGroup}",method = RequestMethod.GET)
    public JsonResult triggerJob(@PathVariable("jobName") String jobName,@PathVariable("jobGroup") String jobGroup) throws Exception{
    	log.debug("/job/trigger----jobName:"+jobName+",jobGroup:"+jobGroup);
    	jobManager.trigger(jobName,jobGroup);
    	return new JsonResult(ResultCode.SUCCESS,"trigger success");
    }
}
