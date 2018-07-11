package com.ronglian.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ronglian.common.PageResult;
import com.ronglian.common.ResultCode;
import com.ronglian.model.HotArticle;
import com.ronglian.service.HotArticleService;
import com.ronglian.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月28日 上午3:04:19
* @description:描述
*/
@RestController
@Slf4j
@RequestMapping("/hotarticle")
public class HotArticleController {
	
	@Autowired
	private HotArticleService hotArticleService;
	
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
//	@RequestParam(value = "startTime", required = true) String startTime,
	public JsonResult find(@RequestParam(value = "platformId", required = false, defaultValue = "") String platformId,
				           @RequestParam(value = "platformTypeId", required = false, defaultValue = "") String platformTypeId,
				           @RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
				           @RequestParam(value = "original",required = false,defaultValue = "3") String original, 
						   @RequestParam(value = "order",required = false,defaultValue = "2") String order, 
						   @RequestParam(value = "startTime", required = true) Date startTime, 
						   @RequestParam(value = "accountType", required = true,defaultValue="1") int accountType, 
						   @RequestParam("pageNo") int pageNo,
						   @RequestParam("pageSize") int pageSize) throws IOException{
		log.debug("pageNo:"+pageNo+",pageSize:"+pageSize);
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

}
