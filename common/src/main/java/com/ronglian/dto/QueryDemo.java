package com.ronglian.dto;

import java.io.Serializable;

/**
 * @description: query demo
 * @author: tianjin
 * @email: eternity_bliss@sina.cn
 * @create: 2018-07-16 上午11:20
 */
public class QueryDemo implements Serializable {
    private static final long serialVersionUID = 1188822734371492438L;

    private String[] includes;
    private String filter;

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
