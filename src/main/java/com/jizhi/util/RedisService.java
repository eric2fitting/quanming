package com.jizhi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis对缓存的操作
 */
/**
 * Redis的相关操作方法
 * 
 * @author Administrator
 *
 */
@Component
public class RedisService {
	//TODO redisTemplate.opsForValue()和redisTemplate操作的区别？？
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    
    /**
     * 读取缓存
     * @param Key
     * @return
     */
    public String get(String Key) {
        return redisTemplate.opsForValue().get(Key);
    }
    
    public Boolean hasKey(String key) {
    	return redisTemplate.hasKey(key);
    }
    /**
     * 写入缓存并设置有效时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key,String value,long time) {
    	boolean result = false;
        try {
                redisTemplate.opsForValue().set(key, value, time,TimeUnit.SECONDS);
                result = true;
        } catch (Exception e) {
                e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * 删除缓存
     * @param key
     * @return
     */
    public boolean delete(String key) {
        boolean result = false;
        try {
                redisTemplate.delete(key);
                result = true;
        } catch (Exception e) {
                e.printStackTrace();
        }
        return result;
    }
}
