package com.ronglian.utils;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

 /**
 * @author liyang
 * 
 * @createTime 2017-12-21
 * 
 *  人民日报自定义响应结构
 */
public class RongLianResult{
	
	private static final long serialVersionUID = 1L;
    // 定义jackson对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
    // 响应业务状态，200表示成功响应
    private Integer code;

    // 响应消息
    private String msg;

    public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	// 响应中的数据
    private Object data;

    public static RongLianResult build(Integer code, String msg, Object data) {
        return new RongLianResult(code, msg, data);
    }

    public static RongLianResult ok(Object data) {
        return new RongLianResult(data);
    }

    public static RongLianResult ok() {
        return new RongLianResult(null);
    }

    public RongLianResult() {

    }

    public static RongLianResult build(Integer code, String msg) {
        return new RongLianResult(code, msg, null);
    }

    public RongLianResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RongLianResult(Object data) {
        this.code = 0;
        this.msg = "OK";
        this.data = data;
    }

   
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 将json结果集转化为RongLianResult对象
     * 
     * @param jsonData json数据
     * @param clazz RongLianResult中的object类型
     * @return
     */
    public static RongLianResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, RongLianResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     * 
     * @param json
     * @return
     */
    public static RongLianResult format(String json) {
        try {
            return MAPPER.readValue(json, RongLianResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     * 
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static RongLianResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
