package com.ronglian.service;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

/**
* @author: 黄硕/huangshuo
* @date:2018年6月28日 下午9:08:06
* @description:描述
*/
public interface SpreadTrackService {
	public JSONObject spreadTrack(String unionId) throws IOException ;
}
