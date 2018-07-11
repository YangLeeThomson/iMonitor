package com.ronglian.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ronglian.common.Constants;
import com.ronglian.common.PageResult;
import com.ronglian.common.exception.BaseException;
import com.ronglian.common.exception.ExceptionDescription;
import com.ronglian.common.exception.ExceptionType;
import com.ronglian.mapper.PlatformTypeMapper;
import com.ronglian.model.PlatformType;
import com.ronglian.service.PlatformTypeService;
import com.ronglian.utils.UUIDUtil;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月18日 上午10:25:57
* @description:平台类型service
*/
@Slf4j
@Service("platformTypeService")
public class PlatformTypeServiceImpl implements PlatformTypeService{
	
	@Autowired
	private PlatformTypeMapper platformTypeMapper;

	@Transactional
	@Override
	public int add(PlatformType platformType) {
		// TODO Auto-generated method stub
		log.debug("platform:"+platformType.toString());
		if(StringUtils.isEmpty(platformType.getName())) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.PARAMETER_LACK,"name");
		}
		if(StringUtils.isEmpty(platformType.getTenantId())) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.PARAMETER_LACK,"tenant id");
		}
		PlatformType oldPlatformType = platformTypeMapper.findByName(platformType.getName(),platformType.getTenantId());
		if(oldPlatformType != null) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.OBJECT_ALREADY_EXISTS);
		}
		platformType.setId(UUIDUtil.getUUID32());
		return platformTypeMapper.add(platformType);
	}

	@Override
	public PageResult<PlatformType> find(Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if((pageNo == 0) || (pageNo == null)) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.PARAMETER_LACK,"pageNo");
		}
		pageSize = ((pageSize == 0) || (pageSize == null)) ? Constants.DEFAULT_PAGESIZE:pageSize;
		PageResult<PlatformType> pageResult = new PageResult<PlatformType>();
		int totalCount = platformTypeMapper.allCount();
		if (totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<PlatformType> list = platformTypeMapper.findPageList(pageNo, pageSize);
			pageResult.setContent(list);
		}else {
			pageResult.setTotalElements(0);
			pageResult.setTotalPages(0);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			pageResult.setContent(null);
		}
		return pageResult;
	}

	@Override
	public PageResult<PlatformType> findByTenantId(String tenantId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if(StringUtils.isEmpty(tenantId)) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.PARAMETER_LACK,"tenant id");
		}
		if((pageNo == 0) || (pageNo == null)) {
			throw new BaseException(ExceptionType.PLATFORM_TYPE,ExceptionDescription.PARAMETER_LACK,"pageNo");
		}
		pageSize = ((pageSize == 0) || (pageSize == null)) ? Constants.DEFAULT_PAGESIZE:pageSize;
		PageResult<PlatformType> pageResult = new PageResult<PlatformType>();
		int totalCount = platformTypeMapper.tenantCount(tenantId);
		if(totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<PlatformType> list = platformTypeMapper.findByTenantIdPageList(tenantId, pageNo, pageSize);
			pageResult.setContent(list);
		}else {
			pageResult.setTotalElements(0);
			pageResult.setTotalPages(0);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			pageResult.setContent(null);
		}
		return pageResult;
	}

	@Override
	public PlatformType findByName(String tenantId, String name) {
		// TODO Auto-generated method stub
		return platformTypeMapper.findByName(name, tenantId);
	}

}
