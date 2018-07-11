package com.ronglian.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.Constants;
import com.ronglian.common.JsonResult;
import com.ronglian.common.PageResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformHistory;
import com.ronglian.model.PlatformType;
import com.ronglian.model.User;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.PlatformService;
import com.ronglian.service.PlatformTypeService;
import com.ronglian.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo,sunqian
* @date:2018年5月16日 上午10:51:46
* @description:描述
*/
@RestController
@Slf4j
@RequestMapping("/platform")
public class PlatformController {
	
	@Autowired
	private PlatformService platformService;
	
	@Autowired
	private PlatformTypeService platformTypeService;
	
	@Autowired
	private	CustomGroupService customGroupService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 根据平台名称查询平台，名称唯一
	 * */
	@RequestMapping(value = "/find/name",method = RequestMethod.GET)
	public JsonResult findByName(@RequestParam("name") String name,@RequestParam("type") String type,@RequestParam("tenantId") String tenantId) {
		log.debug("name:"+name);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findByName(name,type,tenantId));
	}
	
	/**
	 * 根据平台id查询平台，id唯一
	 * */
	@RequestMapping(value = "/find/id",method = RequestMethod.GET)
	public JsonResult findById(@RequestParam("id") String id) {
		log.debug("name:"+id);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findById(id));
	}
	
	/**
	 * 根据平台id查询平台，id唯一
	 * */
	@RequestMapping(value = "/find/tenant/id",method = RequestMethod.GET)
	public JsonResult findByTenantId(@RequestParam("tenantId") String tenantId,@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize) {
		log.debug("tenantId:"+tenantId+",pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findByTenantId(tenantId, pageNo, pageSize));
	}
	
	/**
	 * 分页查询列表
	 * */
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult find(@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize) {
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.find(pageNo, pageSize));
	}
	
	
	/**
	 * 根据平台id查询平台，id唯一
	 * */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public JsonResult addPlatform(@RequestBody Platform platform) {
		log.debug("platform:"+platform.toString());
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.add(platform));
	}
	
	/**
	 * 更新平台信息
	 * */
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public JsonResult updatePlatform(@RequestBody Platform platform) {
		log.debug("platform:"+platform.toString());
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.update(platform));
	}
	
	/**
	 * 更新平台信息
	 * @param
	 * 	id:平台id
	 * */
	@RequestMapping(value = "/delete/id",method = RequestMethod.GET)
	public JsonResult deletePlatform(@RequestParam("id") String id ) {
		log.debug("id:"+id);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.delete(id));
	}
	
	/**
	 * 新增平台浏览记录
	 * */
	@RequestMapping(value = "/history/add",method = RequestMethod.GET)
	public JsonResult addPlatformHistory(@RequestParam(value="platformId", required = true) String platformId,
			@RequestParam(value="platformTypeId", required = true) String platformTypeId,
			@RequestParam(value="platformName", required = true) String platformName,
			@RequestParam(value="platformTypeName", required = true) String platformTypeName,
			HttpServletRequest request) {
//		log.debug("platform:"+platformHistory.toString());
		String username=(String) request.getSession().getAttribute("username");
		PlatformHistory platformHistory = new PlatformHistory();
		platformHistory.setPlatformId(platformId);
		platformHistory.setPlatformName(platformName);
		platformHistory.setPlatformTypeId(platformTypeId);
		platformHistory.setPlatformTypeName(platformTypeName);
		platformHistory.setUserId(username);
		platformService.addPlatformHistory(platformHistory);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findPlatformHistoryList(platformTypeId, 1,5));
	}
//	/**
//	 * 更新平台信息
//	 * */
//	@RequestMapping(value = "/update/history",method = RequestMethod.POST)
//	public JsonResult updatePlatformHistory(@RequestBody PlatformHistory platformHistory) {
//		log.debug("platform:"+platformHistory.toString());
//		return new JsonResult(ResultCode.SUCCESS,"success",platformService.updatePlatformHistory(platformHistory));
//	}
	
//	/**
//	 * 更新平台信息
//	 * @param
//	 * 	id:平台id
//	 * */
//	@RequestMapping(value = "/count/history",method = RequestMethod.GET)
//	public JsonResult countPlatformHistory(@RequestParam("platformTypeId") String platformTypeId ) {
//		log.debug("platformTypeId:"+platformTypeId);
//		return new JsonResult(ResultCode.SUCCESS,"success",platformService.countPlatformHistory(platformTypeId));
//	}
	
//	/**
//	 * 更新平台信息
//	 * @param
//	 * 	id:平台id
//	 * */
//	@RequestMapping(value = "/find/one/history",method = RequestMethod.GET)
//	public JsonResult findOnePlatformHistory(@RequestParam("platformId") String platformId,@RequestParam("platformTypeId") String platformTypeId ) {
//		log.debug("platformTypeId:"+platformTypeId);
//		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findOnePlatformHistory(platformId, platformTypeId));
//	}
	
	/**
	 * 平台历史纪录列表
	 * @param
	 * 	platformTypeId:平台类型id
	 *  pageNo:页码
	 *  pageSize:分页大小
	 * */
	@RequestMapping(value = "/history/list",method = RequestMethod.GET)
	public JsonResult findPlatformHistoryList(@RequestParam(value = "platformTypeId", required = true, defaultValue = "") String platformTypeId,
			@RequestParam(value = "pageNo",required = false,defaultValue="1") int pageNo,
			@RequestParam(value = "pageSize",required = false,defaultValue="5") int pageSize) {
		log.debug("platformTypeId:"+platformTypeId);
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.findPlatformHistoryList(platformTypeId, pageNo, pageSize));
	}
	
	/**
	 * 查询所有平台和自定义监测项
	 * sunqian
	 * */
	@RequestMapping(value = "/findAll",method = RequestMethod.GET)
	public JsonResult findAll(HttpServletRequest request) {
		log.info("查询所有平台，自定义监测项");
		log.info("【进入】Controller_findAll："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		JsonResult result=new JsonResult();
		
		String username=(String) request.getSession().getAttribute("username");
		
		//构造下平台类型的名字和类型id的map，比如  "网站"："UUID32"
		List<PlatformType> allPlatformType= platformTypeService.find(1,Constants.DEFAULT_PAGESIZE * 10).getContent();
		Map<String,String> typeMap=new HashMap<String,String>();
		for(PlatformType type:allPlatformType) {
			typeMap.put(type.getName(), type.getId());
		}
		
		//判断用户是否存在
		if (StringUtils.isNotBlank(username)) {
			User loginUser = userService.findUserByName(username);
			if (loginUser == null) {
				result.setCode(ResultCode.NOT_LOGIN);
				result.setMessage("未找到当前用户：" + username);
				return result;
			} 
		}
		Map<String,Object> data=new HashMap<String,Object>();
		
		//查到所有平台，按类型构造数组
		PageResult<Platform> allPlatform=  platformService.find(1, Constants.DEFAULT_PAGESIZE*10);
		List<Platform> platformList=allPlatform.getContent();
		
		Map<String,Object> website=new HashMap<String,Object>();
		website.put("name", "网站");
		website.put("platfromTypeId", typeMap.get("网站"));
		List<Platform> websiteList=new ArrayList<Platform>();
		
		Map<String,Object> app=new HashMap<String,Object>();
		app.put("name", "APP");
		app.put("platfromTypeId", typeMap.get("APP"));
		List<Platform> appList=new ArrayList<Platform>();
		
		Map<String,Object> weixin=new HashMap<String,Object>();
		weixin.put("name", "微信");
		weixin.put("platfromTypeId", typeMap.get("微信"));
		List<Platform> weixinList=new ArrayList<Platform>();
		
		Map<String,Object> weibo=new HashMap<String,Object>();
		weibo.put("name", "微博");
		weibo.put("platfromTypeId", typeMap.get("微博"));
		List<Platform> weiboList=new ArrayList<Platform>();
		
		for(Platform platform:platformList) {
			if(platform.getPlatformTypeName().equals("网站")) {
				websiteList.add(platform);
			}
			if(platform.getPlatformTypeName().equals("APP")) {
				appList.add(platform);
			}
			if(platform.getPlatformTypeName().equals("微信")) {
				weixinList.add(platform);
			}
			if(platform.getPlatformTypeName().equals("微博")) {
				weiboList.add(platform);
			}
		}
		
		website.put("list", websiteList);
		app.put("list", appList);
		weixin.put("list", weixinList);
		weibo.put("list", weiboList);
		
		//微信的history
		List<PlatformHistory> weixinHistory=platformService.findPlatformHistoryListByUserid(typeMap.get("微信"),username ,1, 5);
		if(weixinHistory.size()<5) {
			// TODO 用户没有历史记录时的处理
		}
		List<PlatformHistory> weiboHistory=platformService.findPlatformHistoryListByUserid(typeMap.get("微博"),username ,1, 5);
		if(weiboHistory.size()<5) {
			// TODO 用户没有历史记录时的处理
		}
		weixin.put("history", weixinHistory);
		weibo.put("history", weiboHistory);
		
		List<Map<String,Object>> nomalList=new ArrayList<Map<String,Object>>();
		nomalList.add(website);
		nomalList.add(app);
		nomalList.add(weixin);
		nomalList.add(weibo);
		data.put("nomal", nomalList);
		
		//构造自定义监测组
		List<CustomMonitorGroup> groupList=customGroupService.findGroupByUser(username);
		for(CustomMonitorGroup group:groupList) {
			String[] platformIds=group.getPlatformIdList().split(",");
			List<Platform> platformsInGroup=new ArrayList<Platform>();
			for(String platformId:platformIds) {
				Platform platform=platformService.findById(platformId);
				platformsInGroup.add(platform);
			}
			group.setPlatformList(platformsInGroup);
		} 
		data.put("special", groupList);
		
		result.setData(data);
		log.info("【结束】Controller_findAll："+request.getRequestURI()+" "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		return result;
	}
}
