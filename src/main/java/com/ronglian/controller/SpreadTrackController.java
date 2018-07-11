package com.ronglian.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronglian.common.JsonResult;
import com.ronglian.common.ResultCode;
import com.ronglian.service.SpreadTrackService;


/**
* @author: 黄硕/huangshuo
* @date:2018年6月28日 下午10:56:43
* @description:文章传播路径
*/
@RestController
//@Slf4j
@RequestMapping("/article")
public class SpreadTrackController {
	
	@Autowired
	private SpreadTrackService spreadTrackService;
	
	@RequestMapping(value = "/track",method = RequestMethod.GET)
	public JsonResult articleTrack(@RequestParam(value="unionId",required=true) String unionId) throws IOException {
		return new JsonResult(ResultCode.SUCCESS,"success",spreadTrackService.spreadTrack(unionId));
	}

}
