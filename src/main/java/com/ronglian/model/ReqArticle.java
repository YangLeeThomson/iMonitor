package com.ronglian.model;

import java.util.List;


import lombok.Data;

/**
 * @Description 苏州台POST原始文章数据接口，文章接收封装类型  
 * @author sunqian  
 * @date 2018年5月9日
 */
@Data
public class ReqArticle {
	private int count;
	private List<OriginalArticle> data;
	
	@Override
	public String toString() {
		return "ReqArticle [count=" + count + ", data=" + data + "]";
	}

}
