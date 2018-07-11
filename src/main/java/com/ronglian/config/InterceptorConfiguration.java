package com.ronglian.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ronglian.interceptor.SignInterceptor;
/** 
* @ClassName: InterceptorConfiguration 
* @Description: TODO
* @author: YeohLee
* @date: 2018年6月13日 下午4:15:32  
*/
@EnableWebMvc
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter{
	
	@Value("${sign.interceptor.enterance}")
    private String enterance;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if("on".equals(enterance)){
			registry.addInterceptor(new SignInterceptor()).addPathPatterns("/**");
		       super.addInterceptors(registry);
		}
	}
    @Bean
    public SignInterceptor signInterceptor(){
        return new SignInterceptor();
    }
}
