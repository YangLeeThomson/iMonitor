/**   
 * Copyright © 2018 北京荣之联科技股份有限公司 All rights reserved.
 * 
 * @Package: com.ronglian.service.impl 
 * @author: YeohLee   
 * @date: 2018年5月28日 上午12:16:58 
 */
package com.ronglian.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.ronglian.mapper.CommonMapper;
import com.ronglian.mapper.PlatformMapper;
import com.ronglian.model.Platform;
import com.ronglian.service.CommonService;

 /** 
 * @ClassName: CommonServiceImpl 
 * @Description: TODO
 * @author: YeohLee
 * @date: 2018年5月28日 上午12:16:58  
 */
@Service(value="commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
    private RedisTemplate<Object,Object> redisTemplate;
	
	@Autowired
    private CommonMapper commonMapper;
	
	@Autowired
    private PlatformMapper platformMapper;
	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#putPlatformIntoRedis()
	 */
	@Override
	public void putPlatformIntoRedis() {
		// TODO Auto-generated method stub
		List<Platform> list = this.commonMapper.selectPlatform();
		this.redisTemplate.opsForValue().set("platformList", list);
		for(Platform plat:list){
			String id = plat.getId();
			String name = plat.getName();
			if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(name)){
				this.redisTemplate.opsForValue().set(id, name);
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#putPlatformTypeIntoRedis()
	 */
	@Override
	public void putPlatformTypeIntoRedis() {
		// TODO Auto-generated method stub
		List<Platform> list = this.commonMapper.selectPlatformType();
		for(Platform plat:list){
			String platformTypeId = plat.getPlatformTypeId();
			String platformTypeName = plat.getPlatformTypeName();
			if(StringUtils.isNotBlank(platformTypeName) && StringUtils.isNotBlank(platformTypeId)){
				this.redisTemplate.opsForValue().set(platformTypeId, platformTypeName);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#putPlatformMappingType()
	 */
	@Override
	public void putPlatformMappingType() {
		// TODO Auto-generated method stub
		List<Platform> list = this.commonMapper.selectPlatformMappingType();
		for(Platform plat:list){
			String platformTypeId = plat.getPlatformTypeId();
			String id = plat.getId();
			if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(platformTypeId)){
				this.redisTemplate.opsForValue().set(id+"mappingtype", platformTypeId);
			}
		}
	}
public static void main(String[] args) {
	String str = "\"看苏州\"";
	String str1 = str.replace("\"", "");
	System.out.println(str1);
	System.out.println(str);
}
	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#getPlatformNameById(java.lang.String)
	 */
	@Override
	public String getPlatformNameById(String platformId) {
		// TODO Auto-generated method stub
		String platformName = this.stringRedisTemplate.opsForValue().get(platformId);
		if(StringUtils.isBlank(platformName)){
			Platform entity = this.platformMapper.findById(platformId);
			if(entity != null){
				platformName = entity.getName();
			}
		}
		if(StringUtils.isBlank(platformName)){
			platformName = "未知平台";
		}
		platformName = platformName.replace("\"", "");
		return platformName;
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#getPlatformTypeNameById(java.lang.String)
	 */
	@Override
	public String getPlatformTypeNameById(String platformTypeId) {
		// TODO Auto-generated method stub
		platformTypeId = platformTypeId.replace("\"", "");
//		System.out.println("==================================="+platformTypeId+"===================================");
		String platformTypeName = this.stringRedisTemplate.opsForValue().get(platformTypeId);
		if(StringUtils.isBlank(platformTypeName)){
			Platform entity = this.commonMapper.selectPlatformTypeByTypeId(platformTypeId);
			if(entity != null){
				platformTypeName = entity.getPlatformTypeName();
			}
		}
		if(StringUtils.isBlank(platformTypeName)){
			platformTypeName = "未知平台组";
		}
		platformTypeName = platformTypeName.replace("\"", "");
//		System.out.println("==================================="+platformTypeName+"===================================");
		return platformTypeName;
	}

	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#getPlatformTypeById(java.lang.String)
	 */
	@Override
	public String getPlatformTypeByPlatformId(String platformId) {
		// TODO Auto-generated method stub
		String platformTypeId = this.stringRedisTemplate.opsForValue().get(platformId+"mappingtype");
		if(StringUtils.isBlank(platformTypeId)){
			Platform entity = this.commonMapper.selectPlatformMappingTypeById(platformId);
			if(entity != null){
				platformTypeId = entity.getPlatformTypeId();
			}
		}
		return platformTypeId;
	}
	/* (non-Javadoc)
	 * @see com.ronglian.service.CommonService#getAllPlatform()
	 */
	@Override
	public List<Platform> getAllPlatform() {
		// TODO Auto-generated method stub
		 List<Platform> list = null;
		 String resultStr = this.stringRedisTemplate.opsForValue().get("platformList");
		 if(StringUtils.isBlank(resultStr)){
			 list = this.commonMapper.selectPlatform();
		 }
		 if(StringUtils.isNotBlank(resultStr)){
			 list = (List<Platform>) JSONUtils.parse(resultStr);
		 }
		return list;
	}

}
