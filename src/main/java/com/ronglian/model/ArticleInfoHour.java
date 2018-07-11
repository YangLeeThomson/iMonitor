package com.ronglian.model;

import java.util.Date;

import com.ronglian.common.Constants;
import com.ronglian.utils.Utils;

import lombok.Data;


/**
 * 
 * @Description: mysql存储每篇文章点击数各数据 实体类
 * @author sunqian 
 * @date 2018年6月21日 下午4:26:01
 */
@Data
public class ArticleInfoHour {
	
	private String union_id;//unionid
	private String platform_id;
	private String platform_type_id;
	private String article_id;
	private float comprehensive_num;
	private int comment_num;
	private int click_num;
	private int distinct_user_click_num;
	private int thumbs_num;
	private int award_num;
	private int share_num;
	private int trans_num;
	private int collect_num;
	private String json_nums;
	private String create_time;
	private int status;
	
	public ArticleInfoHour(){
		this.create_time=Utils.dateToString(new Date(), Constants.DEFAULT_DATE_FORMAT_YMD);
	}
}
