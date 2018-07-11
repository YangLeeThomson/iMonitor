package com.ronglian.repository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronglian.common.PageResult;
import com.ronglian.model.FocalMediaTrans;
import com.ronglian.model.FocalTransCount;

public interface FocalMediaRepository{
	public PageResult<FocalTransCount> findFocalTransCountPage(List<String> queryIds,Integer queryType,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException;
	public PageResult<FocalMediaTrans> findFocalMediaTransPage(List<String> queryIds,Integer queryType,Integer publishStatus,String mediaId,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException;
	
	public void saveFocalMediaTrans(List<FocalMediaTrans> focalMediaTransList) throws JsonProcessingException;
	
	public void updateFocalMediaTrans(List<FocalMediaTrans> focalMediaTransList) throws JsonProcessingException, IOException;
	
	public Map<String,FocalMediaTrans> findByIds(List<String> ids) throws IOException;
	
	public void overByMediaId(String mediaId) throws IOException ;
}
