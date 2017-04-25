/**  
 * @Project: EC-BaseComponent
 * @Title: IKeysClient.java
 * @Package org.meibaobao.ecos.cache
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-19 上午11:27:44
 * @Copyright: Waker360 Software Services Co.,Ltd. All rights reserved.
 * @version V1.0  
 */
package org.base.component.cache.redisCom_back;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

/** 
 * @ClassName: IKeys 
 * @Description: 对redis Key 的操作
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-19 上午11:27:44
 *  
 */

public class Keys {
	private JedisPool jedisPool;
	/**
	 * 清除所有key.
	 * @Title: flushAll 
	 * @Description: TODO
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-19下午01:21:08
	 */
	 public String flushAll(){  
	     Jedis jedis=jedisPool.getResource();  
	     String stata=jedis.flushAll();
	     jedisPool.returnResource(jedis);
	     return stata;  
	 }  
	   
	 /** 
	  * 更改key名 
	  * @param String oldkey 
	  * @param String newkey 
	  * @return 状态码 
	  * */  
	 public String rename(String oldkey,String newkey){  
	     return rename(SafeEncoder.encode(oldkey),SafeEncoder.encode(newkey));  
	 }  
	   
	 /** 
	  * 更改key,仅当新key不存在时才执行 
	  * @param String oldkey 
	  * @param String newkey 
	  * @return 状态码 
	  * */  
	 public long renamenx(String oldkey,String newkey){  
	     Jedis jedis=jedisPool.getResource();  
	     long status =jedis.renamenx(oldkey, newkey);  
	     jedisPool.returnResource(jedis);  
	     return status;  
	 }  
	   
	 /** 
	  * 更改key 名
	  * @param String oldkey 
	  * @param String newkey 
	  * @return 状态码 
	  * */  
	 public String rename(byte[] oldkey,byte[] newkey){  
	      Jedis jedis=jedisPool.getResource();  
	      String status =jedis.rename(oldkey, newkey);  
	      jedisPool.returnResource(jedis);  
	      return status;  
	  }  
	    
	  /** 
	   * 设置key的过期时间，以秒为单位 
	   * @param String key 
	   * @param seconds 过期时间,已秒为单位 
	   * @return 影响的记录数 
	   * */  
	  public long expired(String key,int seconds){  
	      Jedis jedis=jedisPool.getResource();  
	      long count =jedis.expire(key, seconds);  
	      jedisPool.returnResource(jedis);  
	      return count;  
	  }  
	    
	  /** 
	   * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。 
	   * @param String key 
	   * @param 时间,已秒为单位 
	   * @return 影响的记录数 
	   * */  
	  public long expireAt(String key,long timestamp){  
	      Jedis jedis=jedisPool.getResource();  
	      long count =jedis.expireAt(key, timestamp);  
	      jedisPool.returnResource(jedis);  
	      return count;  
	  }  
	    
	  /** 
	   * 查询key的过期时间 
	   * @param String key 
	   * @return 以秒为单位的时间表示 
	   * */  
	  public long ttl(String key){  
		  Jedis sjedis=jedisPool.getResource();  
	      long len=sjedis.ttl(key);  
	      jedisPool.returnResource(sjedis);  
	      return len;  
	  }  
	    
	  /** 
	   * 取消对key过期时间的设置 
	   *@param key 
	   *@return 影响的记录数 
	   * */  
	  public long persist(String key){  
	      Jedis jedis=jedisPool.getResource();  
	      long count =jedis.persist(key);  
	      jedisPool.returnResource(jedis);  
	      return count;  
	  }  
	  /** 
	   * 删除keys对应的记录,可以是多个key 
	   * @param String... keys 
	   * @return 删除的记录数 
	   * */  
	  public long del(String... keys){  
	      Jedis jedis=jedisPool.getResource();  
	      long count =jedis.del(keys);  
	      jedisPool.returnResource(jedis);  
	      return count;  
	  }  
	    
	  /** 
	   * 删除keys对应的记录,可以是多个key 
	   * @param String... keys 
	   * @return 删除的记录数 
	   * */  
	  public long del(byte[]... keys){  
	      Jedis jedis=jedisPool.getResource();  
	      long count =jedis.del(keys);  
	      jedisPool.returnResource(jedis);  
	      return count;  
	  }  
	    
	  /** 
	   * 判断key是否存在 
	   * @param String key 
	   * @return boolean 
	   * */  
	  public boolean exists(String key){  
		  Jedis sjedis=jedisPool.getResource();  
	      boolean exis=sjedis.exists(key);  
	      jedisPool.returnResource(sjedis);  
	      return exis;  
	  }  
	    
	  /** 
	   * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法 
	   * @param String key 
	   * @return List<String> 集合的全部记录 
	   * **/  
	  public List<String> sort(String key){  
		  Jedis sjedis=jedisPool.getResource();  
	      List<String> list=sjedis.sort(key);  
	      jedisPool.returnResource(sjedis);  
	      return list;  
	  }  
	    
	  /** 
	   * 对List,Set,SortSet进行排序或limit 
	   * @param String key 
	   * @param SortingParams parame 定义排序类型或limit的起止位置. 
	   * @return List<String> 全部或部分记录 
	   * **/  
	  public List<String> sort(String key,SortingParams parame){
		  Jedis sjedis=jedisPool.getResource();  
	      List<String> list=sjedis.sort(key, parame);  
	      jedisPool.returnResource(sjedis);  
	      return list;  
	  }  
	    
	  /** 
	   * 返回指定key存储的类型 
	   * @param String key 
	   * @return String  string|list|set|zset|hash 
	   * **/  
	  public String type(String key){  
		  Jedis sjedis=jedisPool.getResource();  
	      String type=sjedis.type(key);  
	      jedisPool.returnResource(sjedis);  
	      return type;  
	  }  
	  /** 
	   * 查找所有匹配给定的模式的键 
	   * @param String key的表达式,*表示多个，？表示一个 
	   * */  
	  public Set<String> kyes(String pattern){  
	      Jedis jedis=jedisPool.getResource();  
	      Set<String> set =jedis.keys(pattern);  
	      jedisPool.returnResource(jedis);  
	      return set;  
	  }
}
