/**  
 * @Project: EC-BaseComponent
 * @Title: RedisTools.java
 * @Package org.meibaobao.ecos.cache
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-20 下午01:43:27
 * @Copyright: Waker360 Software Services Co.,Ltd. All rights reserved.
 * @version V1.0  
 */
package org.base.component.cache.jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.base.component.utils.SerializeUtil;
import org.springframework.stereotype.Component;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

/**
 * Redis 工具类
 * 
 * @ClassName: RedisTools
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-20 下午01:43:27
 * 
 */
@Component
public class RedisUtil implements IRedisUtil {
	
	private Logger logger = Logger.getLogger(RedisUtil.class); 

	/**
	 * 主服务器数据源连接池(主要用于写操作)
	 */
	private JedisPool jedisPool = null;
//	private Jedis jedis = null;

	/**
	 * 数据源共享连接池,主要用于读取数据(暂时没有使用)
	 */
	private ShardedJedisPool shardedJedisPool = null;
//	private ShardedJedis shardedJedis = null;

	// 构造方法
	public RedisUtil() {
	};

//	public RedisUtil(Jedis jedis) {
//		this.jedis = jedis;
//	}

	/**
	 * @return jedisPool
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * @param jedisPool
	 *            the jedisPool to set
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * @return shardedJedisPool
	 */
	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	/**
	 * @param shardedJedisPool
	 *            the shardedJedisPool to set
	 */
	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

//	/**
//	 * @return jedis
//	 */
//	public Jedis getJedis() {
//		return jedis;
//	}
//
//	/**
//	 * @param jedis
//	 *            the jedis to set
//	 */
//	public void setJedis(Jedis jedis) {
//		this.jedis = jedis;
//	}
//
//	/**
//	 * @return shardedJedis
//	 */
//	public ShardedJedis getShardedJedis() {
//		return shardedJedis;
//	}
//
//	/**
//	 * @param shardedJedis
//	 *            the shardedJedis to set
//	 */
//	public void setShardedJedis(ShardedJedis shardedJedis) {
//		this.shardedJedis = shardedJedis;
//	}

	/**
	 * 清除所有key.
	 * 
	 * @Title: flushAll
	 * @Description: TODO
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-19下午01:21:08
	 */
	@SuppressWarnings({"rawtypes" })
	public String flushAll() {
		String stata = null;
		Pool pool = null;
		Jedis redis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				redis = (Jedis)openJedis.get("jedis");
				stata = redis.flushAll();
			}
			
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return stata;
	}

	/**
	 * 更改key名
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	public String rename(String oldkey, String newkey) {
		return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
	}

	/**
	 * 更改key,仅当新key不存在时才执行
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public long renamenx(String oldkey, String newkey) {

		long status = -1;
		Pool pool = null;
		Jedis redis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				redis = (Jedis)openJedis.get("jedis");
				status = redis.renamenx(oldkey, newkey);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return status;
	}

	/**
	 * 更改key 名
	 * 
	 * @param String
	 *            oldkey
	 * @param String
	 *            newkey
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String rename(byte[] oldkey, byte[] newkey) {

		String status = null;
		Pool pool = null;
		Jedis redis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				redis = (Jedis)openJedis.get("jedis");
				status = redis.rename(oldkey, newkey);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return status;
	}

	/**
	 * 设置key的过期时间，以秒为单位
	 * 
	 * @param String
	 *            key
	 * @param seconds
	 *            过期时间,已秒为单位
	 * @return 影响的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long expired(String key, int seconds) {

		long count = 0;
		Pool pool = null;
		Jedis redis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				redis = (Jedis)openJedis.get("jedis");
				count = redis.expire(key, seconds);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return count;
	}

	/**
	 * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
	 * 
	 * @param String
	 *            key
	 * @param 时间
	 *            ,已秒为单位
	 * @return 影响的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long expireAt(String key, long timestamp) {

		long count = 0;
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.expireAt(key, timestamp);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 查询key的过期时间
	 * 
	 * @param String
	 *            key
	 * @return 以秒为单位的时间表示
	 * */
	@SuppressWarnings({"rawtypes" })
	public long ttl(String key) {
		long len = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					len = ((ShardedJedis)redis).ttl(key);
				}else if(redis instanceof Jedis){
					len = ((Jedis)redis).ttl(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return len;
	}

	/**
	 * 取消对key过期时间的设置
	 * 
	 * @param key
	 * @return 影响的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long persist(String key) {
		long count = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.persist(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 * 
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long del(String... keys) {

		long count = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.del(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 * 
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long del(byte[]... keys) {
		long count = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.del(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param String
	 *            key
	 * @return boolean
	 * */
	@SuppressWarnings({"rawtypes" })
	public boolean exists(String key) {
		boolean exis = false;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					exis = ((ShardedJedis)redis).exists(key);
				}else if(redis instanceof Jedis){
					exis = ((Jedis)redis).exists(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return exis;
	}

	/**
	 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
	 * 
	 * @param String
	 *            key
	 * @return List<String> 集合的全部记录
	 * **/
	@SuppressWarnings({"rawtypes" })
	public List<String> sort(String key) {
		List<String> list = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				list = jedis.sort(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return list;
	}

	/**
	 * 对List,Set,SortSet进行排序或limit
	 * 
	 * @param String
	 *            key
	 * @param SortingParams
	 *            parame 定义排序类型或limit的起止位置.
	 * @return List<String> 全部或部分记录
	 * **/
	@SuppressWarnings({"rawtypes" })
	public List<String> sort(String key, SortingParams parame) {
		List<String> list = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				list = jedis.sort(key, parame);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return list;
	}

	/**
	 * 返回指定key存储的类型
	 * 
	 * @param String
	 *            key
	 * @return String string|list|set|zset|hash
	 * **/
	@SuppressWarnings({"rawtypes" })
	public String type(String key) {
		String type = null;
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					type = ((ShardedJedis)redis).type(key);
				}else if(redis instanceof Jedis){
					type = ((Jedis)redis).type(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return type;
	}
	
	/**
	 * 查找所有匹配给定的模式的键
	 * 
	 * @param String
	 *            key的表达式,*表示多个，？表示一个
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> kyes(String pattern) {
		Set<String> set = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				set = jedis.keys(pattern);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return set;
	}

	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 状态码，1成功，0失败
	 * */
	@SuppressWarnings({"rawtypes" })
	public long hdel(String key, String fieid) {
		long s = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.hdel(key, fieid);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		
		return s;
	}

	/**
	 * 移除给定的一个key。
	 * 
	 * @Title: hdel
	 * @Description: TODO
	 * @param key
	 * @return
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-15下午03:20:58
	 */
	@SuppressWarnings({"rawtypes" })
	public long hdel(String key) {
		long s = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.del(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 1存在，0不存在
	 * */
	@SuppressWarnings({"rawtypes" })
	public boolean hexists(String key, String fieid) {
		boolean s = false;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					s = ((ShardedJedis)redis).hexists(key, fieid);
				}else if(redis instanceof Jedis){
					s = ((Jedis)redis).hexists(key, fieid);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return s;
	}

	/**
	 * 返回hash中指定存储位置的值
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 存储对应的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public String hget(String key, String fieid) {
		String s = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					s = ((ShardedJedis)redis).hget(key, fieid);
				}else if(redis instanceof Jedis){
					s = ((Jedis)redis).hget(key, fieid);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return s;
	}

	/**
	 * 以Map的形式返回hash中的存储和值
	 * 
	 * @param String
	 *            key
	 * @return Map<Strinig,String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public Map<String, String> hgetall(String key) {
		Map<String, String> map = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					map = ((ShardedJedis)redis).hgetAll(key);
				}else if(redis instanceof Jedis){
					map = ((Jedis)redis).hgetAll(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return map;
	}

	/**
	 * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储位置
	 * @param String
	 *            long value 要增加的值,可以是负数
	 * @return 增加指定数字后，存储位置的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public Long hincrby(String key, String fieid, long value) {
		Long s = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.hincrBy(key, fieid, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
	 * 
	 * @param String
	 *            key
	 * @return Set<String> 存储名称的集合
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> hkeys(String key) {
		Set<String> set = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					set = ((ShardedJedis)redis).hkeys(key);
				}else if(redis instanceof Jedis){
					set = ((Jedis)redis).hkeys(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return set;
	}

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 * 
	 * @param String
	 *            key
	 * @return long 存储的个数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long hlen(String key) {
		long len = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					len = ((ShardedJedis)redis).hlen(key);
				}else if(redis instanceof Jedis){
					len = ((Jedis)redis).hlen(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return len;
	}

	/**
	 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            ... fieids 存储位置
	 * @return List<String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public List<String> hmget(String key, String... fieids) {
		List<String> list = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					list = ((ShardedJedis)redis).hmget(key, fieids);
				}else if(redis instanceof Jedis){
					list = ((Jedis)redis).hmget(key, fieids);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		
		return list;
	}

	/**
	 * 添加对应关系，如果对应关系已存在，则覆盖
	 * 
	 * @param Strin
	 *            key
	 * @param Map
	 *            <String,String> 对应关系
	 * @return 状态，成功返回OK
	 * */
	@SuppressWarnings({"rawtypes" })
	public String hmset(String key, Map<String, String> map) {
		String s = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.hmset(key, map);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 添加一个对应关系
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long hset(String key, String fieid, String value) {
		long s = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.hset(key, fieid, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 添加对应关系，只有在fieid不存在时才执行
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败fieid已存
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long hsetnx(String key, String fieid, String value) {
		long s = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.hsetnx(key, fieid, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 获取hash中value的集合
	 * 
	 * @param String
	 *            key
	 * @return List<String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public List<String> hvals(String key) {
		List<String> list = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					list = ((ShardedJedis)redis).hvals(key);
				}else if(redis instanceof Jedis){
					
					list = ((Jedis)redis).hvals(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return list;
	}

	/**
	 * List长度
	 * 
	 * @param String
	 *            key
	 * @return 长度
	 * */
	public long llen(String key) {
		long len = llen(SafeEncoder.encode(key));
		return len;
	}

	/**
	 * List长度
	 * 
	 * @param byte[] key
	 * @return 长度
	 * */
	@SuppressWarnings({"rawtypes" })
	public long llen(byte[] key) {
		long count = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					count = ((ShardedJedis)redis).llen(key);
				}else if(redis instanceof Jedis){
					count = ((Jedis)redis).llen(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return count;
	}

	/**
	 * 覆盖操作,将覆盖List中指定位置的值
	 * 
	 * @param byte[] key
	 * @param int index 位置
	 * @param byte[] value 值
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String lset(byte[] key, int index, byte[] value) {
		String status = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				status = jedis.lset(key, index, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		
		return status;
	}

	/**
	 * 覆盖操作,将覆盖List中指定位置的值
	 * 
	 * @param key
	 * @param int index 位置
	 * @param String
	 *            value 值
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String lset(String key, int index, String value) {
		String lset = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				lset = jedis.lset(key, index,value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return lset;
	}

	/**
	 * 在value的相对位置插入记录
	 * 
	 * @param key
	 * @param LIST_POSITION
	 *            前面插入或后面插入
	 * @param String
	 *            pivot 相对位置的内容
	 * @param String
	 *            value 插入的内容
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		long linsert = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				linsert = jedis.linsert(key, where, pivot, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return linsert;
	}

	/**
	 * 在指定位置插入记录
	 * 
	 * @param String
	 *            key
	 * @param LIST_POSITION
	 *            前面插入或后面插入
	 * @param byte[] pivot 相对位置的内容
	 * @param byte[] value 插入的内容
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		long count = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.linsert(key, where, pivot, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 获取List中指定位置的值
	 * 
	 * @param String
	 *            key
	 * @param int index 位置
	 * @return 值
	 * **/
	public String lindex(String key, int index) {
		String encode = SafeEncoder.encode(lindex(SafeEncoder.encode(key),
				index));
		return encode;
	}

	/**
	 * 获取List中指定位置的值
	 * 
	 * @param byte[] key
	 * @param int index 位置
	 * @return 值
	 * **/
	@SuppressWarnings({"rawtypes" })
	public byte[] lindex(byte[] key, int index) {
		byte[] value = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					value = ((ShardedJedis)redis).lindex(key, index);
				}else if(redis instanceof Jedis){
					
					value = ((Jedis)redis).lindex(key, index);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return value;
	}

	/**
	 * 将List中的第一条记录移出List
	 * 
	 * @param String
	 *            key
	 * @return 移出的记录
	 * */
	@SuppressWarnings({"rawtypes" })
	public String lpop(String key) {
		String encode = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				encode = jedis.lpop(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}		
		return encode;
	}

	/**
	 * 将List中的第一条记录移出List
	 * 
	 * @param byte[] key
	 * @return 移出的记录
	 * */
	@SuppressWarnings({"rawtypes" })
	public byte[] lpop(byte[] key) {
		byte[] value = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				value = jedis.lpop(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return value;
	}

	/**
	 * 将List中最后第一条记录移出List
	 * 
	 * @param byte[] key
	 * @return 移出的记录
	 * */
	@SuppressWarnings({"rawtypes" })
	public String rpop(String key) {
		String value = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				value = jedis.rpop(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return value;
	}

	/**
	 * 向List尾部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long lpush(String key, String value) {
		long lpush = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				lpush = jedis.lpush(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return lpush;
	}

	/**
	 * 向List尾部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long lpush(String key, String... value) {
		Long lpush = -1l;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				lpush = jedis.lpush(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return lpush;
	}

	/**
	 * 向List头部追加记录
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long rpush(String key, String value) {
		long count = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.rpush(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 向List中追加记录
	 * 
	 * @param byte[] key
	 * @param byte[] value
	 * @return 记录总数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long lpush(byte[] key, byte[]... value) {
		long count = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.lpush(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param String
	 *            key
	 * @param long start
	 * @param long end
	 * @return List
	 * */
	@SuppressWarnings({"rawtypes" })
	public List<String> lrange(String key, long start, long end) {
		List<String> list = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					list = ((ShardedJedis)redis).lrange(key, start, end);
				}else if(redis instanceof Jedis){
					list = ((Jedis)redis).lrange(key, start, end);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return list;
	}

	/**
	 * 获取指定范围的记录，可以做为分页使用
	 * 
	 * @param byte[] key
	 * @param int start
	 * @param int end 如果为负数，则尾部开始计算
	 * @return List
	 * */
	@SuppressWarnings({"rawtypes" })
	public List<byte[]> lrange(byte[] key, int start, int end) {
		List<byte[]> list = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					list = ((ShardedJedis)redis).lrange(key, start, end);
				}else if(redis instanceof Jedis){
					list = ((Jedis)redis).lrange(key, start, end);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return list;
	}

	/**
	 * 删除List中c条记录，被删除的记录值为value
	 * 
	 * @param byte[] key
	 * @param int c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
	 * @param byte[] value 要匹配的值
	 * @return 删除后的List中的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long lrem(byte[] key, int c, byte[] value) {
		long count = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				count = jedis.lrem(key, c, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return count;
	}

	/**
	 * 删除List中c条记录，被删除的记录值为value
	 * 
	 * @param String
	 *            key
	 * @param int c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
	 * @param String
	 *            value 要匹配的值
	 * @return 删除后的List中的记录数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long lrem(String key, int c, String value) {
		long lrem = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				lrem = lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return lrem;
	}

	/**
	 * 算是删除吧，只保留start与end之间的记录
	 * 
	 * @param byte[] key
	 * @param int start 记录的开始位置(0表示第一条记录)
	 * @param int end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
	 * @return 执行状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String ltrim(byte[] key, int start, int end) {
		String str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.ltrim(key, start, end);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}		
		return str;
	}

	/**
	 * 算是删除，只保留start与end之间的记录
	 * 
	 * @param String
	 *            key
	 * @param int start 记录的开始位置(0表示第一条记录)
	 * @param int end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
	 * @return 执行状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String ltrim(String key, int start, int end) {
		String ltrim = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				ltrim = jedis.ltrim(key, start, end);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return ltrim;
	}

	/**
	 * 向Set添加一条记录，如果member已存在返回0,否则返回1,-1失败
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return 操作码,0或1
	 * */
	@SuppressWarnings({"rawtypes" })
	public long sadd(String key, String member) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.sadd(key, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 获取给定key中元素个数
	 * 
	 * @param String
	 *            key
	 * @return 元素个数
	 * */
	@SuppressWarnings({"rawtypes" })
	public long scard(String key) {
		long len = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					len = ((ShardedJedis)redis).scard(key);
				}else if(redis instanceof Jedis){
					len = ((Jedis)redis).scard(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return len;
	}

	/**
	 * 返回从第一组和所有的给定集合之间的差异的成员
	 * 
	 * @param String
	 *            ... keys
	 * @return 差异的成员集合
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> sdiff(String... keys) {
		Set<String> set = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				set = jedis.sdiff(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return set;
	}

	/**
	 * 这个命令等于sdiff,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
	 * 
	 * @param String
	 *            newkey 新结果集的key
	 * @param String
	 *            ... keys 比较的集合
	 * @return 新集合中的记录数
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long sdiffstore(String newkey, String... keys) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.sdiffstore(newkey, keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 返回给定集合交集的成员,如果其中一个集合为不存在或为空，则返回空Set
	 * 
	 * @param String
	 *            ... keys
	 * @return 交集成员的集合
	 * **/
	@SuppressWarnings({"rawtypes" })
	public Set<String> sinter(String... keys) {
		Set<String> set = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				set = jedis.sinter(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return set;
	}

	/**
	 * 这个命令等于sinter,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
	 * 
	 * @param String
	 *            newkey 新结果集的key
	 * @param String
	 *            ... keys 比较的集合
	 * @return 新集合中的记录数
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long sinterstore(String newkey, String... keys) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.sinterstore(newkey, keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 确定一个给定的值是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member 要判断的值
	 * @return 存在返回1，不存在返回0
	 * **/
	@SuppressWarnings({"rawtypes" })
	public boolean sismember(String key, String member) {
		boolean s = false;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					s = ((ShardedJedis)redis).sismember(key, member);
				}else if(redis instanceof Jedis){
					s = ((Jedis)redis).sismember(key, member);
					
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return s;
	}

	/**
	 * 返回集合中的所有成员
	 * 
	 * @param String
	 *            key
	 * @return 成员集合
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> smembers(String key) {
		Set<String> set = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					set = ((ShardedJedis)redis).smembers(key);
				}else if(redis instanceof Jedis){
					set = ((Jedis)redis).smembers(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return set;
	}

	/**
	 * 将成员从源集合移出放入目标集合 <br/>
	 * 如果源集合不存在或不包哈指定成员，不进行任何操作，返回0<br/>
	 * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除
	 * 
	 * @param String
	 *            srckey 源集合
	 * @param String
	 *            dstkey 目标集合
	 * @param String
	 *            member 源集合中的成员
	 * @return 状态码，1成功，0失败
	 * */
	@SuppressWarnings({"rawtypes" })
	public long smove(String srckey, String dstkey, String member) {
		long s = 0;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.smove(srckey, dstkey, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 从集合中删除成员
	 * 
	 * @param String
	 *            key
	 * @return 被删除的成员
	 * */
	@SuppressWarnings({"rawtypes" })
	public String spop(String key) {
		String s = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.spop(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 从集合中删除指定成员
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member 要删除的成员
	 * @return 状态码，成功返回1，成员不存在返回0,-1失败
	 * */
	@SuppressWarnings({"rawtypes" })
	public long srem(String key, String member) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.srem(key, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 合并多个集合并返回合并后的结果，合并后的结果集合并不保存<br/>
	 * 
	 * @param String
	 *            ... keys
	 * @return 合并后的结果集合
	 * @see sunionstore
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> sunion(String... keys) {
		Set<String> set = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				set = jedis.sunion(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return set;
	}

	/**
	 * 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖
	 * 
	 * @param String
	 *            newkey 新集合的key
	 * @param String
	 *            ... keys 要合并的集合
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long sunionstore(String newkey, String... keys) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.sunionstore(newkey, keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
	 * 
	 * @param String
	 *            key
	 * @param double score 权重
	 * @param String
	 *            member 要加入的值，
	 * @return 状态码 1成功，0已存在member的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zadd(String key, double score, String member) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.zadd(key, score, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 获取集合中元素的数量
	 * 
	 * @param String
	 *            key
	 * @return 如果返回0则集合不存在
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zcard(String key) {
		long len = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					len = ((ShardedJedis)redis).zcard(key);
				}else if(redis instanceof Jedis){
					len = ((Jedis)redis).zcard(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return len;
	}

	/**
	 * 获取指定权重区间内集合的数量
	 * 
	 * @param String
	 *            key
	 * @param double min 最小排序位置
	 * @param double max 最大排序位置
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zcount(String key, double min, double max) {
		long len = 0;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					len = ((ShardedJedis)redis).zcount(key, min, max);
				}else if(redis instanceof Jedis){
					
					len = ((Jedis)redis).zcount(key, min, max);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return len;
	}

	/**
	 * 权重增加给定值，如果给定的member已存在
	 * 
	 * @param String
	 *            key
	 * @param double score 要增的权重
	 * @param String
	 *            member 要插入的值
	 * @return 增后的权重
	 * */
	@SuppressWarnings({"rawtypes" })
	public Double zincrby(String key, double score, String member) {
		Double s = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.zincrby(key, score, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
	 * 
	 * @param String
	 *            key
	 * @param int start 开始位置(包含)
	 * @param int end 结束位置(包含)
	 * @return Set<String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> zrange(String key, int start, int end) {
		Set<String> set = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					set = ((ShardedJedis)redis).zrange(key, start, end);
				}else if(redis instanceof Jedis){
					set = ((Jedis)redis).zrange(key, start, end);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return set;
	}

	/**
	 * 返回指定权重区间的元素集合
	 * 
	 * @param String
	 *            key
	 * @param double min 上限权重
	 * @param double max 下限权重
	 * @return Set<String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> set = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					set = ((ShardedJedis)redis).zrangeByScore(key, min, max);
				}else if(redis instanceof Jedis){
					set = ((Jedis)redis).zrangeByScore(key, min, max);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return set;
	}

	/**
	 * 获取指定值在集合中的位置，集合排序从低到高
	 * 
	 * @see zrevrank
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return long 位置
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zrank(String key, String member) {
		long index = -1;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					index = ((ShardedJedis)redis).zrank(key, member);
				}else if(redis instanceof Jedis){
					index = ((Jedis)redis).zrank(key, member);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return index;
	}

	/**
	 * 获取指定值在集合中的位置，集合排序从低到高
	 * 
	 * @see zrank
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return long 位置
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zrevrank(String key, String member) {
		long index = -1;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					index = ((ShardedJedis)redis).zrevrank(key, member);
				}else if(redis instanceof Jedis){
					index = ((Jedis)redis).zrevrank(key, member);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return index;
	}

	/**
	 * 从集合中删除成员
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return 返回1成功
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zrem(String key, String member) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.zrem(key, member);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public long zrem(String key) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.del(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 删除给定位置区间的元素
	 * 
	 * @param String
	 *            key
	 * @param int start 开始区间，从0开始(包含)
	 * @param int end 结束区间,-1为最后一个元素(包含)
	 * @return 删除的数量
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zremrangeByRank(String key, int start, int end) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.zremrangeByRank(key, start, end);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 删除给定权重区间的元素
	 * 
	 * @param String
	 *            key
	 * @param double min 下限权重(包含)
	 * @param double max 上限权重(包含)
	 * @return 删除的数量
	 * */
	@SuppressWarnings({"rawtypes" })
	public long zremrangeByScore(String key, double min, double max) {
		long s = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				s = jedis.zremrangeByScore(key, min, max);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return s;
	}

	/**
	 * 获取给定区间的元素，原始按照权重由高到低排序
	 * 
	 * @param String
	 *            key
	 * @param int start
	 * @param int end
	 * @return Set<String>
	 * */
	@SuppressWarnings({"rawtypes" })
	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> set = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					set = ((ShardedJedis)redis).zrevrange(key, start, end);
				}else if(redis instanceof Jedis){
					
					set = ((Jedis)redis).zrevrange(key, start, end);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return set;
	}

	/**
	 * 获取给定值在集合中的权重
	 * 
	 * @param String
	 *            key
	 * @param memeber
	 * @return double 权重
	 * */
	@SuppressWarnings({"rawtypes" })
	public double zscore(String key, String memebr) {
		Double score = -1d;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					score = ((ShardedJedis)redis).zscore(key, memebr);
				}else if(redis instanceof Jedis){
					score = ((Jedis)redis).zscore(key, memebr);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		if (score != null)
			return score;
		return score;
	}

	/**
	 * 根据key获取记录
	 * 
	 * @param String
	 *            key
	 * @return 值
	 * */
	@SuppressWarnings({"rawtypes" })
	public String get(String key) {
		String value = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					value = ((ShardedJedis)redis).get(key);
				}else if(redis instanceof Jedis){
					
					value = ((Jedis)redis).get(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return value;
	}

	/**
	 * 根据key获取记录
	 * 
	 * @param byte[] key
	 * @return 值
	 * */
	@SuppressWarnings({"rawtypes" })
	public byte[] get(byte[] key) {
		byte[] value = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					value = ((ShardedJedis)redis).get(key);
				}else if(redis instanceof Jedis){
					value = ((Jedis)redis).get(key);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return value;
	}

	/**
	 * 添加有过期时间的记录
	 * 
	 * @param String
	 *            key
	 * @param int seconds 过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 * */
	@SuppressWarnings({"rawtypes" })
	public String setEx(String key, int seconds, String value) {
		String str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.setex(key, seconds, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 添加有过期时间的记录
	 * 
	 * @param String
	 *            key
	 * @param int seconds 过期时间，以秒为单位
	 * @param String
	 *            value
	 * @return String 操作状态
	 * */
	@SuppressWarnings({"rawtypes" })
	public String setEx(byte[] key, int seconds, byte[] value) {
		String str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.setex(key, seconds, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 添加一条记录，仅当给定的key不存在时才插入
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 状态码，1插入成功且key不存在，0未插入，key存在
	 * */
	@SuppressWarnings({"rawtypes" })
	public long setnx(String key, String value) {
		long str = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.setnx(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String set(String key, String value) {
		String set = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				set = jedis.set(key,value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return set;
	}

	/**
	 * 添加记录,如果记录已存在将覆盖原有的value
	 * 
	 * @param byte[] key
	 * @param byte[] value
	 * @return 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String set(byte[] key, byte[] value) {
		String status = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				status = jedis.set(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return status;
	}

	/**
	 * 设置对象到指定key
	 * 
	 * @Title: setObject
	 * @Description: TODO
	 * @param keyStr
	 * @param obj
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午04:48:42
	 */
	public String setObject(String keyStr, Object obj) {
		return this.set(keyStr.getBytes(), SerializeUtil.serialize(obj));
	}

	/**
	 * 设置对象到指定key,并指定过期时间
	 * 
	 * @Title: setObject
	 * @Description: TODO
	 * @param keyStr
	 * @param expire
	 *            　　过期时间
	 * @param obj
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午04:47:09
	 */
	public String setObject(String keyStr, int expire, Object obj) {
		return this.setEx(keyStr.getBytes(), expire, SerializeUtil.serialize(obj));
	}

	/**
	 * 跟据key获取对象
	 * 
	 * @Title: getObject
	 * @Description: TODO
	 * @param keys
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午04:45:53
	 */
	public Object getObject(String keyStr) {
		byte[] o = this.get(keyStr.getBytes());
		if(null == o){
			return null;
		}
		return SerializeUtil.unserialize(o);
	}

	/**
	 * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
	 * 例:String str1="123456789";<br/>
	 * 对str1操作后setRange(key,4,0000)，str1="123400009";
	 * 
	 * @param String
	 *            key
	 * @param long offset
	 * @param String
	 *            value
	 * @return long value的长度
	 * */
	@SuppressWarnings({"rawtypes" })
	public long setRange(String key, long offset, String value) {
		long len = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				len = jedis.setrange(key, offset, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return len;
	}

	/**
	 * 在指定的key中追加value
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return long 追加后value的长度
	 * **/
	@SuppressWarnings({"rawtypes" })
	public long append(String key, String value) {
		long len = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				len = jedis.append(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return len;
	}

	/**
	 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String
	 *            key
	 * @param long number 要减去的值
	 * @return long 减指定值后的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public Long decrBy(String key, long number) {
		Long len = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				len = jedis.decrBy(key, number);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return len;
	}

	/**
	 * <b>可以作为获取唯一id的方法</b><br/>
	 * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String
	 *            key
	 * @param long number 要减去的值
	 * @return long 相加后的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public Long incrBy(String key, long number) {
		Long len = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				len = jedis.incrBy(key, number);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return len;
	}

	/**
	 * 对指定key对应的value进行截取
	 * 
	 * @param String
	 *            key
	 * @param long startOffset 开始位置(包含)
	 * @param long endOffset 结束位置(包含)
	 * @return String 截取的值
	 * */
	@SuppressWarnings({"rawtypes" })
	public String getrange(String key, long startOffset, long endOffset) {
		String value = null;
		
		Pool pool = null;
		Object redis = null;
		try {
			Map<String, Object> openRedis =openRedis();
			if(null !=openRedis){
				pool = (Pool)openRedis.get("pool");
				redis =openRedis.get("jedis");
				if(redis instanceof ShardedJedis){
					value = ((ShardedJedis)redis).getrange(key, startOffset, endOffset);
				}else if(redis instanceof Jedis){
					value = ((Jedis)redis).getrange(key, startOffset, endOffset);
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, redis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, redis);
		}
		return value;
	}

	/**
	 * 获取并设置指定key对应的value<br/>
	 * 如果key存在返回之前的value,否则返回null
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            value
	 * @return String 原始value或null
	 * */
	@SuppressWarnings({"rawtypes" })
	public String getSet(String key, String value) {
		String str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.getSet(key, value);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
	 * 
	 * @param String
	 *            keys
	 * @return List<String> 值得集合
	 * */
	@SuppressWarnings({"rawtypes" })
	public List<String> mget(String... keys) {
		List<String> str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.mget(keys);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 批量存储记录
	 * 
	 * @param String
	 *            keysvalues 例:keysvalues="key1","value1","key2","value2";
	 * @return String 状态码
	 * */
	@SuppressWarnings({"rawtypes" })
	public String mset(String... keysvalues) {
		String str = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				str = jedis.mset(keysvalues);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return str;
	}

	/**
	 * 获取key对应的值的长度
	 * 
	 * @param String
	 *            key
	 * @return value值得长度
	 *
	 **/
	@SuppressWarnings({"rawtypes" })
	public long strlen(String key) {
		long len = -1;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				len = jedis.strlen(key);
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return len;
	}

	/**
	 * 使用redis管道执行特定操作(常用于批量处理)
	 * 
	 * @Title: PipelineExecute
	 * @Description: TODO
	 * @param pipelineExecute
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21下午02:52:53
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Object> pipelineExecute(PipelineExecute pipelineExecute) {
		List<Object> backResult = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				Client client = jedis.getClient();
				pipelineExecute.setClient(client);
				try {
					pipelineExecute.execute();
					// pipelineExecute.sync();
					backResult = pipelineExecute.syncAndReturnAll();
				} catch (Exception ex) {
					ex.printStackTrace();
					throw ex;
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return backResult;
	}

	/**
	 * 执行redis操作并对事务进行控制
	 * 
	 * @Title: execute
	 * @Description: TODO
	 * @param redisExecute
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午12:35:45
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Object> execute(RedisTransactionExecute redisExecute) {
		List<Object> backResult = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				
				Client client = jedis.getClient();
				redisExecute.setClient(client);
				try {
					client.multi();
					redisExecute.execute();
					backResult = redisExecute.exec();
				} catch (Exception ex) {
					backResult = null;
					redisExecute.discard();
					throw ex;
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);;
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return backResult;
	}

	/**
	 * 开启对keys监控,并对key的操作进行事务管理
	 * 
	 * @Title: watchExecute
	 * @Description: TODO
	 * @param redisExecute
	 * @param keys
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21上午01:33:19
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Object> watchExecute(RedisTransactionExecute redisExecute,
			byte[]... keys) {
		List<Object> backResult = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				
				Client client = jedis.getClient();
				redisExecute.setClient(client);
				try {
					client.watch(keys);
					client.multi();
					redisExecute.execute();
					backResult = redisExecute.exec();
				} catch (Exception ex) {
					backResult = null;
					redisExecute.discard();
					throw ex;
				} finally {
					client.unwatch();
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return backResult;
	}

	/**
	 * 开启对keys监控,并对key的操作进行事务管理
	 * 
	 * @Title: watchExecute
	 * @Description: TODO
	 * @param redisExecute
	 *            一个抽像类.实现execute方法
	 * @param keys
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21上午01:35:59
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Object> watchExecute(RedisTransactionExecute redisExecute,
			String... keys) {
		List<Object> backResult = null;
		
		Pool pool = null;
		Jedis jedis = null;
		try {
			Map<String, Object> openJedis = openJedis();
			if(null != openJedis){
				pool = (Pool)openJedis.get("pool");
				jedis = (Jedis)openJedis.get("jedis");
				
				Client client = jedis.getClient();
				redisExecute.setClient(client);
				try {
					client.watch(keys);
					client.multi();
					redisExecute.execute();
					backResult = redisExecute.exec();
				} catch (Exception ex) {
					backResult = null;
					redisExecute.discard();
					throw ex;
				} finally {
					client.unwatch();
				}
			}
		} catch (Exception e) {
			returnBrokenJedis(pool, jedis);
			throw new RuntimeException(e);
		} finally {
			closeJedis(pool, jedis);
		}
		return backResult;
	}
	
	/**
	 * 打开redis连接
	 * @return
	 * @throws Exception
	 */
	private Map<String,Object> openRedis() throws Exception{
		if (null != shardedJedisPool) {
			return openShareJedis();
		} else if(null != jedisPool){
			return openJedis();
		}
		return null;
	}
	
	/**
	 * 打开JEDIS数据库连接
	 * 
	 * @param jedis
	 * @throws Exception 
	 */
	public Map<String,Object> openJedis() throws Exception {
		Map<String,Object> result = null;
		if (null == jedisPool) {
			return null;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = new HashMap<String,Object>();
			result.put("pool", jedisPool);
			result.put("jedis", jedis);
		} catch (Exception e) {
			returnBrokenJedis(jedisPool,jedis);
			logger.error(e.getMessage(),e);
			throw e;
		}
		return result;
	}

	/**
	 * 打开JEDIS从数据库连接
	 * @throws Exception 
	 * 
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-8-30下午08:41:23
	 */
	public Map<String,Object> openShareJedis() throws Exception {
		Map<String,Object> result = null;
		if (null == shardedJedisPool) {
			return null;
		}
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();
			result = new HashMap<String,Object>();
			result.put("pool", shardedJedisPool);
			result.put("jedis", shardedJedis);
		} catch (Exception e) {
			returnBrokenJedis(shardedJedisPool,shardedJedis);
			logger.error(e.getMessage(),e);
			throw e;
		}
		return result;
	}

	/**
	 * 关闭JEDIS数据库连接(返回连接池)
	 * 
	 * @param jedis
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void closeJedis(Pool p, Object jedis) {
		if (null != p && null != jedis) {
			try {
				// 返回连接池
				p.returnResource(jedis);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放redis对象
	 * 
	 * @param p
	 * @param jedis
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-8-31下午02:12:21
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void returnBrokenJedis(Pool p, Object jedis) {

		if (null != p && null != jedis) {
			try {
				// 释放redis对象
				p.returnBrokenResource(jedis);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}
		}
	}

	// /**
	// * 关闭JEDIS从数据库连接
	// * @param jedis
	// */
	// public void closeShareJedis() {
	// if (null != shardedJedis) {
	// try {
	// shardedJedisPool.returnResource(shardedJedis);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	//
	// /**
	// * 得到redis管道(常用于批量处理)
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: getPipeline
	// * @Description: 当使用pipeline管理处理时,需Pipeline.sync();同步提交?
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20下午04:06:38
	// */
	// public Pipeline getPipeline(){
	// Pipeline pl = jedis.pipelined();
	// return pl;
	// }
	//
	// /**
	// * 开启redis 事务
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: createTransaction
	// * @Description: TODO
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20上午09:13:58
	// */
	// public Transaction createTransaction(){
	// return jedis.multi();
	// }
	//
	// /**
	// * 提交redis 事务
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: commit
	// * @Description: TODO
	// * @param t
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20上午11:30:53
	// */
	// public List<Object> commit(Transaction t)throws Exception{
	// List<Object> list=null;
	// try{
	// list = t.exec();
	// }catch(Exception e){
	// throw e;
	// }
	// return list;
	// }
	//
	// /**
	// * 回滚redis 事务
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: commit
	// * @Description: TODO
	// * @param t
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20上午11:30:53
	// */
	// public String rollback(Transaction t)throws Exception{
	// String state=null;
	// try{
	// state = t.discard();
	// }catch(Exception e){
	// throw e;
	// }
	// return state;
	// }
	//
	// /**
	// * 开启对keys监控
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: watch
	// * @Description: TODO
	// * @param keys
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20下午01:15:05
	// */
	// public String watch(byte[]...keys){
	// return jedis.watch(keys);
	// }
	//
	// /**
	// * 开启对keys监控
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: watch
	// * @Description: TODO
	// * @param keys
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20下午01:16:07
	// */
	// public String watch(String...keys){
	// return jedis.watch(keys);
	// }
	//
	// /**
	// * 取消监控
	// * 需要手动开启和关闭JEDIS连接池
	// * @Title: unwatch
	// * @Description: TODO
	// * @return
	// * @throws
	// * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	// * @date: 2012-12-20下午01:16:16
	// */
	// public String unwatch(){
	// return jedis.unwatch();
	// }

	final int oneStep = 100;
	public Map<String, String> hgetallPiPeStep(String cacheKey,List<String> cacheMapKeys){
		Map<String,String> data = new HashMap<String, String>();
		Jedis jedis = jedisPool.getResource();
		try{
			long start = System.currentTimeMillis();
			Pipeline pipe = jedis.pipelined();
			for(String oneKey:cacheMapKeys){
				pipe.hget(cacheKey, oneKey);
			}
			List<Object> cacheData = pipe.syncAndReturnAll();
			long end = System.currentTimeMillis();
			
			System.out.println("pipe 获取数据:" + cacheKey + " " + start + " " + end);
			if(cacheData == null) return null;
			else{
				for(int idx =0;idx<cacheMapKeys.size(); idx++){
					String onemapKey =  cacheMapKeys.get(idx);
					data.put(onemapKey, cacheData.get(idx) == null ? null : cacheData.get(idx).toString());
				}
			}
		}catch(Exception e){
			returnBrokenJedis(jedisPool, jedis);
			throw new RuntimeException(e);
		}finally{
			closeJedis(jedisPool, jedis);
		}
		
		return data;
	}
	
	public Map<String, String> hgetallPiPe(String cacheKey,List<String> cacheMapKeys) {
		Map<String,String> data = new HashMap<String, String>();
		List<String> oneCacheMapKeys = new ArrayList<String>();
		Map<String,String> oneData = new HashMap<String, String>();
		for(int idx=0;idx<cacheMapKeys.size();idx++){
			oneCacheMapKeys.add(cacheMapKeys.get(idx));
			if((idx+1)%oneStep == 0){
				System.out.println(cacheKey + " step:" + (idx+1) + "  start ...");
				oneData = hgetallPiPeStep(cacheKey, cacheMapKeys);
				if(oneData!=null && !oneData.isEmpty()){
					data.putAll(oneData);
				}
				oneCacheMapKeys = new ArrayList<String>();
				System.out.println(cacheKey + " step:" + (idx+1) + "  ok,end!");
			}else if((idx+1) == cacheMapKeys.size() && !oneCacheMapKeys.isEmpty()){
				oneData = hgetallPiPeStep(cacheKey, cacheMapKeys);
				if(oneData!=null && !oneData.isEmpty()){
					data.putAll(oneData);
					break;
				}
			}
			
		}
		return data;
	}

	@Override
	public void hmsetPiPe(String key, Map<String, String> map) {
		
		Iterator<String> keyIter = map.keySet().iterator();
		String oneKey = "";
		String oneVal = "";
		Map<String, String> oneMap = new HashMap<String, String>();
		int idx =1;
		while(keyIter.hasNext()){
			oneKey = keyIter.next();
			oneVal = map.get(oneKey);
			oneMap.put(oneKey, oneVal);
			if(idx%oneStep == 0){
				System.out.println(key + " step:" + idx + "  start ...");
				hmsetPiPeStep(key, oneMap);
				oneMap = new HashMap<String, String>();
				System.out.println(key + " step:" + idx + "  ok,end!");
			}else if(idx == map.size() && !oneMap.isEmpty()){
				hmsetPiPeStep(key, oneMap);
				break;
			}
			idx++;
		}
	}
	
	public void hmsetPiPeStep(String key, Map<String, String> map) {
		Jedis jedis = jedisPool.getResource();
		try{
			Pipeline pipe = jedis.pipelined();
			Iterator<String> keyIter = map.keySet().iterator();
			String oneKey = "";
			String oneVal = "";
			while(keyIter.hasNext()){
				oneKey = keyIter.next();
				oneVal = map.get(oneKey);
				pipe.hset(key, oneKey, oneVal);
			}
//			pipe.sync();
			pipe.syncAndReturnAll();
			jedisPool.returnResource(jedis);
			jedisPool.returnBrokenResource(jedis);
		}catch(Exception e){
			returnBrokenJedis(jedisPool, jedis);
			throw new RuntimeException(e);
		}finally{
			closeJedis(jedisPool, jedis);
		}
	}
}
