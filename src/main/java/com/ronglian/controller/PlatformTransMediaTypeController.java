package com.ronglian.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.Constants;
import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.service.PlatformTransMediaTypeService;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月29日 下午1:37:24
* @description:平台文章转载媒体类型
*/
@RestController
@Slf4j
@RequestMapping("/platform/trans/mediatype")
public class PlatformTransMediaTypeController {

	@Autowired
	private PlatformTransMediaTypeService platformTransMediaTypeService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // 严格限制日期转换
        sdf.setLenient(false);

        //true:允许输入空值，false:不能为空值 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

    }
	
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult find(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
			               @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
			               @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
			               @RequestParam(value = "startTime", required = true) Date startTime,
			               @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType)throws IOException {
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
}
