package com.ronglian.config;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* @author: 黄硕/huangshuo
* @date:2018年5月3日 上午11:18:31
* @description:redis配置
*/
@Configuration
@EnableCaching //启用缓存
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private int port;
    
    @Value("${spring.redis.password}")
    private String password;
    
    @Value("${spring.redis.database}")
    private int database;
    
    @Value("${spring.redis.timeout}")
    private int timeout;
    
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWait;
    
    
    /**
     * 自定义key生成策略：
     * 类路径+方法名称+参数
     * */
	@Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator(){
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for(Object obj:params){
                    sb.append(obj.toString());
                }
                return sb.toString();
           }
        };
    }
	
	/**
	 * redis连接池连接
	 * */
	@Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setDatabase(database);
        factory.setTimeout(timeout);
        return factory;
    }
	
	/**
     * 配置CacheManager 管理cache
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate); 
        cacheManager.setDefaultExpiration(60*60); // 设置key-value超时时间
        return cacheManager;
    }
	
    /**
     * 存储前把对象序列化，采用jackson
     * */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
       RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
       template.setConnectionFactory(connectionFactory);

       //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
       Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

       ObjectMapper mapper = new ObjectMapper();
       mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
       mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
       serializer.setObjectMapper(mapper);

       template.setValueSerializer(serializer);
       //使用StringRedisSerializer来序列化和反序列化redis的key值
       template.setKeySerializer(new StringRedisSerializer());
       //设置存取hash类型时的序列化方式
       template.setHashKeySerializer(new StringRedisSerializer());
       template.setHashValueSerializer(serializer);
       
       template.afterPropertiesSet();
       return template;
    }
}
