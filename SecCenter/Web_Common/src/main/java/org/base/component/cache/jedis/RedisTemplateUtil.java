package org.base.component.cache.jedis;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTemplateUtil<T> {

	Logger logger = Logger.getLogger(RedisTemplateUtil.class); 
	
	private RedisTemplate<String, T> redisTemplate;  
	
	public void setRedisTemplate(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void set(final String key, final String value) {  
        redisTemplate.execute(new RedisCallback<Object>() {  
            @Override  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                connection.set(  
                        redisTemplate.getStringSerializer().serialize(key),  
                        redisTemplate.getStringSerializer().serialize(value));  
                logger.info("save key:" + key + ",value:" + value);  
                return null;  
            }  
        });  
    }  
  
    public String get(final String key) {  
        return redisTemplate.execute(new RedisCallback<String>() {  
            @Override  
            public String doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] byteKye = redisTemplate.getStringSerializer().serialize(  
                        key);  
                if (connection.exists(byteKye)) {  
                    byte[] byteValue = connection.get(byteKye);  
                    String value = redisTemplate.getStringSerializer()  
                            .deserialize(byteValue);  
                    logger.debug("get key:" + key + ",value:" + value);  
                    return value;  
                }  
                logger.error("valus does not exist!,key:"+key);  
                return null;  
            }  
        });  
    }  
  
    public void delete(final String key) {  
        redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection) {  
                connection.del(redisTemplate.getStringSerializer().serialize(  
                        key));  
                return null;  
            }  
        });  
    }  
}
