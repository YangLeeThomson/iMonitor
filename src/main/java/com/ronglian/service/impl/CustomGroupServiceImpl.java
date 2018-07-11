package com.ronglian.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.mapper.CustomerGroupMapper;
import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.service.CustomGroupService;


/**
* @author: sunqian
* @date:2018年6月14日 上午12:18:55
* @description:自定义检测项
*/
@Service(value = "customGroupService")
public class CustomGroupServiceImpl implements CustomGroupService {
	
	@Autowired
	CustomerGroupMapper customerGroupMapper;

	@Override
	public int addGroup(CustomMonitorGroup group) {
		return customerGroupMapper.add(group);

	}

	@Override
	public List<CustomMonitorGroup> findGroupByUser(String username) {
		return customerGroupMapper.listByUserId(username);
	}

	@Override
	public CustomMonitorGroup findGroupByGroupName(String groupName) {
		return customerGroupMapper.findByGroupName(groupName);
	}
	
	@Override
	public CustomMonitorGroup findGroupByGroupId(String groupId) {
		return customerGroupMapper.findByGroupId(groupId);
	}

	@Override
	public int deleteCustomGroup(CustomMonitorGroup group) {
		String id=group.getId();
		return customerGroupMapper.delete(id);
	}

}
