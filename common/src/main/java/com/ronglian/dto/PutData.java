package com.ronglian.dto;

import java.io.Serializable;

/**
 * @description: insert data
 * @author: tianjin
 * @email: eternity_bliss@sina.cn
 * @create: 2018-07-16 上午11:22
 */
public class PutData implements Serializable {
    private static final long serialVersionUID = 1217367952819635135L;
    private String type;        //not null
    private String index;       //not null
    private String id;
    private String source;      //not null

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
