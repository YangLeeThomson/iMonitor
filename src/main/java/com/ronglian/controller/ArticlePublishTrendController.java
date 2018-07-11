package com.ronglian.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ronglian.service.ArticlePublishTrendService;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月28日 上午4:26:05
* @description:作品发布时间趋势
*/
@RestController
@Slf4j
@RequestMapping("/article/publish/trend")
public class ArticlePublishTrendController {
	
	@Autowired
	private ArticlePublishTrendService articlePublishTrendService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        // 严格限制日期转换
        sdf.setLenient(false);

        //true:允许输入空值，false:不能为空值 
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

    }
	
	/**
	 * 分页查询列表
	 * */
	@RequestMapping(value = "/find",method = RequestMethod.GET)
	public JsonResult find(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
            @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
            @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
            @RequestParam(value = "startTime", required = true) Date startTime,
            @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType)throws IOException {
		log.debug("/article/publish/trend/find");


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
					if(publishDate.equals(returnListMap.get("name")+"时")){
						hashMap.put("value",returnListMap.get("value")+"");
					}
				}
			}
			return_list = template_list;
		}
		return new JsonResult(ResultCode.SUCCESS,"success",return_list);
	}

}
