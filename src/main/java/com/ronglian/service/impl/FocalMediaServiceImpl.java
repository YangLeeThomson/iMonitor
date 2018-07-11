package com.ronglian.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronglian.common.PageResult;
import com.ronglian.mapper.PlatformMapper;
import com.ronglian.model.FocalMediaTrans;
import com.ronglian.model.FocalTransCount;
import com.ronglian.repository.ElasticRepository;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.FocalMediaService;
import com.ronglian.service.PlatformService;
import com.ronglian.utils.MD5Util;
import com.ronglian.mapper.FocalMediaMapper;
import com.ronglian.repository.FocalMediaRepository;

@Slf4j
@Service("focalMediaService")
public class FocalMediaServiceImpl implements FocalMediaService{
	
	@Autowired
	private ElasticRepository elasticRepository;
	@Autowired
	private FocalMediaRepository focalMediaRepository;
	
	@Autowired
	private PlatformMapper platformMapper;
	@Autowired
	private FocalMediaMapper focalMediaMapper;
	@Autowired
	private CustomGroupService customGroupService;
	@Autowired
	private PlatformService platformService;
	/**
	 * 重点媒体转载统计查询
	 * @throws IOException 
	 * queryType:1按媒体统计，2按平台统计
	 * */
	@Override
	public PageResult<FocalTransCount> findFocalTransCountPage(String queryId,String groupId,String mainGroup,Integer queryType,
			Date startTime, Date endTime,int pageNo,int pageSize) throws IOException{
		List<String> queryIds =new LinkedList<String>();
		if(!StringUtils.isEmpty(groupId)){
			//queryType=3;
			if(customGroupService.findGroupByGroupId(groupId)!=null&&customGroupService.findGroupByGroupId(groupId).getPlatformIdList()!=null){
			queryIds =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));}else{
				PageResult<FocalTransCount> focalTransCountPage=new PageResult<FocalTransCount>();
				focalTransCountPage.setTotalElements(0);
				focalTransCountPage.setTotalPages(0);
				focalTransCountPage.setNumber(pageNo);
				focalTransCountPage.setLimit(pageSize);
				focalTransCountPage.setContent(null);
				return focalTransCountPage;
			}
		}else if(!StringUtils.isEmpty(mainGroup)){
			//queryType=3;
			if(platformService.findByPlatformTypeId(mainGroup)!=null){
			queryIds =platformService.findByPlatformTypeId(mainGroup);}else{
				PageResult<FocalTransCount> focalTransCountPage=new PageResult<FocalTransCount>();
				focalTransCountPage.setTotalElements(0);
				focalTransCountPage.setTotalPages(0);
				focalTransCountPage.setNumber(pageNo);
				focalTransCountPage.setLimit(pageSize);
				focalTransCountPage.setContent(null);
				return focalTransCountPage;
			}
		}else if(!StringUtils.isEmpty(queryId)){
			queryIds.add(queryId);
		}
		return focalMediaRepository.findFocalTransCountPage(queryIds,queryType,startTime,endTime,pageNo,pageSize);
	}
	
	/**
	 * 重点媒体转载列表
	 * @throws IOException 
	 * queryType:
	 * 为1时，查询某媒体的平台转载情况，此时queryId是媒体Id
	 * 为2时，查询某平台的媒体转载情况，此时queryId是平台Id
	 * 为0时，查询所有平台、媒体的转载情况，此时不会用到queryId
	 * */
	@Override
	public Map<String,Object> findFocalMediaTransPage(String queryId,String groupId,String mainGroup,String mediaId,Integer queryType,
			Integer publishStatus,Date startTime, Date endTime,int pageNo,int pageSize) throws IOException{
		Set<String> mediaIds=new HashSet<String>();
		Set<String> platformIds=new HashSet<String>();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<String> queryIds =new LinkedList<String>();
		PageResult<FocalMediaTrans> focalMediaTransPage=new PageResult<FocalMediaTrans>();
		if(!StringUtils.isEmpty(groupId)){
			if(queryType==0){
				queryType=3;
			}else{
				queryType=2;
			}
			if(customGroupService.findGroupByGroupId(groupId)!=null&&customGroupService.findGroupByGroupId(groupId).getPlatformIdList()!=null){
				queryIds =Arrays.asList(customGroupService.findGroupByGroupId(groupId).getPlatformIdList().split(","));}else{
					focalMediaTransPage.setTotalElements(0);
					focalMediaTransPage.setTotalPages(0);
					focalMediaTransPage.setNumber(pageNo);
					focalMediaTransPage.setLimit(pageSize);
					focalMediaTransPage.setContent(null);
					resultMap.put("pageResult",focalMediaTransPage);
					return resultMap;
				}
		}else if(!StringUtils.isEmpty(mainGroup)){
			if(queryType==0){
				queryType=3;
			}else{
				queryType=2;
			}
			if(platformService.findByPlatformTypeId(mainGroup)!=null){
				queryIds =platformService.findByPlatformTypeId(mainGroup);}else{
					focalMediaTransPage.setTotalElements(0);
					focalMediaTransPage.setTotalPages(0);
					focalMediaTransPage.setNumber(pageNo);
					focalMediaTransPage.setLimit(pageSize);
					focalMediaTransPage.setContent(null);
					resultMap.put("pageResult",focalMediaTransPage);
					return resultMap;
				}
		}else{
			queryIds.add(queryId);
		}
		focalMediaTransPage=focalMediaRepository.findFocalMediaTransPage(queryIds,queryType,publishStatus,mediaId,startTime,endTime,pageNo,pageSize);
		List<FocalMediaTrans> focalMediaTransList=focalMediaTransPage.getContent();
		StringBuffer sb = null;
		if(queryType!=null){
			if(queryType==2){
				platformIds.addAll(queryIds);
				if(focalMediaTransList!=null&&focalMediaTransList.size()>0){
					for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
						platformIds.add(focalMediaTrans.getPlatformId());
						mediaIds.add(focalMediaTrans.getMediaId());
					}
					resultMap.put("pageResult",focalMediaTransPage);
					sb= new StringBuffer();
					if(mediaIds!=null&&mediaIds.size()>0){
						for(String id:mediaIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("medias",focalMediaMapper.findFocalMediaByIds(sb.substring(0,sb.length()-1)));
					sb= new StringBuffer();
					if(platformIds!=null&&platformIds.size()>0){
						for(String id:platformIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("platforms",platformMapper.findPlatformByIds(sb.substring(0,sb.length()-1)));
				}
			}else if(queryType==1){
				mediaIds.addAll(queryIds);
				if(focalMediaTransList!=null&&focalMediaTransList.size()>0){
					for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
						platformIds.add(focalMediaTrans.getPlatformId());
						mediaIds.add(focalMediaTrans.getMediaId());
					}
					resultMap.put("pageResult",focalMediaTransPage);
					sb= new StringBuffer();
					if(mediaIds!=null&&mediaIds.size()>0){
						for(String id:mediaIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("medias",focalMediaMapper.findFocalMediaByIds(sb.substring(0,sb.length()-1)));
					sb= new StringBuffer();
					if(platformIds!=null&&platformIds.size()>0){
						for(String id:platformIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("platforms",platformMapper.findPlatformByIds(sb.substring(0,sb.length()-1)));
				}
			}else if(queryType==0){
				if(focalMediaTransList!=null&&focalMediaTransList.size()>0){
				for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
					platformIds.add(focalMediaTrans.getPlatformId());
					mediaIds.add(focalMediaTrans.getMediaId());
				}
				resultMap.put("pageResult",focalMediaTransPage);
				sb= new StringBuffer();
				if(mediaIds!=null&&mediaIds.size()>0){
					for(String id:mediaIds){
						sb.append("\"");
						sb.append(id);
						sb.append("\"");
						sb.append(',');
					}
				}
				resultMap.put("medias",focalMediaMapper.findFocalMediaByIds(sb.substring(0,sb.length()-1)));
				sb= new StringBuffer();
				if(platformIds!=null&&platformIds.size()>0){
					for(String id:platformIds){
						sb.append("\"");
						sb.append(id);
						sb.append("\"");
						sb.append(',');
					}
				}
				resultMap.put("platforms",platformMapper.findPlatformByIds(sb.substring(0,sb.length()-1)));
				}
			}else if(queryType==3){
				platformIds.addAll(queryIds);
				if(focalMediaTransList!=null&&focalMediaTransList.size()>0){
					for(FocalMediaTrans focalMediaTrans:focalMediaTransList){
						platformIds.add(focalMediaTrans.getPlatformId());
						mediaIds.add(focalMediaTrans.getMediaId());
					}
					resultMap.put("pageResult",focalMediaTransPage);
					sb= new StringBuffer();
					if(mediaIds!=null&&mediaIds.size()>0){
						for(String id:mediaIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("medias",focalMediaMapper.findFocalMediaByIds(sb.substring(0,sb.length()-1)));
					sb= new StringBuffer();
					if(platformIds!=null&&platformIds.size()>0){
						for(String id:platformIds){
							sb.append("\"");
							sb.append(id);
							sb.append("\"");
							sb.append(',');
						}
					}
					resultMap.put("platforms",platformMapper.findPlatformByIds(sb.substring(0,sb.length()-1)));
				}
			}
		}
		return resultMap;
	}

	/**
	 * 存储重点媒体转载记录
	 * @throws ParseException 
	 * @throws IOException 
	 * 
	 * */
	@Override
	public void saveFocalMediaTrans(List<Map<String,Object>> requestMapList) throws ParseException, IOException{
		String mediaId=null;
		List<String> updateIdList=new LinkedList<String>();
		List<FocalMediaTrans> addList=new LinkedList<FocalMediaTrans>();
		List<FocalMediaTrans> updateList=new LinkedList<FocalMediaTrans>();
		Map<String,FocalMediaTrans> updateListMap=null;
		List<Map> dataList=null;
		for(Map requestMap:requestMapList){
		mediaId=(String)requestMap.get("media_id");
		focalMediaRepository.overByMediaId(mediaId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(requestMap.get("home_page")!=null){
			dataList=(List<Map>)requestMap.get("home_page");
			if(dataList.size()>0){
				for(Map data:dataList){
					//主页的第一次数据传递
					if(data.containsKey("percent")){
						addList.add(new FocalMediaTrans(data.containsKey("originApp")? (MD5Util.encodeByMD5((String)data.get("transTitle")+(String)data.get("transTime"))):(data.get("transUrl")!=null?MD5Util.encodeByMD5((String)data.get("transUrl")):null),(String)data.get("unionId"),data.get("platformId")!=null?(String)data.get("platformId"):null,
								data.get("originalId")!=null?(String)data.get("originalId"):null,mediaId,
								data.get("transTitle")!=null?(String)data.get("transTitle"):null,data.get("transUrl")!=null?(String)data.get("transUrl"):null,data.get("transTime")!=null?sdf.parse((String)data.get("transTime")):null ,
								1,0,
								data.get("originalTitle")!=null?(String)data.get("originalTitle"):null,new Date(),
								data.get("percent")!=null?(Double)data.get("percent"):null));
						
						//主页的第一次后的数据传递
						//需要按照字段查询，unionId
					}else{
						if(data.get("isDeleted")==null||data.get("transUrl")==null){
							continue;
						}
						updateIdList.add(data.containsKey("originApp")? (MD5Util.encodeByMD5((String)data.get("transTitle")+(String)data.get("transTime"))):(data.get("transUrl")!=null?MD5Util.encodeByMD5((String)data.get("transUrl")):null));
					}
				}
			}
		}
		//非主页的数据传递
		if(requestMap.get("not_home_page")!=null){
			dataList=(List<Map>)requestMap.get("not_home_page");
			if(dataList.size()>0){
				for(Map data:dataList){
					addList.add(new FocalMediaTrans( data.containsKey("originApp")? (MD5Util.encodeByMD5((String)data.get("transTitle")+(String)data.get("transTime"))):(data.get("transUrl")!=null?MD5Util.encodeByMD5((String)data.get("transUrl")):null),data.get("unionId")!=null?(String)data.get("unionId"):null,data.get("platformId")!=null?(String)data.get("platformId"):null,
					data.get("originalId")!=null?(String)data.get("originalId"):null,mediaId,
					data.get("transTitle")!=null?(String)data.get("transTitle"):null,
					data.get("transUrl")!=null?(String)data.get("transUrl"):null,data.get("transTime")!=null?sdf.parse((String)data.get("transTime")):null,
					0,0,
					data.get("originalTitle")!=null?(String)data.get("originalTitle"):null,new Date(),
					data.get("percent")!=null?(Double)data.get("percent"):null));
				}
			}
		}
		if(addList.size()>0){
			focalMediaRepository.saveFocalMediaTrans(addList);
		}
		if(updateIdList.size()>0){
			updateListMap=focalMediaRepository.findByIds(updateIdList);}
		}
		FocalMediaTrans focalMediaTrans=null;
		Date transTime=null;
		Date now= new Date();
		Long publishSeconds=null;
		for(Map requestMap:requestMapList){
			if(requestMap.get("home_page")!=null){
				dataList=(List<Map>)requestMap.get("home_page");
					if(dataList.size()>0){
						for(Map data:dataList){
							//主页的第一次数据传递
							if(!data.containsKey("percent")){
								if(data.get("isDeleted")==null||data.get("transUrl")==null){
									continue;
								}
								focalMediaTrans=updateListMap.get(data.containsKey("originApp")? (MD5Util.encodeByMD5((String)data.get("transTitle")+(String)data.get("transTime"))):(data.get("transUrl")!=null?MD5Util.encodeByMD5((String)data.get("transUrl")):null));
								transTime=focalMediaTrans.getTransTime();
								publishSeconds=(now.getTime()-transTime.getTime())/1000;
								focalMediaTrans.setHomePageSeconds(publishSeconds.intValue());
								if((Integer)data.get("isDeleted")==1){
									focalMediaTrans.setPublishStatus(3);
								}else if((Integer)data.get("isDeleted")==0){
									focalMediaTrans.setPublishStatus(1);
								}
								updateList.add(focalMediaTrans);
							}
						}
					}
			}
		}
		if(updateList.size()>0){
		focalMediaRepository.updateFocalMediaTrans(updateList);}
	}
}