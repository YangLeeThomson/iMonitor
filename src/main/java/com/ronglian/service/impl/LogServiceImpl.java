package com.ronglian.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.model.Log;
import com.ronglian.service.LogService;
import com.ronglian.mapper.LogMapper;

@Slf4j
@Service("logService")
public class LogServiceImpl implements LogService{
	
	@Autowired
	private LogMapper logMapper;
	
	@Override
	public int add(Log log){
		return logMapper.add(log);
	}

	@Override
	public int update(Log log){
		return logMapper.update(log);
	}

}
