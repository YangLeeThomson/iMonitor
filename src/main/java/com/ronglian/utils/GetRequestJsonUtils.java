/**
 * 
 */
package com.ronglian.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * @author liyang
 * @createTime 2018年1月24日
 */
public class GetRequestJsonUtils {
	/*** 
     * 获取 request 中 json 字符串的内容 
     *  
     * @param request 
     * @return : <code>byte[]</code> 
     * @throws IOException 
     */  
    public static String getRequestJsonString(HttpServletRequest request)  
            throws IOException {  
        String submitMehtod = request.getMethod();  
        // GET  
        if (submitMehtod.equals("GET")) {  
            return new String(request.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");  
        // POST  
        } else {  
            return getRequestPostStr(request);  
        }  
    }  
  
    /**    
     * 描述:获取 post 请求的 byte[] 数组 
     * <pre> 
     * 举例： 
     * </pre> 
     * @param request 
     * @return 
     * @throws IOException     
     */  
    public static byte[] getRequestPostBytes(HttpServletRequest request)  
            throws IOException {  
        int contentLength = request.getContentLength();  
        if(contentLength<0){  
            return null;  
        }  
        byte buffer[] = new byte[contentLength];  
        for (int i = 0; i < contentLength;) {  
  
            int readlen = request.getInputStream().read(buffer, i,  
                    contentLength - i);  
            if (readlen == -1) {  
                break;  
            }  
            i += readlen;  
        }  
        return buffer;  
    }  
  
    /**    
     * 描述:获取 post 请求内容 
     * <pre> 
     * 举例：
     * </pre> 
     * @param request 
     * @return 
     * @throws IOException     
     */  
    public static String getRequestPostStr(HttpServletRequest request)  
            throws IOException {  
        byte buffer[] = getRequestPostBytes(request);  
        String charEncoding = request.getCharacterEncoding();  
        if (charEncoding == null) {  
            charEncoding = "UTF-8";  
        }  
        return new String(buffer, charEncoding);  
    }  
    
    /*
     * 将JSONstring转换为Map集合
     * */
    public static synchronized Map<String, Object> parseObject(String str){
    	  Map<String, Object> map = new HashMap<String, Object>() ;  
          try {  
              if(! StringUtils.isBlank(str)){
              	// json��ʽ�ַ���jsonStringת��ΪJSONObject����
              	Map jsonObject = JSON.parseObject(str);
              	Set<String> keys = new HashSet<String>();
              	keys = jsonObject.keySet();
              	for(String key:keys){
              		map.put(key, jsonObject.get(key));
              	}
              }  
          } catch (Exception e) {  
              e.printStackTrace();  
          }  
          return map;  
    }
}
