package com.ronglian.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ronglian.common.PageResult;
import com.ronglian.model.FocalTransCount;

public interface FocalMediaService{
	public PageResult<FocalTransCount>  findFocalTransCountPage(String queryId,String groupId,String mainGroup,Integer queryType,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException;
	public Map<String,Object> findFocalMediaTransPage(String queryId,String groupId,String mainGroup,String mediaId,Integer publishStatus,Integer queryType,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException;
	
	public void saveFocalMediaTrans(List<Map<String,Object>> requestMapList) throws ParseException, IOException;
	
}