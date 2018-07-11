/**
 * 
 */
package com.ronglian.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

/**
 * ��дHttpServletRequestWrapper���� 
 * @author liyang
 * @createTime 2018��1��24��
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private byte[] requestBody = null;  
	  
    public MyHttpServletRequestWrapper (HttpServletRequest request) {  
  
        super(request);  
  
        //��������body  
        try {  
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * ��д getInputStream() 
     */  
    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        if(requestBody == null){  
            requestBody= new byte[0];  
        }  
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);  
        return new ServletInputStream(){  
            @Override  
            public int read() throws IOException {  
                return bais.read();  
            }

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub
				
			}  
        };  
    }  
  
    /** 
     * ��д getReader() 
     */  
    @Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  
}
