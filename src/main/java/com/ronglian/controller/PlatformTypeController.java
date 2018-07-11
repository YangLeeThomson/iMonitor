package com.ronglian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.PlatformType;
import com.ronglian.service.PlatformTypeService;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 上午10:29:09
* @description:描述
*/
@RestController
@Slf4j
@RequestMapping("/platform/type")
public class PlatformTypeController {

	@Autowired
	private PlatformTypeService platformTypeService;
	
	/**
	 * 新增平台类型
	 * */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public JsonResult addPlatformType(@RequestBody PlatformType platformType) {
		log.debug("platform type:"+platformType.toString());
		return new JsonResult(ResultCode.SUCCESS,"success",platformTypeService.add(platformType));
	}
	
	/**
	 * 新增平台类型
	 * */
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult find(@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize) {
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",platformTypeService.find(pageNo, pageSize));
	}
	
	/**
	 * 根据平台id查询平台类型，id唯一
	 * */
	@RequestMapping(value = "/find/tenant/id",method = RequestMethod.GET)
	public JsonResult findByTenantId(@RequestParam("tenantId") String tenantId,@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize) {
		log.debug("tenantId:"+tenantId+",pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",platformTypeService.findByTenantId(tenantId, pageNo, pageSize));
	}
	
	/**
	 * 根据平台id查询平台类型，id唯一
	 * */
	@RequestMapping(value = "/find/name",method = RequestMethod.GET)
	public JsonResult findByName(@RequestParam("tenantId") String tenantId,@RequestParam("name") String name) {
		log.debug("tenantId:"+tenantId+",tenantId:"+tenantId+",name:"+name);
		return new JsonResult(ResultCode.SUCCESS,"success",platformTypeService.findByName(tenantId, name));
	}
}
