package com.ronglian.common;

import java.util.List;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月12日 上午10:28:46
* @description:分页对象
*/
public class PageResult<T> {
	
	/**
	 * <p>Description: 当前页码</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private int number;
	/**
	 * <p>Description: 总页数</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private int totalPages;
	/**
	 * <p>Description: 总记录数</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private long totalElements;
	/**
	 * <p>Description: 起始记录数</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private int elementsNo;
	/**
	 * <p>Description: 查询记录条数</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private int limit;
	/**
	 * <p>Description: 查询结果</p>
	 * <p>Author:黄硕/huangshuo</p>
	 * @Fields number 
	 */
	private List<T> content;
	
	public int getNumber(){
		return number;
	}

	public void setNumber(int number){
		this.number = number;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public long getTotalElements(){
		return totalElements;
	}

	public void setTotalElements(long totalElements){
		this.totalElements = totalElements;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public void setTotalElements(int totalElements){
		this.totalElements = totalElements;
	}

	public int getElementsNo(){
		return elementsNo;
	}

	public void setElementsNo(int elementsNo){
		this.elementsNo = elementsNo;
	}

	public int getLimit(){
		return limit;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}

	public List<T> getContent(){
		return content;
	}

	public void setContent(List<T> content){
		this.content = content;
	}
}
