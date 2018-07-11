package com.ronglian.mapper.provider;


public class FocalMediaSqlProvider{
	public String findFocalMediaByIds(String ids){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		/*if(ids!=null&&ids.size()>0){
			for(String id:ids){
				sb.append(id);
				sb.append(',');
			}
			sb.substring(0,sb.length()-1);
		}*/
		sb.append(ids);
		sb.append(")");
		return "select id,name,type_name from focal_media where id in "+sb.toString();
	}
}
