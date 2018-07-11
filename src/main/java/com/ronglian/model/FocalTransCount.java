package com.ronglian.model;

public class FocalTransCount  implements Comparable<FocalTransCount>{
	private Integer id;
	private String mediaId;
	private String platformId;
	private String mediaName;
	private String originMediaName;
	private String platformName;
	private String originPlatformName;
	private Integer homePageTrans;
	private Integer nonHomeTrans;
	public Integer getId(){
		return id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public String getMediaId(){
		return mediaId;
	}
	public void setMediaId(String mediaId){
		this.mediaId = mediaId;
	}
	public String getPlatformId(){
		return platformId;
	}
	public void setPlatformId(String platformId){
		this.platformId = platformId;
	}
	public String getMediaName(){
		return mediaName;
	}
	public void setMediaName(String mediaName){
		this.mediaName = mediaName;
	}
	public String getPlatformName(){
		return platformName;
	}
	public void setPlatformName(String platformName){
		this.platformName = platformName;
	}
	public Integer getHomePageTrans(){
		return homePageTrans;
	}
	public void setHomePageTrans(Integer homePageTrans){
		this.homePageTrans = homePageTrans;
	}
	public Integer getNonHomeTrans(){
		return nonHomeTrans;
	}
	public void setNonHomeTrans(Integer nonHomeTrans){
		this.nonHomeTrans = nonHomeTrans;
	}
	public FocalTransCount(){
		super();
	}
	@Override
	public int compareTo(FocalTransCount o){
		int i=(o.getHomePageTrans()!=null?o.getHomePageTrans():0)-(this.getHomePageTrans()!=null?this.getHomePageTrans():0);
		return i;
	}
	public String getOriginMediaName(){
		return originMediaName;
	}
	public void setOriginMediaName(String originMediaName){
		this.originMediaName = originMediaName;
	}
	public String getOriginPlatformName(){
		return originPlatformName;
	}
	public void setOriginPlatformName(String originPlatformName){
		this.originPlatformName = originPlatformName;
	}
	public FocalTransCount(Integer id,String mediaId,String platformId,String mediaName,String originMediaName,String platformName,String originPlatformName,Integer homePageTrans,Integer nonHomeTrans){
		super();
		this.id = id;
		this.mediaId = mediaId;
		this.platformId = platformId;
		this.mediaName = mediaName;
		this.originMediaName = originMediaName;
		this.platformName = platformName;
		this.originPlatformName = originPlatformName;
		this.homePageTrans = homePageTrans;
		this.nonHomeTrans = nonHomeTrans;
	}
	
}