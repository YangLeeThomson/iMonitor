package com.ronglian.service;

import java.util.List;

import com.ronglian.model.CustomMonitorGroup;

public interface CustomGroupService {

	int addGroup(CustomMonitorGroup group);

	List<CustomMonitorGroup> findGroupByUser(String username);

	CustomMonitorGroup findGroupByGroupName(String groupName);
	
	CustomMonitorGroup findGroupByGroupId(String groupName);

	int deleteCustomGroup(CustomMonitorGroup group);

}
