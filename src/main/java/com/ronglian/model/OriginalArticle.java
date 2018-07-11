package com.ronglian.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronglian.utils.UUIDUtil;

import lombok.Data;


/**
 * 
 * @Description: 原始文章实体
 * @author public
 * @date 2018年6月15日 下午5:15:20
 */
@Data
public class OriginalArticle{
	
	
	//主键id
		private String id;
		//是否删除，0否1删除
		private int deleted;
		//资讯id
		private String media_id;  
		//来自哪个平台0 看苏州 1无线苏州 2 微博 3 微信4名城苏州 。。。。
		private String origin_app;
		//资讯url
		private String origin_url; 
		//资讯标题
		private String title; 	
		//资讯正文
		private String content;
		//记者
		private String repoter;
		//原创或转载的来源
		private String source;
		//资讯发布时间（utc时间戳）
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
		private Date public_time;
		//所属栏目
		private String column;
		//资讯状态 0发布 1更新 2撤回（下架）
		private int media_status;
		//创建时间
		private Date create_time;
		//租户id
		private String tenant_id;
		//平台id
		private String platform_id;
		
		private int commentNum;
		private int thumbsNum;
		private int transNum;
		//分类
		private int classification;
		
		public OriginalArticle(){
			this.id=UUIDUtil.getUUID32();
		}
	
	
}
