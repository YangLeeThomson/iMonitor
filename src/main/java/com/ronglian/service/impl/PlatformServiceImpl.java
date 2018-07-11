package com.ronglian.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ronglian.common.PageResult;
import com.ronglian.common.exception.BaseException;
import com.ronglian.common.exception.ExceptionDescription;
import com.ronglian.common.exception.ExceptionType;
import com.ronglian.mapper.PlatformMapper;
import com.ronglian.mapper.PlatformTypeMapper;
import com.ronglian.model.Platform;
import com.ronglian.model.PlatformHistory;
import com.ronglian.model.PlatformType;
import com.ronglian.service.PlatformService;
import com.ronglian.utils.UUIDUtil;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月15日 下午4:43:26
* @description:描述
*/
@Slf4j
@Service(value = "platformService")
public class PlatformServiceImpl implements PlatformService{
	
	@Autowired
	private PlatformMapper platformMapper;
	
	@Autowired
	private PlatformTypeMapper platformTypeMapper;

	@Transactional
	@Override
	public int add(Platform platform) {
		// TODO Auto-generated method stub
		log.debug("platform:"+platform.toString());
		if(StringUtils.isEmpty(platform.getName())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"name");
		}
		if(StringUtils.isEmpty(platform.getTenantId())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"tenantId");
		}
		Platform oldPlatform = platformMapper.findByName(platform.getName(),platform.getPlatformTypeName(),platform.getTenantId());
		if(oldPlatform != null) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.OBJECT_ALREADY_EXISTS);
		}
		PlatformType platformType = platformTypeMapper.findByName(platform.getPlatformTypeName(),platform.getTenantId());
		if(platformType == null) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.OBJECT_NOT_EXISTS,"platform tye not exists!");
		}
		if(platform.getStatus()==null) {
			platform.setStatus(0);
		}
		if(platform.getWeight()==null) {
			platform.setWeight(5);
		}
		platform.setPlatformTypeId(platformType.getId());
		platform.setPlatformTypeName(platformType.getName());
		platform.setId(UUIDUtil.getUUID32());
		return platformMapper.add(platform);
	}

	@Override
	public Platform findById(String id) {
		// TODO Auto-generated method stub
		log.debug("id:"+id);
		return platformMapper.findById(id);
	}

	@Override
	public Platform findByName(String name,String type,String tenantId) {
		// TODO Auto-generated method stub
		log.debug("name:"+name);
		return platformMapper.findByName(name,type,tenantId);
	}

	@Override
	public PageResult<Platform> findByTenantId(String tenantId, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		log.debug("tenantId:"+tenantId+",pageNo:"+pageNo+",pageSize:"+pageSize);
		PageResult<Platform> pageResult = new PageResult<Platform>();
		int totalCount = platformMapper.tenantCount(tenantId);
		if (totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<Platform> list = platformMapper.findByTenantIdPageList(tenantId, pageNo, pageSize);
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
	public PageResult<Platform> find(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		PageResult<Platform> pageResult = new PageResult<Platform>();
		int totalCount = platformMapper.allCount();
		if (totalCount > 0) {
			pageResult.setTotalElements(totalCount);
			int totalPages = Utils.totalPage(totalCount, pageSize);
			pageResult.setTotalPages(totalPages);
			pageResult.setNumber(pageNo);
			pageResult.setLimit(pageSize);
			List<Platform> list = platformMapper.findPageList(pageNo, pageSize);
			for(Platform platform:list) {
				String platformName = platform.getName();
				String platformTypeName = platform.getPlatformTypeName();
				platform.setName(platformName+"_"+platformTypeName);
			}
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
	public int update(Platform platform) {
		// TODO Auto-generated method stub
		log.debug("platform:"+platform.toString());
		return platformMapper.update(platform);
	}

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		log.debug("id:"+id);
		return platformMapper.delete(id);
	}

	@Override
	public int addPlatformHistory(PlatformHistory platformHistory) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(platformHistory.getPlatformId())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"platformId");
		}
		if(StringUtils.isEmpty(platformHistory.getPlatformTypeId())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"platformTypeId");
		}
		PlatformHistory oldPlatformHistory = platformMapper.findOnePlatformHistory(platformHistory.getPlatformId(),platformHistory.getPlatformTypeId(),platformHistory.getUserId());
		if(oldPlatformHistory != null) {
			return platformMapper.updatePlatformHistory(platformHistory);
		}else {
			return platformMapper.addPlatformHistory(platformHistory);
		}
	}

	@Override
	public int countPlatformHistory(String platformTypeId) {
		// TODO Auto-generated method stub
		return platformMapper.countPlatformHistory(platformTypeId);
	}

	@Override
	public List<PlatformHistory> findPlatformHistoryList(String platformTypeId, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return platformMapper.findPlatformHistoryList(platformTypeId, pageNo, pageSize);
	}

	@Override
	public int updatePlatformHistory(PlatformHistory platformHistory) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(platformHistory.getPlatformId())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"platformId");
		}
		if(StringUtils.isEmpty(platformHistory.getPlatformTypeId())) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.PARAMETER_LACK,"platformTypeId");
		}
		PlatformHistory oldPlatformHistory = platformMapper.findOnePlatformHistory(platformHistory.getPlatformId(),platformHistory.getPlatformTypeId(),platformHistory.getUserId());
		if(oldPlatformHistory == null) {
			throw new BaseException(ExceptionType.PLATFORM,ExceptionDescription.OBJECT_NOT_EXISTS);
		}
		return platformMapper.updatePlatformHistory(platformHistory);
	}

	@Override
	public PlatformHistory findOnePlatformHistory(String platformId, String platformTypeId,String userid) {
		// TODO Auto-generated method stub
		return platformMapper.findOnePlatformHistory(platformId, platformTypeId,userid);
	}

	@Override
	public List<PlatformHistory> findPlatformHistoryListByUserid(String platformTypeId, String username, int pageNo,int pageSize) {
		return platformMapper.findPlatformHistoryListByUserid(platformTypeId,username, pageNo, pageSize);
	}
	
	@Override
	public List<Platform> findPlatformByIds(Set<String> ids){
		//return platformMapper.findPlatformByIds(ids);
		return null;
	}

	@Override
	public List<String> findByPlatformTypeId(String platformTypeId) {
		List<String> platformIds=null;
		List<Platform> platforms=null;
		if(platformTypeId==null)
			platforms=platformMapper.findPageList(1, Integer.MAX_VALUE);
		else {
			platforms=platformMapper.findByPlatformTypeId(platformTypeId);
		}
		if(platforms !=null && platforms.size()>0) {
			platformIds=new ArrayList<String>();
			for(Platform platform:platforms) {
				platformIds.add(platform.getId());
			}
		}
		return platformIds;
	}

}
