package com.ronglian.common;

import java.util.HashMap;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月19日 上午11:47:38
* @description:描述
*/


public class Constants {
	
	/**
	 * 默认分页大小
	 * */
	public static int DEFAULT_PAGESIZE = 100;
	
	/**
	 * 监控天数，默认14天
	 * */
	public static int MONITOR_DAY_LENGTH = 14;
	
	public static String DEFAULT_DATE_FORMAT_YMD = "yyyy-MM-dd";
	public static String DEFAULT_DATE_FORMAT_YMD2 = "yyyy.MM.dd";
	public static String DATE_FORMAT_YMD = "yyyy.MM.dd";
	
	public static String DATE_FORMAT_MDY = "MM/dd/yyyy";
	public static String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	public static class ARTICLES_QUENES{
		public static String ORIGIN_ARTICLE_ERROR="origin-articles-error";
		public static String ORIGIN_ARTICLE_DISTINCE="origin-articles-distinct";
		public static String ORIGIN_ARTICLE_CLASSIFICATION="origin-articles-classification";
		public static String ORIGIN_ARTICLE_TRANS="origin-articles-gettrans";
		public static String ORIGIN_ARTICLE_TRANSERROR="origin-articles-gettrans-error";
		public static String ORIGIN_ARTICLE_FOCALMEDIA="origin-articles-focalmedia";
	}
	
	public static String ES_INDEX_IMONITOR_ARTICLE_PERFIX = "imonitor_article-";
	public static String ES_INDEX_IMONITOR_TRANS_INFO_PERFIX = "imonitor_trans_info-";
	
	public static HashMap<String,String> PLATFORM_NAME = new HashMap<String,String>() {
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("2","看苏州");
			put("1","无线苏州");
			put("4","微博");
			put("3","微信");
			put("0","名城苏州网");
		}
	};
	public static HashMap<String,String> PLATFORM_TYPE_NAME = new HashMap<String,String>() {
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			put("2","APP");
			put("1","APP");
			put("4","微博");
			put("3","微信");
			put("0","网站");
		}
	};
	
	public static HashMap<String,Integer> CLASSIFICATION_CODE = new HashMap<String,Integer>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("时政",1);
			put("经济",2);
			put("社会",3);
			put("文化",4);
			put("生活",5);
			put("体育",6);
			put("娱乐",7);
			put("军事",8);
			put("法制",9);
			put("教育",10);
			put("科技",11);
			put("房产",12);
			put("汽车",13);
			put("其他",14);
			put("宏观",2);
			put("金融",2);
			put("股指期货",2);
			put("能源",2);
			put("农业",2);
			put("环保",3);
			put("事故",3);
			put("公益",3);
			put("万象",3);
			put("人才就业",3);
			put("健康",5);
			put("情感",5);
			put("篮球",6);
			put("足球",6);
			put("明星",7);
			put("八卦",7);
			put("动漫",7);
			put("音乐",7);
			put("美女",7);
			put("国防",8);
			put("航空",8);
			put("武器",8);
			put("环球军情",8);
			put("高考",10);
			put("公考",10);
			put("考研",10);
			put("留学",10);
			put("家电数码",11);
			put("手机",11);
			put("智能",11);
		}
	};
	
	public static HashMap<String,String> CLASSIFICATION_NAME = new HashMap<String,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("shizheng","时政");
			put("jingji","经济");
			put("shehui","社会");
			put("wenhua","文化");
			put("shenghuo","生活");
			put("tiyu","体育");
			put("yule","娱乐");
			put("junshi","军事");
			put("fazhi","法制");
			put("jiaoyu","教育");
			put("keji","科技");
			put("fangchan","房产");
			put("qiche","汽车");
			put("other","其他");
		}
	};
	public static HashMap<Integer,String> CLASSIFICATION_CODE_NEW = new HashMap<Integer,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			put(1,"时政");
			put(2,"经济");
			put(3,"社会");
			put(4,"文化");
			put(5,"生活");
			put(6,"体育");
			put(7,"娱乐");
			put(8,"军事");
			put(9,"法制");
			put(10,"教育");
			put(11,"科技");
			put(12,"房产");
			put(13,"汽车");
			put(14,"其他");
			put(0,"其他");
		}
	};
	
	public static HashMap<Integer,String> CHANNEL_CODE = new HashMap<Integer,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(1,"网站");
			put(2,"微信");
			put(3,"微博");
			put(4,"APP");
			put(0,"其他");
		}
	};
	
	public static HashMap<String,String> CHANNEL_NAME = new HashMap<String,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("website","网站");
			put("weixin","微信");
			put("weibo","微博");
			put("app","APP");
			put("search","其他");
		}
	};
	
	public static HashMap<String,String> MEDIA_TYPE_NAME = new HashMap<String,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("official","官方媒体");
			put("gov","政府网站");
			put("foreigh","国外媒体");
			put("business","商业媒体");
			put("personal","自媒体");
			put("other","其他");
		}
	};
	public static HashMap<Integer,String> MEDIA_TYPE_CODE = new HashMap<Integer,String>(){
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			put(1,"官方媒体");
			put(2,"政府网站");
			put(3,"商业媒体");
			put(4,"自媒体");
			put(5,"国外媒体");
			put(0,"其他");
		}
	};
	
	public static HashMap<String,String> FOCAL_MEDIA = new HashMap<String,String>() {
		/**
		 * <p>Description: </p>
		 * <p>Author:Alan/黄硕</p>
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			put("新华网","http://www.xinhuanet.com/");
			put("人民网","http://www.people.com.cn/");
			put("央视新闻","http://news.cctv.com/");
			put("央广网","http://www.cnr.cn/");
			put("中国新闻网","http://www.chinanews.com/");
			put("中国青年网","http://www.youth.cn/");
			put("环球网","http://www.huanqiu.com/");
			put("凤凰网","http://www.ifeng.com/");
			put("网易新闻","http://news.163.com/");
			put("搜狐","http://www.sohu.com/");
			put("新浪","http://www.sina.com.cn/");
			put("腾讯新闻","http://news.qq.com/");
			put("澎湃","http://www.thepaper.cn/");
			put("中国江苏网","http://www.jschina.com.cn/");
			put("扬子晚报网","http://www.yangtse.com/");

			put("新华社","xinhuashefabu1");
			put("人民日报","rmrbwx");
			put("央视新闻","cctvnewscenter");
			put("新华视点","XHSXHSD");
			put("人民网","people_rmw");
			put("中国新闻网","cns2012");
			put("中国青年报","zqbcyol");
			put("澎湃新闻","thepapernews");
			put("江苏新闻","jstvjsxw");
			put("中国江苏网","jschina2013");
			put("扬子晚报","yzwb20102806");
			put("南方都市报","nddaily");
			put("楚天都市报","ctdsbgfwx");
			put("现代快报","xiandaikuaibao");
			put("都市快报","dskbdskb");
			put("钱江晚报","qianjiangwanbao");
			put("新闻晨报","shxwcb");
			put("新华每日电讯","caodi_zhoukan");
			put("法制晚报","fzwb_52165216");
			
			put("人民日报","https://weibo.com/rmrb?topnav=1&wvr=6&topsug=1");
			put("人民网","https://weibo.com/renminwang?refer_flag=1002060001_");
			put("中国新闻网","https://weibo.com/chinanewsv?refer_flag=1001030101_");
			put("中国日报","https://weibo.com/chinadailywebsite?refer_flag=1001030101_");
			put("环球时报","https://weibo.com/huanqiushibaoguanwei?refer_flag=1002060001_");
			put("扬子晚报","https://weibo.com/yangtse?refer_flag=1001030101_");
		}
	};
}
