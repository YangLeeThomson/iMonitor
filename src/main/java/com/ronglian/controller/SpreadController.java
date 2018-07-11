package com.ronglian.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.SpreadService;

/**
* @author: yanglee
* @date:2018年5月22日 上午11:50:01 
* @description:描述
*/
@RestController
@Slf4j
@RequestMapping("/spread")
public class SpreadController {
	
	@Autowired
	private SpreadService spreadService;
	
	@Autowired
	private CustomGroupService customGroupService;
	
	/**
	 * 5.1传播情况统计
	 * */
//	@RequestMapping(value = {"/accounted/{groupId}"},method = RequestMethod.GET)
	@RequestMapping(value = {"/accounted"},method = RequestMethod.GET)
	public JsonResult spreadAccounted(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
		log.debug("platformId:"+platformId);
		log.debug("platformtypeId:"+platformTypeId);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformArticleInfo(platformIdList,platformTypeId,startTime,accountType));
	}
	/**
	 * 5.2各平台作品数占比统计
	 * */
//	@RequestMapping(value = "/platform/articlenum/accounted/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/platform/articlenum/accounted",method = RequestMethod.GET)
	public JsonResult getPlatformArticleNumAccounted(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true) Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3") Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformArticleNumAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	/**
	 * 5.3原创文章占比统计
	 * */
//	@RequestMapping(value = "/platform/originalnum/accounted/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/platform/originalnum/accounted",method = RequestMethod.GET)
	public JsonResult getOriginalArticleNumAccounted(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getOriginalArticleNumAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	/**
	 * 5.4周环比统计接口
	 * */
	@RequestMapping(value = "/platform/weeked/circle",method = RequestMethod.GET)
	public JsonResult getWeekedCircleAccounted(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam(value="startTime",required=true)Date startTime) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getWeekedCircleAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	/**
	 * 5.5传播地域分布
	 * 
	 * */
//	@RequestMapping(value = "/area/current/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/area/current",method = RequestMethod.GET)
	public JsonResult getSpreadAreaCurrent(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getSpreadAreaCurrent(platformTypeId,platformIdList,startTime,accountType));
	}
	/**
	 * 5.6某一地域转载数量
	 * 
	 * */
//	@RequestMapping(value = "/provience/transnum/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/provience/transnum",method = RequestMethod.GET)
	public JsonResult getProvienceTransnum(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="province",required=true) String province,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getProvienceTransnum(platformTypeId,platformIdList,province,startTime,accountType));
	}
	/**
	 * 5.7地区转载列表
	 * 
	 * */
//	@RequestMapping(value = "/area/trans/list/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/area/trans/list",method = RequestMethod.GET)
	public JsonResult getSpreadAreaTransList(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="province",required=true) String province,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("platformId:"+platformId);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		if("广西".equals(province)){
			province = "广西壮族自治区";
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getSpreadAreaTransList(platformTypeId,platformIdList,province,startTime,accountType,pageNo,pageSize));
	}
	/**
	 * 5.8平台转载排行
	 * */
//	@RequestMapping(value = "/platform/trans/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/platform/trans",method = RequestMethod.GET)
	public JsonResult getPlatformTrans(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformTrans(platformTypeId,platformIdList,startTime,accountType));
	}
	/**
	 * 5.8.2平台转载排行(非首页)
	 * */
//	@RequestMapping(value = "/platform/trans/{groupId}",method = RequestMethod.GET)
	@RequestMapping(value = "/platform/translist",method = RequestMethod.GET)
	public JsonResult getPlatformTranslist(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
//			@PathVariable("groupId") String groupId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType) {
//		log.debug("accountType:"+accountType);
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformTrans2(platformTypeId,platformIdList,startTime,accountType));
	}
	/**
	 * 
	 * @Description: 传入platformTypeId、platformId、groupId得到平台id列表
	 * @param @param platformTypeId
	 * @param @param platformId
	 * @param @param groupId
	 * @param @return    参数  
	 * @return List<String>    返回类型  
	 * @throws
	 */
	private  List<String> getPlatformList(String platformId, String groupId) {
		
		List<String> platformIdList=null;
		if(StringUtils.isNotBlank(platformId)) {
			//根据platformId查询
			platformIdList=new ArrayList<String>();
			platformIdList.add(platformId);
		}
		if(StringUtils.isNotBlank(groupId)) {
			//根据groupId查询
			CustomMonitorGroup group = null;
			group = this.customGroupService.findGroupByGroupId(groupId);
			if(group != null){
				platformIdList = Arrays.asList(group.getPlatformIdList().split(","));
			}else{
				platformIdList = new ArrayList();
				platformIdList.add(groupId);
			}
		}
		return platformIdList;
	}
}
