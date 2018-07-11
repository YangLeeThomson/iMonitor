package com.ronglian.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.Constants;
import com.ronglian.common.JsonResult;
import com.ronglian.common.PageResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.CustomMonitorGroup;
import com.ronglian.model.HotArticle;
import com.ronglian.model.Platform;
import com.ronglian.repository.SpreadTrackRepository;
import com.ronglian.service.ArticleInfoService;
import com.ronglian.service.ArticlePublishTrendService;
import com.ronglian.service.CommentAnalysisService;
import com.ronglian.service.CustomGroupService;
import com.ronglian.service.FocalMediaService;
import com.ronglian.service.HotArticleService;
import com.ronglian.service.PlatformComparedService;
import com.ronglian.service.PlatformService;
import com.ronglian.service.PlatformTransChannelService;
import com.ronglian.service.PlatformTransClassificationService;
import com.ronglian.service.PlatformTransMediaTypeService;
import com.ronglian.service.PlatformTypeService;
import com.ronglian.service.SpreadService;
import com.ronglian.service.SpreadTimeTrendService;
import com.ronglian.service.SpreadTrackService;
import com.ronglian.service.TransPeriodService;
import com.ronglian.service.UserService;
import com.ronglian.utils.MD5Util;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年7月1日 下午3:03:28
* @description:api
*/
@Slf4j
@RestController
@RequestMapping("/api")
public class iMonitorApi {

	@Autowired
	private PlatformService platformService;
	
	@Autowired
	private PlatformComparedService platformComparedService;
	
	@Autowired
	private PlatformTypeService platformTypeService;
	
	@Autowired
	private CustomGroupService customGroupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SpreadService spreadService;
	
	@Autowired
	private PlatformTransMediaTypeService platformTransMediaTypeService;
	
	@Autowired
	private PlatformTransChannelService platformTransChannelService;
	
	@Autowired
	private SpreadTimeTrendService spreadTimeTrendService;
	
	@Autowired
	private HotArticleService hotArticleService;
	
	@Autowired
	private ArticlePublishTrendService articlePublishTrendService;
	
	@Autowired
    private TransPeriodService transPeriodService;
	
	@Autowired
	private PlatformTransClassificationService platformTransClassificationService;
	
	
	@Autowired
	private FocalMediaService focalMediaService;
	@Autowired
	private CommentAnalysisService commentAnalysisService;
	
	@Autowired
	private SpreadTrackService spreadTrackService;
	
	@Value("${imonitor.api.key}")
	private String IMONITORAPIKEY;
	
	@Autowired
	private ArticleInfoService articleInfoService;
	@InitBinder
    public void initBinder(WebDataBinder binder){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // 严格限制日期转换
        sdf.setLenient(false);

        //true:允许输入空值，false:不能为空值 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

    }
	
	/**
	 * @Description: 校验苏州台传来的key、time值是否合法
	 * @param time
	 *            time参数
	 * @param key
	 *            校验值
	 * @return boolean
	 */
	private boolean paramVerification(String time, String key) {
		if (time == null || key == null)
			return false;

		long currentSecond = System.currentTimeMillis() / 1000;
		long requestSecond = Long.parseLong(time);
		if (currentSecond - requestSecond > 60 * 30) {
			return false;// 如果请求的时间距离现在超过30分，不通过
		}

		String suffixedTime = time + IMONITORAPIKEY;
		String md5Time = MD5Util.encodeByMD5(suffixedTime);
		String serverKey = md5Time.substring(0, 8);
		if (!serverKey.equals(key)) {
			return false;
		}

		return true;
	}
	/**
	 * 单篇文章传播情况统计
	 * */
	@RequestMapping(value = "/article/spread/info",method = RequestMethod.GET)
	public JsonResult ArticleSpreadAccount(@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			@RequestParam(value="unionId",required=true)String unionId,
			@RequestParam("time") String time, 
			@RequestParam("key") String key
			){
		return this.articleInfoService.getArticleTransCounted(unionId, pageNo, pageSize);
	}
	
	/**
	 * 分页查询所有平台列表
	 * */
	@RequestMapping(value = "/platform/find",method = RequestMethod.GET)
	public JsonResult platform(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		return new JsonResult(ResultCode.SUCCESS,"success",platformService.find(pageNo, pageSize));
	}
	
	/**
	 * 分页查询所有平台组列表
	 * */
	@RequestMapping(value = "/platform/type/find",method = RequestMethod.GET)
	public JsonResult platformType(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		return new JsonResult(ResultCode.SUCCESS,"success",platformTypeService.find(pageNo, pageSize));
	}
	
	/**
	 * 分页查询所有平台组列表
	 * */
	@RequestMapping(value = "/platform/custom/find",method = RequestMethod.GET)
	public JsonResult customGroup(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("time") String time, 
			@RequestParam("key") String key,
			@RequestParam("username") String username) {
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<CustomMonitorGroup> groupList=customGroupService.findGroupByUser(username);
		for(CustomMonitorGroup group:groupList) {
			String[] platformIds=group.getPlatformIdList().split(",");
			List<Platform> platformList=new ArrayList<Platform>();
			for(String platformId:platformIds) {
				Platform platform=platformService.findById(platformId);
				platform.setName(platform.getName()+"_"+platform.getPlatformTypeName());
				platformList.add(platform);
			}
			group.setPlatformList(platformList);
		}
		return new JsonResult(ResultCode.SUCCESS,"success",groupList);
	}
	
	/**
	 * 
	 * @Description: 传入platformTypeId、platformId、groupId得到平台id列表
	 * @param @param platformTypeId
	 * @param @param platformId
	 * @param @param groupId
	 * @param @return    参数  
	 * @return List<String>    返回类型  
	 * @throws
	 */
	private  List<String> getPlatformList(String platformId, String groupId) {
		
		List<String> platformIdList=null;
		if(StringUtils.isNotBlank(platformId)) {
			//根据platformId查询
			platformIdList=new ArrayList<String>();
			platformIdList.add(platformId);
		}
		if(StringUtils.isNotBlank(groupId)) {
			//根据groupId查询
			CustomMonitorGroup group = null;
			group = this.customGroupService.findGroupByGroupId(groupId);
			if(group != null){
				platformIdList = Arrays.asList(group.getPlatformIdList().split(","));
			}else{
				platformIdList = new ArrayList<String>();
				platformIdList.add(groupId);
			}
		}
		return platformIdList;
	}
	
	/**
	 * 传播情况统计接口
	 * */
	@RequestMapping(value = "/spread/condition/analysis",method = RequestMethod.GET)
	public JsonResult spreadConditionAnalysis(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",spreadService.getPlatformArticleInfo(platformIdList,platformTypeId,startTime,accountType));
	}
	/**
	 * 5.2各平台作品数占比统计
	 * */
	@RequestMapping(value = "/platform/article/count",method = RequestMethod.GET)
	public JsonResult platformArticleCount(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true) Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3") Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformArticleNumAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	
	/**
	 * 5.3原创文章占比统计
	 * */
	@RequestMapping(value = "/article/original/proportion",method = RequestMethod.GET)
	public JsonResult articleOriginalProportion(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getOriginalArticleNumAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	/**
	 * 文章数量时间环比统计接口
	 * */
	@RequestMapping(value = "/article/time/ratio",method = RequestMethod.GET)
	public JsonResult articleTimeRatio(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getWeekedCircleAccounted(startTime,accountType,platformTypeId,platformIdList));
	}
	
	/**
	 * 传播地域分布统计
	 * 
	 * */
	@RequestMapping(value = "/spread/area/analysis",method = RequestMethod.GET)
	public JsonResult spreadAreaAnalysis(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getSpreadAreaCurrent(platformTypeId,platformIdList,startTime,accountType));
	}
	/**
	 * 某一地域转载数量
	 * 
	 * */
	@RequestMapping(value = "/spread/provience/transnum",method = RequestMethod.GET)
	public JsonResult spreadProvienceTransnum(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="province",required=true) String province,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getProvienceTransnum(platformTypeId,platformIdList,province,startTime,accountType));
	}
	/**
	 * 地区转载列表
	 * 
	 * */
	@RequestMapping(value = "/spread/area/list",method = RequestMethod.GET)
	public JsonResult spreadAreaList(
			@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="province",required=true) String province,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId) || StringUtils.isNotBlank(platformId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(platformId, groupId);
			}
		}
		if("广西".equals(province)){
			province = "广西壮族自治区";
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getSpreadAreaTransList(platformTypeId,platformIdList,province,startTime,accountType,pageNo,pageSize));
	}
	
	/**
	 * 平台转载排行
	 * */
	@RequestMapping(value = "/platform/trans/bang",method = RequestMethod.GET)
	public JsonResult getPlatformTrans(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformTrans(platformTypeId,platformIdList,startTime,accountType));
	}
	
	/**
	 * 平台转载排行(非首页)
	 * */
	@RequestMapping(value = "/platform/trans/list",method = RequestMethod.GET)
	public JsonResult platformTransList(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		List<String> platformIdList = null;
		if(StringUtils.isNotBlank(groupId)){
			if("normal".equalsIgnoreCase(groupId)){
				platformIdList = null;
			}else{
				platformIdList = getPlatformList(null, groupId);
			}
		}
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadService.getPlatformTrans2(platformTypeId,platformIdList,startTime,accountType));
	}
	
	@RequestMapping(value = "/platform/trans/mediatype/proportion",method = RequestMethod.GET)
	public JsonResult platformTransMediatypeProportion(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
			               @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
			               @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
			               @RequestParam(value = "startTime", required = true) Date startTime,
			               @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType,
			               @RequestParam("time") String time, 
			   			@RequestParam("key") String key)throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("platformId:"+platformId+",platformTypeId:"+platformTypeId+",startTime:"+startTime+",accountType:"+accountType);
		Date endTime = null;
		if(accountType == 0) {
			endTime = Utils.plusDay(1,startTime);
		}else if(accountType == 3){
			//周查询
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			endTime =Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType == 2){
			//月查询
			try {
				String stringYear = Utils.dateToString(startTime,"yyyy");
				Integer year = Integer.parseInt(stringYear);
				String stringMonth = Utils.dateToString(startTime,"MM");
				Integer month = Integer.parseInt(stringMonth);
				System.out.println(year+"-"+month);
				List<Date> firstDayAndLastDayOfMonth = Utils.getFirstAndLastdayOfMonth(year,month);
				endTime = Utils.plusDay(1,firstDayAndLastDayOfMonth.get(1));
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			//年查询
			endTime = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
		}
		List<HashMap<String,String>> return_list = platformTransMediaTypeService.transMediaTypeProportion(accountType, platformTypeId, platformId, groupId, startTime, endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}
	
	@RequestMapping(value = "/platform/trans/channel/proportion",method = RequestMethod.GET)
	public JsonResult platformTransChannelProportion(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
			               @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
			               @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
			               @RequestParam(value = "startTime", required = true) Date startTime,
			               @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType,
			               @RequestParam("time") String time, 
				   		   @RequestParam("key") String key) throws IOException{
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("platformId:"+platformId+",platformTypeId:"+platformTypeId+",startTime:"+startTime+",accountType:"+accountType);
		Date endTime = null;
		if(accountType == 0) {
			endTime = Utils.plusDay(1,startTime);
		}else if(accountType == 3){
			//周查询
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			endTime = Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType == 2){
			//月查询
			try {
				String stringYear = Utils.dateToString(startTime,"yyyy");
				Integer year = Integer.parseInt(stringYear);
				String stringMonth = Utils.dateToString(startTime,"MM");
				Integer month = Integer.parseInt(stringMonth);
				System.out.println(year+"-"+month);
				List<Date> firstDayAndLastDayOfMonth = Utils.getFirstAndLastdayOfMonth(year,month);
				endTime = Utils.plusDay(1,firstDayAndLastDayOfMonth.get(1));
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			//年查询
			endTime = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
		}
		List<HashMap<String,String>> return_list = platformTransChannelService.transChannelProportion(accountType, platformTypeId,platformId, groupId, startTime, endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}
	
	/**
	 * 传播时间趋势接口
	 * */
	@RequestMapping(value = "/spread/time/trend",method = RequestMethod.GET)
	public JsonResult spreadTimeTrend(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
	   		@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.info("传播时间趋势接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getSpreadTimeTend(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 媒体转载排行接口,首页
	 * */
	@RequestMapping(value = "/platform/trans/media/bang",method = RequestMethod.GET)
	public JsonResult platformTransMediaBang(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
	   		@RequestParam("key") String key) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.info("媒体转载排行接口");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getMediaOrder(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 分页查询列表
	 * */
	@RequestMapping(value = "/article/hot/list",method = RequestMethod.GET)
	public JsonResult articleHotList(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
				           @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
				           @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
				           @RequestParam(value = "original",required = false,defaultValue = "3") String original, 
						   @RequestParam(value = "order",required = false,defaultValue = "2") String order, 
						   @RequestParam(value = "startTime", required = true) Date startTime, 
						   @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType, 
						   @RequestParam("pageNo") int pageNo,
						   @RequestParam("pageSize") int pageSize,
						   @RequestParam("time") String time, 
					   		@RequestParam("key") String key) throws IOException{
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		PageResult<HotArticle> result = null;
		Date endDate = null;
		if(accountType == 0) {
			//天查询
			endDate = Utils.plusDay(1,startTime);
		}else if(accountType == 3){
			//周查询
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			endDate = Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType == 2){
			//月查询
			try {
				String stringYear = Utils.dateToString(startTime,"yyyy");
				Integer year = Integer.parseInt(stringYear);
				String stringMonth = Utils.dateToString(startTime,"MM");
				Integer month = Integer.parseInt(stringMonth);
				System.out.println(year+"-"+month);
				List<Date> firstDayAndLastDayOfMonth = Utils.getFirstAndLastdayOfMonth(year,month);
				endDate = Utils.plusDay(1,firstDayAndLastDayOfMonth.get(1));
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			//年查询
			endDate = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
		}
		//original 0:全部，1：原创，2：非原创
		result = hotArticleService.findPageList(accountType, platformTypeId, platformId,groupId,original, order, startTime, endDate, pageNo, pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",result);
	}
	
	/**
	 * 分页查询列表
	 * */
	@RequestMapping(value = "/article/publish/trend",method = RequestMethod.GET)
	public JsonResult articlePublishTrend(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
            @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
            @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
            @RequestParam(value = "startTime", required = true) Date startTime,
            @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType,
            @RequestParam("time") String time, 
	   		@RequestParam("key") String key)throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		Date endTime = null;
		Date showEndTime = null;
		if(accountType == 0) {
			//天查询
			endTime = Utils.plusDay(1,startTime);
			showEndTime = startTime;
		}else if(accountType == 3){
			//周查询
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			showEndTime = Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD);
			endTime = Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType == 2){
			//月查询
			try {
				String stringYear = Utils.dateToString(startTime,"yyyy");
				Integer year = Integer.parseInt(stringYear);
				String stringMonth = Utils.dateToString(startTime,"MM");
				Integer month = Integer.parseInt(stringMonth);
				System.out.println(year+"-"+month);
				List<Date> firstDayAndLastDayOfMonth = Utils.getFirstAndLastdayOfMonth(year,month);
				endTime = Utils.plusDay(1,firstDayAndLastDayOfMonth.get(1));
				showEndTime = firstDayAndLastDayOfMonth.get(1);
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			//年查询
			endTime = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
			showEndTime = endTime;
		}
		
		List<HashMap<String,String>> return_list = articlePublishTrendService.articlePublishTrend(accountType, platformTypeId, platformId, groupId, startTime, endTime);
		if(!return_list.isEmpty()) {
			String lastDate = Utils.dateToString(showEndTime,Constants.DEFAULT_DATE_FORMAT_YMD);
			List<String> dateList = null;
			if(accountType==0)  {
				dateList = new ArrayList<>();
				for(int i=1;i<=24;i++) {
					if(i<10) {
						dateList.add("0"+i);
					}else {
						dateList.add(i+"");
					}
				}
			}else {
				try {
					dateList = Utils.getDatesBetweenTwoDate(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD),lastDate,Constants.DEFAULT_DATE_FORMAT_YMD);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			List<HashMap<String,String>> template_list = new ArrayList<>();
			for(String dateStr:dateList) {
				HashMap<String, String> return_map = new HashMap<String,String>();
				return_map.put("name","作品数");
				return_map.put("value","0");
				if(accountType != 0) {
					return_map.put("date",dateStr);
					return_map.put("week",Utils.getWeekOfDate(Utils.stringToDate(dateStr, Constants.DEFAULT_DATE_FORMAT_YMD)));
				}else {
					return_map.put("date",dateStr+"时");
				}
				template_list.add(return_map);
			}
			for(HashMap<String,String> hashMap:template_list) {
				String publishDate = hashMap.get("date");
				for(HashMap<String,String> returnListMap:return_list) {
					if(publishDate.equals(returnListMap.get("name"))){
						hashMap.put("value",returnListMap.get("value")+"");
					}
				}
			}
			return_list = template_list;
		}
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}
	
	/**
	 * 转载时段占比接口
	 * */
	@RequestMapping(value = "/article/trans/period",method = RequestMethod.GET)
	public JsonResult articleTransPeriod(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam("time") String time, 
	   		@RequestParam("key") String key){
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
//		log.debug("accountType:"+accountType);
		return new JsonResult(ResultCode.SUCCESS,"success",this.transPeriodService.getTransPeriod(platformTypeId,platformId,groupId,startTime,accountType));
	}
	
	/**
	 * 综合数值排行接口
	 * */
	@RequestMapping(value = "/article/trans/comprehensive/bang",method = RequestMethod.GET)
	public JsonResult getComprehensiveNum(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType,
			@RequestParam(value="orderCode",required=false,defaultValue="1")int orderCode,
			@RequestParam(value="page",required=false,defaultValue="1")int page,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize,
			@RequestParam("time") String time, 
	   		@RequestParam("key") String key)throws IOException {
		log.info("【开始】Controller_api_getComprehensiveNum： "+(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date())));
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		//log.debug("accountType:"+accountType);
		return new JsonResult(ResultCode.SUCCESS,"success",this.transPeriodService.getComprehensiveNum(platformTypeId,platformId,groupId,startTime,accountType,orderCode,page,pageSize));
	}
	
	/**
	 * 媒体转载排行接口,媒体转载分析页
	 * */
	@RequestMapping(value = "/platform/trans/media/bang2",method = RequestMethod.GET)
	public JsonResult platformTransMediaBang2(@RequestParam(value="platformTypeId",required=false) String platformTypeId,
			@RequestParam(value="platformId",required=false) String platformId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="startTime",required=true)Date startTime,
			@RequestParam(value="accountType",required=false,defaultValue="3")int accountType,
			@RequestParam(value="mediaType",required=false)Integer mediaType,
			@RequestParam(value="channel",required=false)Integer channel,
			@RequestParam(value="pageNo",required=false,defaultValue="1")int pageNo,
			@RequestParam(value="pageSize",required=false,defaultValue="10")int pageSize,
			@RequestParam("time") String time, 
	   		@RequestParam("key") String key)throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.info("媒体转载排行接口Super");
		return new JsonResult(ResultCode.SUCCESS,"success",this.spreadTimeTrendService.getMediaOrderSuper(platformTypeId,platformId,groupId,startTime,accountType,mediaType,channel,pageNo,pageSize));
	}
	
	@RequestMapping(value = "/platform/trans/classification/proportion",method = RequestMethod.GET)
	public JsonResult platformTransClassificationProportion(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
			               @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
			               @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
			               @RequestParam(value = "startTime", required = true) Date startTime,
			               @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType,
			   			@RequestParam("time") String time, 
				   		@RequestParam("key") String key)throws IOException {
		log.debug("platformId:"+platformId+",platformTypeId:"+platformTypeId+",startTime:"+startTime+",accountType:"+accountType);
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		Date endTime = null;
		if(accountType == 0) {
			endTime = Utils.plusDay(1,startTime);
		}else if(accountType == 3){
			//周查询
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			endTime = Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType == 2){
			//月查询
			try {
				String stringYear = Utils.dateToString(startTime,"yyyy");
				Integer year = Integer.parseInt(stringYear);
				String stringMonth = Utils.dateToString(startTime,"MM");
				Integer month = Integer.parseInt(stringMonth);
				System.out.println(year+"-"+month);
				List<Date> firstDayAndLastDayOfMonth = Utils.getFirstAndLastdayOfMonth(year,month);
				endTime = Utils.plusDay(1,firstDayAndLastDayOfMonth.get(1));
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			//年查询
			endTime = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
		}
		List<HashMap<String,String>> return_list = platformTransClassificationService.transArticleClassificationProportion(accountType, platformTypeId, platformId, groupId, startTime, endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}
	
	/**
	 * Top10列表
	 * */
	@RequestMapping(value = "/platform/trans/classification/top/ten",method = RequestMethod.GET)
	public JsonResult classificationTopTen(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
			               @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
			               @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
			               @RequestParam(value = "startTime", required = true) Date startTime,
			               @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType,
			               @RequestParam(value = "classification", required = true,defaultValue="1") int classification,
			               @RequestParam(value = "other", required = false,defaultValue="") String other)throws IOException {
		log.debug("platformId:"+platformId+",platformTypeId:"+platformTypeId+",startTime:"+startTime+",accountType:"+accountType);
		Date endDate = null;
		if(accountType==0) {
			endDate = Utils.plusDay(1,startTime);
		}else if(accountType==3) {
			String lastDayOfWeek = Utils.getFirstDayOfWeek(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD));
			endDate = Utils.plusDay(1,Utils.stringToDate(lastDayOfWeek, Constants.DEFAULT_DATE_FORMAT_YMD));
		}else if(accountType==2) {
			try {
				endDate = Utils.plusDay(1,Utils.getLastDayOfMonth(Utils.dateToString(startTime, Constants.DEFAULT_DATE_FORMAT_YMD)));
			}catch (Exception e) {
				// TODO: handle exception
				log.error("获取月份最后一天",e);
			}
		}else {
			endDate = Utils.plusDay(1,Utils.getYearLastDay(Utils.dateToString(startTime,Constants.DEFAULT_DATE_FORMAT_YMD), Constants.DEFAULT_DATE_FORMAT_YMD));
		}
		List<HashMap<String,String>> return_list = platformTransClassificationService.transArticleClassificationTop10(classification,other, accountType, platformTypeId, platformId, groupId, startTime, endDate, 1, 10);
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}
	
	//重点媒体
	@RequestMapping(value = "/focalmedia/findTransCount",method = RequestMethod.GET)
	public JsonResult findFocalTransCountPage(
			@RequestParam(value="queryId",required=false) String queryId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="mainGroup",required=false) String mainGroup,
			@RequestParam(value="queryType",required=true) Integer queryType,
			@RequestParam(value="startTime",required=false) Long startTime,
			@RequestParam(value="endTime",required=false) Long endTime,
			@RequestParam("time") String time, 
			@RequestParam("key") String key,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		Calendar cStart = Calendar.getInstance();
		cStart.setTimeInMillis(startTime!=null?startTime:0);
		Calendar cEnd = Calendar.getInstance();
		if(endTime!=null)
			cEnd.setTimeInMillis(endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",focalMediaService.findFocalTransCountPage(queryId,groupId,mainGroup,queryType,
				cStart.getTime(), cEnd.getTime(),pageNo,pageSize));
	}
	
	@RequestMapping(value = "/focalmedia/findTransPage",method = RequestMethod.GET)
	public JsonResult findFocalMediaTransPage(
			@RequestParam(value="queryId",required=false) String queryId,
			@RequestParam(value="groupId",required=false) String groupId,
			@RequestParam(value="mainGroup",required=false) String mainGroup,
			@RequestParam(value="mediaId",required=false) String mediaId,
			@RequestParam(value="queryType",required=true) Integer queryType,
			@RequestParam(value="publishStatus",required=false) Integer publishStatus,
			@RequestParam(value="startTime",required=false) Long startTime,
			@RequestParam(value="endTime",required=false) Long endTime,
			@RequestParam("time") String time, 
			@RequestParam("key") String key,
			@RequestParam(value="pageSize",required=false,defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		Calendar cStart = Calendar.getInstance();
		cStart.setTimeInMillis(startTime!=null?startTime:0);
		Calendar cEnd = Calendar.getInstance();
		if(endTime!=null)
			cEnd.setTimeInMillis(endTime);
		return new JsonResult(ResultCode.SUCCESS,"success",focalMediaService.findFocalMediaTransPage(queryId,groupId,mainGroup,mediaId,queryType,
				publishStatus,cStart.getTime(), cEnd.getTime(),pageNo,pageSize));
	}
	//评论
	/**
	 * 分页查询列表
	 * @throws IOException 
	 * */
	@RequestMapping(value = "/comment/findComments",method = RequestMethod.GET)
	public JsonResult find(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId,
				           @RequestParam(value = "orderField", required = false, defaultValue = "") String orderField,
				           @RequestParam("time") String time, 
							@RequestParam("key") String key,
						   @RequestParam("pageNo") int pageNo,
						   @RequestParam("pageSize") int pageSize) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("findComments:unionId:"+unionId+",orderField:"+orderField+",pageNo:"+pageNo+",pageSize:"+pageSize);
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.findPageList(orderField,unionId,pageNo,pageSize));
	}
	@RequestMapping(value = "/comment/getDistribute",method = RequestMethod.GET)
	public JsonResult getDistribute(@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId,
			@RequestParam("time") String time, 
			@RequestParam("key") String key,
				           @RequestParam(value = "queryType", required = false, defaultValue = "") Integer queryType) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("getCommentDistribute:queryType:"+queryType+",unionId:"+unionId);
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentDistribute(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getWordCloud",method = RequestMethod.GET)
	public JsonResult getWordCloud(@RequestParam("time") String time, 
			@RequestParam("key") String key,@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("getWordCloud:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("1");
		queryType.add("2");
		queryType.add("3");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getTags",method = RequestMethod.GET)
	public JsonResult getTags(@RequestParam("time") String time, 
			@RequestParam("key") String key,@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("getTags:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("6");
		queryType.add("7");
		queryType.add("8");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getOpinions",method = RequestMethod.GET)
	public JsonResult getOpinions(@RequestParam("time") String time, 
			@RequestParam("key") String key,@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("getOpinions:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("4");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	@RequestMapping(value = "/comment/getSentiment",method = RequestMethod.GET)
	public JsonResult getSentiment(@RequestParam("time") String time, 
			@RequestParam("key") String key,@RequestParam(value = "unionId", required = false, defaultValue = "") String unionId) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		log.debug("getSentiment:unionId:"+unionId);
		List<String> queryType =new LinkedList<String>();
		queryType.add("5");
		return new JsonResult(ResultCode.SUCCESS,"success",commentAnalysisService.getCommentAnalysis(queryType,unionId));
	}
	
	@RequestMapping(value = "/platform/compared/find",method = RequestMethod.GET)
	public JsonResult findPlatformGroupCompare(@RequestParam(value="userId",required=false) String userId,
			@RequestParam("time") String time, 
			@RequestParam("key") String key,
			HttpServletRequest request,
			@RequestParam(value="today",required=true)Date today,
			@RequestParam(value="accountType",required=false,defaultValue="3")Integer accountType
			) {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		if(StringUtils.isBlank(userId)){
			userId = (String) request.getSession().getAttribute("userId");
		}
		return this.platformComparedService.findPlatformGroupCompare(userId, today, accountType);
	}
	
	@RequestMapping(value = "/article/anaylsis/spread/track",method = RequestMethod.GET)
	public JsonResult articleSpreadTrack(@RequestParam(value="unionId",required=false) String unionId,
			@RequestParam("time") String time, 
			@RequestParam("key") String key) throws IOException {
		if (!paramVerification(time, key)) {
			log.debug("接口请求合法性验证未通过！time:"+time+",key:"+key);
			return new JsonResult(ResultCode.PARAMS_ERROR,"非法的接口调用！");
		}
		
		return new JsonResult(ResultCode.SUCCESS,"success",spreadTrackService.spreadTrack(unionId));
	}
}
