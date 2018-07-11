package com.ronglian.utils;

import java.util.*;

/** 
 * <p>Copyright: All Rights Reserved</p>  
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
 * <p>Description: 系统共用的常量 </p> 
 * <p>Author:xpguo/郭晓鹏</p>
 */
public class ConstantUtil {
	
	public final static String SALT = "ronglian";
	public final static long INTERVAL = 3000;
	public final static String TOKEN_URI = "/asserver/appToken";
	public final static String LOGIN_URI = "/asserver/JGauthorize";
	/*
	 * 数据字典
	 */
	
	//相关聚类
	public final static String CLUSTER_RELEVANT_CLUSTERS = "relevantClusters";
	//聚类的相关文档
	public final static String CLUSTER_RELEVANT_NEWS = "relevantNews";
			
	/**
	 * <p>Description: 聚类簇中所有文档集</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields CLUSTER_ALL_ARTICLES 
	 */
	public final static String CLUSTER_ARTICLES = "articles";
	
	/**
	 * <p>Description: 聚类簇中所有文档集</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields CLUSTER_ALL_ARTICLES 
	 */
	public final static String CLUSTER_ALL_ARTICLES = "allArticles";

	/**
	 * <p>Description:系统字典根节点-新闻内容分类 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_NEWS_CLASSIFICATION 
	 */
	public final static String DIC_ROOT_NEWS_CLASSIFICATION = "news_classification";
	
	/**
	 * <p>Description: 系统字典根节点-网站来源</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_WEBPAGE 
	 */
	public final static String DIC_ROOT_WEB_SOURCE = "web_source";
	
	/**
	 * <p>Description:标签类型-地域 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_REGION 
	 */
	public final static String DIC_LABEL_TYPE_REGION = "region";
	
	/**
	 * <p>Description: 标签类型-网站来源</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_LABEL_TYPE_SOURCE_ORG 
	 */
	public final static String DIC_LABEL_TYPE_SOURCE_ORG = "sourceOrg";
	
	/**
	 * <p>Description: 标签类型-新闻分类</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_LABEL_TYPE_CLASSIFIACATION 
	 */
	public final static String DIC_LABEL_TYPE_CLASSIFIACATION= "classification";
	
	/**
	 * <p>Description:标签类型-新闻原始分类 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_CLASSIFICATION_ORIGINAL 
	 */
	public final static String DIC_LABEL_TYPE_CLASSIFICATION_ORIGINAL= "classificationOriginal";
	/**
	 * <p>Description: 标签类型-新闻类型</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_LABEL_TYPE_NEWS_TYPE 
	 */
	public final static String DIC_LABEL_TYPE_NEWS_TYPE = "newsType";
	
	/**
	 * <p>Description:标签类型-报道类型 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_REPORT_TYPE 
	 */
	public final static String DIC_LABEL_TYPE_REPORT_TYPE = "reportType";
	
	/**
	 * <p>Description:标签类型-新闻原创性 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_ORIGINALITY 
	 */
	public final static String DIC_LABEL_TYPE_ORIGINALITY = "originality";
	
	/**
	 * <p>Description:标签类型-介质/载体 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_CARRIER 
	 */
	public final static String DIC_LABEL_TYPE_CARRIER = "carrier";
	
	/**
	 * <p>Description:标签类型-数据集 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_DATASET 
	 */
	public final static String DIC_LABEL_TYPE_DATASET = "dataset";
	
	/**
	 * <p>Description: 标签类型-事件新闻分类</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_LABEL_TYPE_EVENT_TYPE 
	 */
	public final static String DIC_LABEL_TYPE_EVENT_TYPE = "eventType";
	
	/**
	 * <p>Description: 标签类型-网站属性</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_LABEL_TYPE_WEBSITE_PROPERTY 
	 */
	public final static String DIC_LABEL_TYPE_WEBSITE_PROPERTY = "websiteProperty";
	
	/**
	 * <p>Description:系统字典根节点-排行周期 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_RANKING_CYCLE 
	 */
	public final static String DIC_ROOT_RANKING_CYCLE = "ranking_cycle";
	
	/**
	 * <p>Description: 系统字典根节点-排行类型</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_RANKING_TYPE 
	 */
	public final static String DIC_ROOT_RANKING_TYPE = "ranking_type";
	
	/**
	 * <p>Description: 系统字典根节点-报道类型</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_REPORT_TYPE 
	 */
	public final static String DIC_ROOT_REPORT_TYPE = "report_type";
	
	/**
	 * <p>Description:热点排行的前10条新闻的评论做分析</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_RANKING_COMMENT_ANALYSIS_NUM 
	 */
	public final static int HOT_RANKING_COMMENT_ANALYSIS_NUM = 10;
	
	/**
	 * <p>Description:热点新闻点击榜 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_NEWS_CLICK_LIST 
	 */
	public final static String HOT_NEWS_CLICK_LIST = "点击榜";
	
	/**
	 * <p>Description:热点新闻跟帖榜 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_NEWS_POST_LIST 
	 */
	public final static String HOT_NEWS_POST_LIST = "跟帖榜";
	
	/**
	 * <p>Description: 系统字典根节点-新闻标签</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_LABEL 
	 */
	public final static String DIC_ROOT_LABEL = "label";
	
	/**
	 * <p>Description: 系统字典根节点-事件分类</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_EVENT_CLASSIFICATION 
	 */
	public final static String DIC_ROOT_EVENT_CLASSIFICATION = "event_classification";
	
	/**
	 * <p>Description: 系统字典根节点-专题分类</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields DIC_ROOT_SPECIAL_CLASSIFICATION 
	 */
	public final static String DIC_ROOT_SPECIAL_CLASSIFICATION = "special_classification";
	
	/**
	 * <p>Description: 系统字典－新闻线索词汇字典</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_ROOT_CLUE_WORDS 
	 */
	public final static String DIC_ROOT_CLUE_WORDS = "clue_words";
	/**
	 * <p>Description: 系统字典－热点新闻排行网站字典</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_ROOT_CLUE_WORDS 
	 */
	public final static String DIC_ROOT_HOT_WEB = "hot_web";
	/**
	 * <p>Description: 系统字典－今日头条网站字典</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_ROOT_CLUE_WORDS 
	 */
	public final static String DIC_ROOT_TOP_WEB = "top_web";
	
	/**
	 * <p>Description: 系统字典－热点排行网站字典</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_ROOT_CLUE_WORDS 
	 */
	public final static String DIC_ROOT_HOTWEB = "热点网站";
	
	/**
	 * <p>Description: 系统字典－头条网站字典</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields DIC_ROOT_CLUE_WORDS 
	 */
	public final static String DIC_ROOT_TOPWEB = "头条网站";
//	
//	/**
//	 * <p>Description: 系统停用词</p>
//	 * <p>Author:jlchen/陈金梁</p>
//	 * @Fields DIC_ROOT_SYS_STOPWORD 
//	 */
//	public final static String DIC_ROOT_SYS_STOPWORD = "sys_stopword";
//	
//	/**
//	 * <p>Description: 自定义停用词</p>
//	 * <p>Author:jlchen/陈金梁</p>
//	 * @Fields DIC_ROOT_CUS_STOPWORD 
//	 */
//	public final static String DIC_ROOT_CUS_STOPWORD = "cus_sopword";
//	
//	/**
//	 * <p>Description: 扩展词典</p>
//	 * <p>Author:jlchen/陈金梁</p>
//	 * @Fields DIC_ROOT_EXTEND_WORDS 
//	 */
//	public final static String DIC_ROOT_EXTEND_WORDS = "extend_words";
	
	/** 
	 * <p>Copyright: All Rights Reserved</p>  
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
	 * <p>Description:  词典库类型</p> 
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum DIC_WORDS{
		sys_stopword,
		cus_stopword,
		extend_words;
	}
	/**
	 * <p>Description: 核心报道</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_CORE_REPORT 
	 */
	public final static Integer DIC_CORE_REPORT = 2453;
	
	/**
	 * <p>Description: 原创报道</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_ORIGINAL_REPORT 
	 */
	public final static Integer DIC_ORIGINAL_REPORT = 2457;
	/**
	 * <p>Description:语义分析类型-关键词 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_KEYWORDS 
	 */
	public final static String ANALYSIS_TYPE_KEYWORDS = "keywords";
	
	/**
	 * <p>Description:语义分析类型-实体 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_ENTITY 
	 */
	public final static String ANALYSIS_TYPE_ENTITY = "entity";
	
	/**
	 * <p>Description: 语义分析类型-情感分析</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_SENTIMENT 
	 */
	public final static String ANALYSIS_TYPE_SENTIMENT = "sentiment";

	/**
	 * <p>Description: 语义分析类型-分词</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_TAG 
	 */
	public final static String ANALYSIS_TYPE_TAG = "tag";
	
	/**
	 * <p>Description: 语义分析类型-摘要</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_SUMMARY 
	 */
	public final static String ANALYSIS_TYPE_SUMMARY = "summary";
	
	/**
	 * <p>Description:语义分析类型-聚类 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields BOSON_ANALYSIS_TYPE_CLUSTER  
	 */
	public final static String ANALYSIS_TYPE_CLUSTER = "cluster";
	
	/**
	 * <p>Description: 事件新闻所有分类统计</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields ANALYSIS_EVENT_REPORT_STAT
	 */
	public final static String ANALYSIS_EVENT_REPORT_STAT = "event_report_count";
	
	/**
	 * <p>Description:事件阶段性新闻统计 </p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields ANALYSIS_EVENT_BLAST_POINTS 
	 */
	public final static String ANALYSIS_EVENT_BLAST_POINTS= "blastPoints";
	
	/**
	 * <p>Description:事件阶段性新闻统计 </p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields ANALYSIS_EVENT_KEY_ENVENT 
	 */
	public final static String ANALYSIS_EVENT_KEY_ENVENT= "event_key_event";
	
	/**
	 * <p>Description: 事件时间轴</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields EVENT_TIME_AXIS 
	 */
	public final static String EVENT_TIME_AXIS = "event_time_axis";
	
	/**
	 * <p>Description: 五日报道量</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields EVENT_DAYS_COUNT 
	 */
	public final static String DAYS_COUNT = "days_count";
	/**
	 * <p>Description: 语义分析类型-特征提取</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_FEATURE 
	 */
	public final static String ANALYSIS_TYPE_FEATURE = "feature";
	
	/**
	 * <p>Description:语义分析类型-词云 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_WORD_CLOUD 
	 */
	public final static String ANALYSIS_TYPE_WORD_CLOUD = "word_cloud";
	
	/**
	 * <p>Description:语义分析类型-五日评论量 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_DAYS_STAT 
	 */
	public final static String ANALYSIS_TYPE_DAYS_STAT = "days_stat";
	
	/**
	 * <p>Description:语义分析类型-媒体提及率 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_MEDIA_STAT 
	 */
	public final static String ANALYSIS_TYPE_MEDIA_STAT = "media_stat";
	
	/**
	 * <p>Description:语义分析类型-地域分布 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPE_REGION_STAT 
	 */
	public final static String ANALYSIS_TYPE_REGION_STAT = "region_stat";
	
	/**
	 * <p>Description:语义分析类型-典型意见 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields ANALYSIS_TYPICAL_OPTION 
	 */
	public final static String ANALYSIS_TYPICAL_OPTION = "typical_option";
	
	/**
	 * <p>Description: 语义分析类型-相似新闻</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_SIMILARNEWS 
	 */
	public final static String ANALYSIS_TYPE_SIMILARNEWS = "sameArticles";
	
	/**
	 * <p>Description:语义分析类型-关键词组 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_KEYPHRASES
	 */
	public final static String ANALYSIS_TYPE_KEYPHRASES = "keyPhrases";
	
	/**
	 * <p>Description:语义分析类型-关键词组和关键词的合体 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_KEYEXPRESSIONS 
	 */
	public final static String ANALYSIS_TYPE_KEYEXPRESSIONS = "keyExpressions";

	/**
	 * <p>Description:语义分析类型-相关新闻 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_RELEVANTNEWS
	 */
	public final static String ANALYSIS_TYPE_RELEVANTNEWS = "simArticles";
	
	/**
	 * <p>Description:语义分析类型-实体关系 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_ENTITYRELATION 
	 */
	public final static String ANALYSIS_TYPE_ENTITYRELATION = "entityRelation";
	
	/**
	 * <p>Description:语义分析类型-权重 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_TYPE_WEIGHT
	 */
	public final static String ANALYSIS_TYPE_WEIGHT = "weight";
	
	
	/**
	 * <p>Description: 总数统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_TOTAL 
	 */
	public final static String STAT_TOTAL = "stat_total";
	
	/**
	 * <p>Description: 今日更新统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_TODAY 
	 */
	public final static String STAT_TODAY = "stat_today";
	
	/**
	 * <p>Description: 按时间统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_TIME 
	 */
	public final static String STAT_TIME = "stat_time";
	
	/**
	 * <p>Description: 按来源统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_SOURCE 
	 */
	public final static String STAT_SOURCE = "stat_source";
	
	/**
	 * <p>Description: 按分类统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_CLASSIFICATION 
	 */
	public final static String STAT_CLASSIFICATION = "stat_classification";
	
	/**
	 * <p>Description: 按是否原创统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_ORIGINAL 
	 */
	public final static String STAT_ORIGINAL = "stat_original";

	/**
	 * <p>Description: 按是否原创统计</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields STAT_ORIGINAL
	 */
	public final static String STAT_CARRIER = "stat_carrier";
	/**
	 * <p>Description:语料类型为新闻 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields CORPUS_TYPE_NEWS 
	 */
	public final static String CORPUS_TYPE_NEWS = "news";
	
	/**
	 * <p>Description: 语料类型为评论</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields CORPUS_TYPE_COMMENT 
	 */
	public final static String CORPUS_TYPE_COMMENT = "comment";
	
	/**
	 * <p>Description:语料类型为事件 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields CORPUS_TYPE_EVENT 
	 */
	public final static String CORPUS_TYPE_EVENT = "event";
	
	/**
	 * <p>Description:评论地区 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields COMMENT_AREA 
	 */
	public final static String COMMENT_AREA = "commentArea";
	
	/**
	 * <p>Description:对象化结果需要返回的关键词个数 (存储)</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields KEYWORDS_COUNT 
	 */
	public final static int KEYWORDS_COUNT = 15;
	
	/**
	 * <p>Description:对象化结果需要返回的关键词个数 (界面展示)</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields KEYWORDS_COUNT_PRE 
	 */
	public final static int KEYWORDS_COUNT_PRE = 5;
	
	/**
	 * <p>Description: job.task目录</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields JOB_PACKAGE 
	 */
	public static final String JOB_PACKAGE = "com.uec.inews.task.job.";
	
//	//job任务名称
	/**
	 * <p>Description: 新闻-玻森关键词分析</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_WEBPAGE_KEYWORDS_BOSON 
	 */
	public static final String TASK_WEBPAGE_KEYWORDS_BOSON = "webpage_boson_keywords";
	
	/**
	 * <p>Description: 新闻-玻森实体分析</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_WEBPAGE_NER_BOSON 
	 */
	public static final String TASK_WEBPAGE_NER_BOSON = "webpage_boson_ner";

	/**
	 * <p>Description: 新闻-玻森情感分析</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_WEBPAGE_SENTIMENT_BOSON 
	 */
	public static final String TASK_WEBPAGE_SENTIMENT_BOSON = "webpage_boson_sentiment";
	
	/**
	 * <p>Description:评论-玻森实体分析</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TASK_COMMENT_ENTITY_BOSON 
	 */
	public static final String TASK_COMMENT_ENTITY_BOSON = "comment_boson_entity";
	
	/**
	 * <p>Description:评论地域统计分析 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TASK_COMMENT_AREA_COUNT 
	 */
	public static final String TASK_COMMENT_AREA_COUNT = "comment_area_count";
	
	/**
	 * <p>Description:事件-玻森实体分析 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TASK_EVENT_ENTITY_BOSON 
	 */
	public static final String TASK_EVENT_ENTITY_BOSON = "event_boson_entity";
	
	/**
	 * <p>Description:事件-玻森情感分析 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TASK_EVENT_SENTIMENT_BOSON 
	 */
	public static final String TASK_EVENT_SENTIMENT_BOSON = "event_boson_sentiment";
	
	/**
	 * <p>Description: 事件新闻更新</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_EVENT_NEWS_UPDATE 
	 */
	public static final String TASK_EVENT_NEWS_UPDATE = "event_news_update";
	
	/**
	 * <p>Description:评论-玻森情感分析 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TASK_COMMENT_SENTIMENT_BOSON 
	 */
	public static final String TASK_COMMENT_SENTIMENT_BOSON = "comment_boson_sentiment";
	
	/**
	 * <p>Description: 新闻-es插入</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_WEBPAGE_INSERT 
	 */
	public static final String TASK_WEBPAGE_INSERT = "webpage_insert";

	/**
	 * <p>Description: 新闻-es更新</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_WEBPAGE_UPDATE 
	 */
	public static final String TASK_WEBPAGE_UPDATE = "webpage_update";
	
	/**
	 * <p>Description: 新闻记录状态-删除</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_STATUS_DELETE 
	 */
	public static final String TASK_STATUS_DELETE = "news_status_delete";
	
	/**
	 * <p>Description: 新闻标签</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_NEWS_LABEL 
	 */
	public static final String TASK_NEWS_LABEL = "news_label";
	
	
	/**
	 * <p>Description: es热点新增索引</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_HOT_INSERT 
	 */
	public static final String TASK_HOT_INSERT = "hot_insert";
	
	/**
	 * <p>Description: es热点删除索引</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_HOT_DELETED 
	 */
	public static final String TASK_HOT_DELETED = "hot_deleted";
	
	/**
	 * <p>Description: 事件基本信息索引</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_EVENT_BASIC_INDEX 
	 */
	public static final String TASK_EVENT_BASIC_INDEX = "event_basic_index";
	
	/**
	 * <p>Description: 事件基本信息索引删除</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_EVENT_BASIC_DELETED 
	 */
	public static final String TASK_EVENT_BASIC_DELETED = "event_basic_deleted";
	
	/**
	 * <p>Description: 事件新闻索引</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_EVENT_NEWS_INDEX 
	 */
	public static final String TASK_EVENT_NEWS_INDEX = "event_news_index";
	
	/**
	 * <p>Description: 事件新闻索引删除</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_EVENT_NEWS_DELETED 
	 */
	public static final String TASK_EVENT_NEWS_DELETED = "event_news_deleted";
	
	/**
	 * <p>Description: es新闻聚类新增索引</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_CLUSTER_INSERT 
	 */
	public static final String TASK_CLUSTER_INSERT = "cluster_insert";
	/**
	 * <p>Description: 最新新闻的表名</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_WEBPAGE 
	 */
	public static final String TABLE_WEBPAGE = "news_webpage";
	
	/**
	 * <p>Description:热点新闻评论表名 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields TABLE_COMMENT 
	 */
	public static final String TABLE_COMMENT = "news_comment";
	
	/**
	 * <p>Description:job任务每次从库里查找的评论条数 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_COMMENT_COUNT 
	 */
	public static final int ANALYSIS_COMMENT_COUNT = 20000;
	
	/**
	 * <p>Description:job任务每次从库里查找的事件新闻条数 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields ANALYSIS_EVENT_NEWS_COUNT 
	 */
	public static final int ANALYSIS_EVENT_NEWS_COUNT = 200;
	
	/**
	 * <p>Description: 典型意见单次分析个数</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields COMMENT_NUM 
	 */
//	public final static Integer ANALYSIS_OPTION_COUNT = 1000;
	
	/**
	 * <p>Description: 默认webpage_code,用于状态记录</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields STATUS_WEBPAGE_CODE 
	 */
	public static final String DEFAULT_WEBPAGE_CODE = "0";
	
	/**
	 * <p>Description: 默认系统用户id</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DEFAULT_USER_ID 
	 */
	public static final Integer DEFAULT_USER_ID = 0;
	/**
	 * <p>Description: 记录状态表名</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_ES_STATUS 
	 */
	public static final String TABLE_ES_STATUS = "news_status";
	
	/**
	 * <p>Description: 新闻标签表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_NEWS_LABEL 
	 */
	public static final String TABLE_NEWS_LABEL = "news_label";
	
	/**
	 * <p>Description: 新闻分析结果表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_NEWS_AYALYSIS 
	 */
	public static final String TABLE_NEWS_AYALYSIS = "news_analysis";
	
	/**
	 * <p>Description: 热点新闻表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_NEWS_HOT 
	 */
	public static final String TABLE_NEWS_HOT = "news_hot";
	
	/**
	 * <p>Description: 聚类任务表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_CLUSER_TASK 
	 */
	public static final String TABLE_CLUSER_TASK = "cluster_task";
	
	/**
	 * <p>Description:聚类结果页，媒体提及率中媒体最大数 </p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields MEDIA_REPORT_NUM 
	 */
	public static final Integer CLUSTER_MEDIA_REPORT_NUM = 15;
	
	/**
	 * <p>Description: 聚类结果表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_CLUSER_RESULT 
	 */
	public static final String TABLE_CLUSER_RESULT = "cluster_result";
	/**
	 * <p>Description: 事件基本新闻表</p>
	 * <p>Author:jlchen/陈金梁</p> 
	 * @Fields TABLE_EVENT_BASIC 
	 */
	public static final String TABLE_EVENT_BASIC = "event_basic";
	
	/**
	 * <p>Description: 事件新闻表</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TABLE_EVENT_NEWS 
	 */
	public static final String TABLE_EVENT_NEWS = "event_news";
	
	/**
	 * <p>Description: 优先级</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_PRIORITY 
	 */
	public static final Integer TASK_DEFAULT_PRIORITY = 5;
	
	/**
	 * <p>Description: 热点新闻评论词云统计最小评论量</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_WORD_CLOUD_MIN_VALUE 
	 */
	public final static int HOT_WORD_CLOUD_MIN_VALUE = 30;
	
	/**
	 * <p>Description:热点新闻评论词云最大统计个数 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_WORD_CLOUD_MAX_NUM 
	 */
	public final static int HOT_WORD_CLOUD_MAX_NUM = 300;;
	
	/**
	 * <p>Description:热点新闻评论和最新新闻取前TOPN个统计最多的实体 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields HOT_ENTITY_TOPN 
	 */
	public final static int ENTITY_TOPN = 5;
	
	
	/** 
	 * <p>Copyright: All Rights Reserved</p>  
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
	 * <p>Description: 新闻标签值enum </p> 
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum LABEL_ENUM{
		
		/**
		 * <p>Description:图片 </p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields image 
		 */
		image(87,"图片"), 
		/**
		 * <p>Description: 专题</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields special 
		 */
		special(97,"专题"),
		/**
		 * <p>Description: 事件</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields event 
		 */
		event(88,"事件"), //事件
		
		/**
		 * <p>Description: 原创</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields original 
		 */
		original(98,"原创"),//原创
		/**
		 * <p>Description: 转载</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields reprinted 
		 */
		reprinted(99,"转载"),//转载
		/**
		 * <p>Description: 头条</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields topnews 
		 */
		topnews(93,"头条"),//头条
		/**
		 * <p>Description: 热点</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields hotnews 
		 */
		hotnews(94,"热点"),//热点
		/**
		 * <p>Description: 要闻</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields corenews 
		 */
		corenews(95,"要闻"),//要闻
		/**
		 * <p>Description: 组图</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields imageGroup 
		 */
		imageGroup(100, "组图"),//组图
		/**
		 * <p>Description: 视频</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields video 
		 */
		video(92,"视频"), //视频
		/**
		 * <p>Description: 线索</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields clue 
		 */
		clue(91, "线索"), //线索
		/**
		 * <p>Description: 置顶</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields stick 
		 */
		stick(90, "置顶"), //置顶
		/**
		 * <p>Description: 不可见</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields hidden 
		 */
		hidden(89, "不可见"),//不可见，隐藏
		/**
		 * <p>Description: 头条网站</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields topsite 
		 */
		topsite(110,"头条网站"),//头条网站
		/**
		 * <p>Description: 热点网站</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields hotsite 
		 */
		hotsite(111,"热点网站"),//热点网站
		/**
		 * <p>Description: 历史事件</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields historyEvent 
		 */
		historyEvent(96,"历史事件"),
		/**
		 * <p>Description: 历史</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields history 
		 */
		history(2314,"历史"),//历史
		/**
		 * <p>Description: 出生</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields historyBirth 
		 */
		historyBirth(2441,"出生"),//出生
		/**
		 * <p>Description: 逝世</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields historyDeath 
		 */
		historyDeath(2442,"逝世"),//逝世
		/**
		 * <p>Description: 节假日</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields historyHoliday 
		 */
		historyHoliday(2443,"节假日"),//节假日
		/**
		 * <p>Description: 纪念日</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields historyMemorial 
		 */
		historyMemorial(2444,"纪念日"),//纪念日
		;
		private final String name;
		
		private final Integer id;
		private LABEL_ENUM(final Integer id,final String name ){
			this.name = name;
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public Integer getId() {
			return id;
		}
	};

	/**
	 * <p>Copyright: All Rights Reserved</p>
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
	 * <p>Description: 新闻载体类型标签值enum </p>
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum CARRIER_ENUM{

		/**
		 * <p>Description:网站 </p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields website
		 */
		website(6,"网站"),
		/**
		 * <p>Description: 微博</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields
		 */
		weibo(7,"微博"),
		/**
		 * <p>Description: 微信</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields weixin
		 */
		weixin(8,"微信"),

		/**
		 * <p>Description: APP</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields APP
		 */
		app(9,"APP"),
		;
		private final String name;

		private final Integer id;
		private CARRIER_ENUM(final Integer id,final String name ){
			this.name = name;
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public Integer getId() {
			return id;
		}
	}

	/**
	 * <p>Copyright: All Rights Reserved</p>
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
	 * <p>Description: 新闻网站标签值enum </p>
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum WEBSITE_ENUM{

		/**
		 * <p>Description: 微博</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields
		 */
		weibo(44,"微博"),
		/**
		 * <p>Description: 微信</p>
		 * <p>Author:jlchen/陈金梁</p>
		 * @Fields weixin
		 */
		weixin(2616,"微信");
		private final String name;
		private final Integer id;
		private WEBSITE_ENUM(final Integer id,final String name ){
			this.name = name;
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public Integer getId() {
			return id;
		}
	}
	/**
	 * <p>Description: job单次分析的数据量</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_DO_NUM 
	 */
	public final static int TASK_DO_NUM = 200;
	
	/**
	 * <p>Description: 失败重做次数</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields TASK_FAILURE_NUM 
	 */
	public final static int TASK_FAILURE_NUM = 2;
	
	/**
	 * <p>Description: 关键词分析状态码</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields STATUS_KEYWORDS 
	 */
	public final static int STATUS_KEYWORDS = 3;
	
	/**
	 * <p>Description: 实体分析状态码</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields STATUS_NER 
	 */
	public final static int STATUS_ENTITY = 4;
	
	/**
	 * <p>Description: 情感分析状态码</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields STATUS_SENTIMENT 
	 */
	public final static int STATUS_SENTIMENT = 5;
	
	/**
	 * <p>Description: 典型意见分析状态码</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields STATUS_OPTION 
	 */
	public final static int STATUS_OPTION = 6;
	
	/**
	 * <p>Description:热点评论统计状态码 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields STATUS_COMMENT_AREA 
	 */
	public final static int STATUS_COMMENT_AREA = 7;
	
	/**
	 * <p>Description:实体对象存入扩展词库记录状态码</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields STATUS_COMMENT_AREA
	 */
	public final static int STATUS_EXPAND_OPTION = 8;
	/**
	 * <p>Description:相关内容新闻展示条数 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields RELEVANT_NEWS_NUM 
	 */
	public final static int RELEVANT_NEWS_NUM = 10;
	
	///**
	// * <p>Description:1小时排行周期 </p>
	// * <p>Author:jlchen/陈金梁</p>
	// * @Fields DIC_CYCLE_1H
	// */
	//public final static int DIC_CYCLE_1H = 6;
	//
	///**
	// * <p>Description: 4小时排行周期</p>
	// * <p>Author:jlchen/陈金梁</p>
	// * @Fields DIC_CYCLE_4H
	// */
	//public final static int DIC_CYCLE_4H = 7;
	//
	///**
	// * <p>Description: 12小时排行周期</p>
	// * <p>Author:jlchen/陈金梁</p>
	// * @Fields DIC_CYCLE_12H
	// */
	//public final static int DIC_CYCLE_12H = 8;
	//
	///**
	// * <p>Description: 24小时排行周期</p>
	// * <p>Author:jlchen/陈金梁</p>
	// * @Fields DIC_CYCLE_24H
	// */
	//public final static int DIC_CYCLE_24H = 9;
	
	/**
	* <p>Description:</p>
	* <p>Author:xkwang/王西坤</p>
	* @Title
	* @params  * @param null
	* @return
	*/
	public static enum DIC_CYCLE_HOT{
		hour_1(69),//1小时
		hour_4(70), //4小时
		hour_12(71), //12小时
		hour_24(72), //24小时
		hour_48(73), //48小时
		week_1(74), //一周
		;
		private final Integer id;
		private DIC_CYCLE_HOT(final Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
	}
/**
* <p>Description:</p>
* <p>Author:xkwang/王西坤</p>
* @Title
* @params  * @param null
* @return 
*/
	public static enum DIC_CYCLE_CLUSTER{
		hour_1(6),//1小时
		hour_4(7), //4小时
		hour_12(8), //12小时
		hour_24(9), //24小时
		;
		private final Integer id;
		private DIC_CYCLE_CLUSTER(final Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
	}
	/**
	 * <p>Description: 新浪微博</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_WEIBO 
	 */
	public final static int DIC_WEIBO = 44;
	
	/**
	 * <p>Description: 一周排行周期</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields DIC_CYCLE_1W 
	 */
	public final static int DIC_CYCLE_1W = 74;
	
	/**
	 * <p>Description:首页新闻线索展示条数</p>
	 * <p>Author:sq/孙强</p>
	 * @Fields PORTAL_NEWS_CLUE_NUM 
	 */
	public final static int PORTAL_NEWS_CLUE_NUM = 22;
	
	/**
	 * <p>Description:首页排行的展示条数 </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields PORTAL_RANKING_NUM 
	 */
	public final static int PORTAL_RANKING_NUM = 10;
	
	/**
	 * <p>Description:百度新闻原始url </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields BAIDU_NEWS_BASIC_URL 
	 */
	public final static String BAIDU_NEWS_BASIC_URL = "http://news.baidu.com/";
	
	/**
	 * <p>Description:百度新闻搜索url </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields BAIDU_NEWS_SEARCH_URL 
	 */
	public final static String BAIDU_NEWS_SEARCH_URL = "http://news.baidu.com/ns";
	
	/**
	 * <p>Description: </p>
	 * <p>Author:sq/孙强</p>
	 * @Fields WHOLENET_SEARCH_PRE_NUM 
	 */
	public final static int WHOLENET_SEARCH_PRE_NUM = 500;
	
	/**
	 * <p>Description: 扩展词最低词频</p>
	 * <p>Author:jlchen/陈金梁</p>
	 * @Fields WORDS_LATEST_COUNT 
	 */
	public final static int WORDS_LATEST_COUNT = 10;
	
	/**
	 * <p>Description:推荐类型-新闻 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields RECOMMEND_TYPE_NEWS 
	 */
	public final static int RECOMMEND_TYPE_NEWS = 0;
	
	/**
	 * <p>Description:推荐类型-线索 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields RECOMMEND_TYPE_CLUE 
	 */
	public final static int RECOMMEND_TYPE_CLUE = 1;
	
	/**
	 * <p>Description: 推荐类型-热点</p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields RECOMMEND_TYPE_HOT 
	 */
	public final static int RECOMMEND_TYPE_HOT = 2;
	
	/**
	 * <p>Description:推荐类型-图片 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields RECOMMEND_TYPE_IMAGE 
	 */
	public final static int RECOMMEND_TYPE_IMAGE = 3;

	/**
	 * <p>Description:推荐类型-视频 </p>
	 * <p>Author:xpguo/郭晓鹏</p>
	 * @Fields RECOMMEND_TYPE_VIDEO 
	 */
	public final static int RECOMMEND_TYPE_VIDEO = 4;
	
	/**
	 * <p>Description: 聚类关键词个数</p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields WORDS_LATEST_COUNT 
	 */
	public final static int WORDS_CLUSTER_KEYWORDS_COUNT = 5;

	/**
	 * <p>Description: 我的定制类型</p>
	 * <p>Author:xkwnag/王西坤</p>
	 * @Fields
	 */
	public final static int CUSTOM_GENERAL = 1;
	public final static int CUSTOM_THREAD = 2;
	public final static int CUSTOM_HOT = 3;
	public final static int CUSTOM_HEADER = 4;
	public final static int CUSTOM_PIC = 5;
	public final static int CUSTOM_VIDEO = 6;


	public static enum CUSTOM_ENUM{

		general,//通用
		topnews,//头条
		hotnews,//热点
		image,//组图
		video, //视频
		thread, //线索
	};

	public static EnumMap<CUSTOM_ENUM, Integer> CUSTOM_MAP = new EnumMap<>(CUSTOM_ENUM.class);

	static{//put方法
		CUSTOM_MAP.put(CUSTOM_ENUM.general, 1);
		CUSTOM_MAP.put(CUSTOM_ENUM.thread, 2);
		CUSTOM_MAP.put(CUSTOM_ENUM.hotnews, 3);
		CUSTOM_MAP.put(CUSTOM_ENUM.topnews, 4);
		CUSTOM_MAP.put(CUSTOM_ENUM.image, 5);
		CUSTOM_MAP.put(CUSTOM_ENUM.video, 6);
	}

	public static Map<String, Integer> Ranking_cycle = new HashMap<String,Integer>();
	static{//put方法
		Ranking_cycle.put("1小时", 69);
		Ranking_cycle.put("4小时", 70);
		Ranking_cycle.put("12小时", 71);
		Ranking_cycle.put("24小时", 72);
	}

	public static Map<String, Integer> Ranking_type = new HashMap<String,Integer>();
	static{//put方法
		Ranking_type.put("点击榜", 75);
		Ranking_type.put("跟帖榜", 76);
		Ranking_type.put("综合榜", 77);
	}

	/** 
	 * <p>Copyright: All Rights Reserved</p>  
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
	 * <p>Description: 日志模块名 </p> 
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum LOG_MODULE{
		LOGIN("login"), // 登录页
		PORTAL("portal"), //首页
		WEBPAGE("webpage"),//新闻库模块
		HOTNEWS("hotNews"),//热点新闻
		IMAGE("image"), //图片库
		VIDEO("video"), //视频
		CLUE("clue"),//新闻线索
		CLUSTER("cluster"), //热点发现
		EVENT("event"),//事件专题
		CUSTOM("custom"),//个性化定制
		TOP("top"),//头条
		STICK("stick"),//置顶
		CALENDAR("calendar"), //日历
		LOG("log"), //日志模块
		;
		private final String module;

		private LOG_MODULE(final String module) {
			this.module = module;
		}
		public String getValue() {
	        return module;
	    }
	}
	
	
	
	/** 
	 * <p>Copyright: All Rights Reserved</p>  
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
	 * <p>Description: 日志记录中操作类型 </p> 
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum LOG_OP_TYPE{
		logout(0,"logout"),//0登出
		login(1,"login"),//1登录
		page_view(2,"page_view"), //2访问页面
		filter(3, "filter"), //3筛选
		search(4, "search"), //4搜索
		custom(5, "custom"), //5定制
		draft(6, "draft"), //6建稿
		store(7, "store"), //7收藏
		;
		private final Integer id;
		private final String value;
		private LOG_OP_TYPE(final Integer id,final String value) {
			this.id = id;
			this.value = value;
		}
		public Integer getId() {
			return id;
		}
		public String getValue() {
			return value;
		}
		
	}
	
	/** 
	 * <p>Copyright: All Rights Reserved</p>  
	 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p> 
	 * <p>Description: 媒体状态 </p> 
	 * <p>Author:jlchen/陈金梁</p>
	 */
	public static enum MEDIA_STATUS{
		news(0),//0普通新闻
		vedio(1), //1视频
		hasImg(2), //2含图片
		image(3), //3含500x500图片
		imageGroup(4), //4 组图，降序
		embedVedio(5), //5内嵌视频
		singleVedio(6),//6独立视频
		;
		private final Integer id;
		private MEDIA_STATUS(final Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return id;
		}
	}

	/**
	* <p>Description:字典表sysDicRepository 排行周期</p>
	* <p>Author:xkwang/王西坤</p>
	* @Title
	* @params  * @param null
	* @return
	*/
	public final static int SYSDIC_PARENTID_RANKING_CYCLE = 3;

	/**
	 * <p>Description:我的定制来源、分类、地区选择全部</p>
	 * <p>Author:xkwang/王西坤</p>
	 * @Title
	 * @params  * @param null
	 * @return
	 */
	public final static String USERCUSTOMALL = "all";
	
	
	/**
	 * <p>Description: </p>
	 * <p>Author:bgchen/陈本庚</p>
	 * @Fields ANALYSIS_EVENT_ENTITY_RELATION 
	 */
	public static final String ANALYSIS_EVENT_ENTITY_RELATION = "entityRelation";



	/**
	* <p>Description:IRobot新建任务采集类型</p>
	* <p>Author:xkwang/王西坤</p>
	* @return
	*/
	public static Map<Integer, String> crawlType_Map = new HashMap<Integer,String>();
	static{//put方法
		crawlType_Map.put(0, "普通新闻");
		crawlType_Map.put(1, "头条");
		crawlType_Map.put(2, "热点排行");
		crawlType_Map.put(3, "组图");
	}

	/**
	* <p>Description:IRobot新建任务解析类型</p>
	* <p>Author:xkwang/王西坤</p>
	* @return
	*/
	public static Map<Integer, String> parserType_Map = new HashMap<Integer,String>();
	static{//put方法
		parserType_Map.put(1, "bs4");
		parserType_Map.put(2, "regular");
		parserType_Map.put(3, "browser");
		parserType_Map.put(4, "xpath");
		parserType_Map.put(5, "xml");
		parserType_Map.put(6, "json");
	}

	/**
	* <p>Description:模板获取所属字段分类</p>
	* <p>Author:xkwang/王西坤</p>
	* @return
	*/
	public static List< String> IROBOT_FIELDS_MAP = new ArrayList<>();
	static{//put方法
		IROBOT_FIELDS_MAP.add("webpage_url");
		IROBOT_FIELDS_MAP.add("title");
		IROBOT_FIELDS_MAP.add("short_title");
		IROBOT_FIELDS_MAP.add("keywords");
		IROBOT_FIELDS_MAP.add("web_summary");
		IROBOT_FIELDS_MAP.add("content");
		IROBOT_FIELDS_MAP.add("web_site");
		IROBOT_FIELDS_MAP.add("no_tag_content");
		IROBOT_FIELDS_MAP.add("webpage_code");
		IROBOT_FIELDS_MAP.add("classification");
		IROBOT_FIELDS_MAP.add("source_report");
		IROBOT_FIELDS_MAP.add("release_datetime");
		IROBOT_FIELDS_MAP.add("source_crawl");
		IROBOT_FIELDS_MAP.add("crawl_datetime");
		IROBOT_FIELDS_MAP.add("region");
		IROBOT_FIELDS_MAP.add("reposts_num");
		IROBOT_FIELDS_MAP.add("comments_num");
		IROBOT_FIELDS_MAP.add("clicking_num");
		IROBOT_FIELDS_MAP.add("participate_num");
		IROBOT_FIELDS_MAP.add("vote");
		IROBOT_FIELDS_MAP.add("against");
		IROBOT_FIELDS_MAP.add("browse_num");
		IROBOT_FIELDS_MAP.add("author");
		IROBOT_FIELDS_MAP.add("editor");
		IROBOT_FIELDS_MAP.add("video_url_list");
		IROBOT_FIELDS_MAP.add("original_id");
		IROBOT_FIELDS_MAP.add("original_parent_id");
		IROBOT_FIELDS_MAP.add("original_relation_id");
		IROBOT_FIELDS_MAP.add("is_deleted");
		IROBOT_FIELDS_MAP.add("image_status");
		IROBOT_FIELDS_MAP.add("video_status");
		IROBOT_FIELDS_MAP.add("pic_path");
		IROBOT_FIELDS_MAP.add("retweeted");
	}

	/**
	* <p>Description:模板获取日期规则</p>
	* <p>Author:xkwang/王西坤</p>
	* @return
	*/
	public static List< String> IROBOT_DATEFORMAT_MAP = new ArrayList<>();
	static{//put方法
		IROBOT_DATEFORMAT_MAP.add("yyyy-MM-dd HH:mm:ss");
		IROBOT_DATEFORMAT_MAP.add("yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * <p>Description:iRobot服务器</p>
	 * <p>Author:xkwang/王西坤</p>
	 */
	//public static final String IROBOT_SERVER = "http://10.6.0.20:5000";

	/**
	 * <p>Description:iRobot服务器</p>
	 * <p>Author:xkwang/王西坤</p>
	 */
	public static final String DIC_SYS_DATA_INDUSTRY = "industry";

	/**
	 * <br/>Description:scoll检索默认保留时间，单位ms，默认60s，
	 * <p>Author:jlchen/陈金梁</p>
	 * @return
	 */
	public static int SCOLL_DEFAULT_TIME = 600000;

	/**
	 * <br/>Description:置顶推送文章类型ID
	 * <p>Author:ftliu/刘芳彤</p>
	 * @return
	 */
	public static Integer STICK_NEWSTYPE_ID = null;

	/**
	 * <br/>Description:线索新闻top500排行榜对应的redis的key值
	 * <p>Author:bgchen/陈本庚</p>
	 * @return
	 */
	public static String REDIS_HOTNEWS_RANK = "inews.hotnews.ranklist";


	/**
	 * <br/>Description:线索新闻top500排行榜对应的redis的key值
	 * <p>Author:bgchen/陈本庚</p>
	 * @return
	 */
	public static String REDIS_HOTNEWS_RANK_TIME = "inews.hotnews.ranklist.time";

	/**
	 * <br/>Description:redis默认过期时间，2小时，2*60*60=7200s，
	 * <p>Author:jlchen/陈金梁</p>
	 * @return
	 */
	public static  Integer DEFAULT_REDIS_EXPIRE_TIME = 7200;

	/**
	 * <br/>Description:默认租户
	 * <p>Author:jlchen/陈金梁</p>
	 * @return
	 */
	public static  String DEFAULT_TENANT_MARK = "uec";

	/**
	* <p>Description:inews项目地址</p>
	* <p>Author:xkwang/王西坤</p>
	*/
	public static  String INEWSENGINE_URL =  "http://inewsengine.com/uec/wechat/gotoappnewsdetail/";

	/**
	* <p>Description:微信默认图片地址</p>
	* <p>Author:xkwang/王西坤</p>
	* @Title
	* @params  * @param null
	* @return
	*/
	public static  String WECHAT_IMAGE_URL =  "/static/data/wechat/rongzhilian.jpg";
	public static  String WECHAT_ROOT_PATH =  "/static/data/wechat";

	/**
	 * <p>Description:</p>
	 * <p>Author:ftliu/刘芳彤</p>
	 */
	public static Integer CUSTUM_CRAWLTYPE_WECHAT = 3;
	public static Integer CUSTUM_CRAWLTYPE_MICROBLOG = 2;
	public static Integer CUSTUM_NUM_MAX = 15;

	public static String PORTAL_HOTWEBSITES  = "19,16,2448,23,2446,2447,44";

}
