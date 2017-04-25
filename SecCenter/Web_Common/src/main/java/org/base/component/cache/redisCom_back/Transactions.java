/**  
 * @Project: EC-Examples
 * @Title: Transactions.java
 * @Package org.meibaobao.ecos.cache
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-20 上午09:08:13
 * @Copyright: Waker360 Software Services Co.,Ltd. All rights reserved.
 * @version V1.0  
 */
package org.base.component.cache.redisCom_back;

import java.util.List;

import org.base.component.cache.jedis.PipelineExecute;
import org.base.component.cache.jedis.RedisTransactionExecute;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/** 
 * redis 事务
 * @ClassName: Transactions 
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-20 上午09:08:13
 *  
 */
public class Transactions {
	private JedisPool jedisPool;
	
	private static ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<Jedis>();
	
	/**
	 * 执行redis操作并对事务进行控制
	 * @Title: execute 
	 * @Description: TODO
	 * @param redisExecute
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午12:35:45
	 */
	public Object execute(RedisTransactionExecute redisExecute){
		Jedis jedis = null;
		List<Object> state=null;
		Object backResult=null;
		try {
			jedis=jedisPool.getResource();
			Client client = jedis.getClient();
			client.multi();
			redisExecute.setClient(client);
			redisExecute.execute();
			state = redisExecute.exec();
		} catch (Exception ex) {
			state=null;
			redisExecute.discard();
		}finally{
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
		}
		if(null==state){
			backResult=null;
		}
		return backResult;
	}
    
	/**
	 * 开启对keys监控,并对key的操作进行事务管理
	 * @Title: watchExecute 
	 * @Description: TODO
	 * @param redisExecute
	 * @param keys
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21上午01:33:19
	 */
	public Object watchExecute(RedisTransactionExecute redisExecute,byte[]...keys){
		Jedis jedis=jedisPool.getResource();
		List<Object> state=null;
		Object backResult=null;
		Client client = jedis.getClient();
		redisExecute.setClient(client);
		try {
			client.watch(keys);
			client.multi();
			redisExecute.execute();
			state = redisExecute.exec();
		} catch (Exception ex) {
			state=null;
			redisExecute.discard();
		}finally{
			try{
				client.unwatch();
			}catch(Exception e){}
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
		}
		if(null==state){
			backResult=null;
		}
		return backResult;
	}
	
	/**
	 * 开启对keys监控,并对key的操作进行事务管理
	 * @Title: watchExecute 
	 * @Description: TODO
	 * @param redisExecute
	 * @param keys
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21上午01:35:59
	 */
	public Object watchExecute(RedisTransactionExecute redisExecute,String...keys){
		Jedis jedis=jedisPool.getResource();
		List<Object> state=null;
		Object backResult=null;
		Client client = jedis.getClient();
		redisExecute.setClient(client);
		try {
			client.watch(keys);
			client.multi();
			redisExecute.execute();
			state = redisExecute.exec();
		} catch (Exception ex) {
			state=null;
			redisExecute.discard();
		}finally{
			try{
				client.unwatch();
			}catch(Exception e){}
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
		}
		if(null==state){
			backResult=null;
		}
		return backResult;
	}
	
    /**
     * 使用redis管道执行特定操作(常用于批量处理)
     * @Title: PipelineExecute 
     * @Description: TODO
     * @param pipelineExecute
     * @return  
     * @throws 
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-21下午02:52:53
     */
    public List<Object> pipelineExecute(PipelineExecute pipelineExecute){
    	Jedis jedis=jedisPool.getResource();
		List<Object> backResult=null;
		Client client = jedis.getClient();
		pipelineExecute.setClient(client);
		try {
			pipelineExecute.execute();//backResult=pipelineExecute.execute();
//			pipelineExecute.sync();
			backResult = pipelineExecute.syncAndReturnAll();
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
		}
		return backResult;
    }
	
	/**
	 * 开启redis 事务
	 * 必须手动关闭事务
	 * @Title: createTransaction 
	 * @Description: TODO
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20上午09:13:58
	 */
	public Transaction createTransaction(){
		Jedis jedis = jedisPool.getResource();	
		jedisThreadLocal.set(jedis);
		return jedis.multi();
	}
	
	/**
	 * 提交redis 事务
	 * @Title: commit 
	 * @Description: TODO
	 * @param t
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20上午11:30:53
	 */
	public List<Object> commit(Transaction t)throws Exception{
		Jedis jedis =null;
		List<Object> list=null;
		try{
			list = t.exec();
		}catch(Exception e){
			throw e;
		}finally{
			jedis = jedisThreadLocal.get();
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
			jedisThreadLocal.remove();
		}
		return list;
	}
	
	/**
	 * 提交redis 事务
	 * @Title: commit 
	 * @Description: TODO
	 * @param t
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20上午11:30:53
	 */
	public String rollback(Transaction t)throws Exception{
		String state=null;
		Jedis jedis =null;
		try{
			state = t.discard();
		}catch(Exception e){
			throw e;
		}finally{
			jedis = jedisThreadLocal.get();
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
			jedisThreadLocal.remove();
		}
		return state;
	}
	
	/**
	 * 开启监控
	 * 注：连接返回连接池
	 * @Title: watch 
	 * @Description: TODO
	 * @param keys
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午01:15:05
	 */
	public String watch(byte[]...keys){
		Jedis jedis=jedisPool.getResource();
		jedisThreadLocal.set(jedis);
		return jedis.watch(keys);
	}
	
	/**
	 * 开启监控
	 * 注：连接返回连接池
	 * @Title: watch 
	 * @Description: TODO
	 * @param keys
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午01:16:07
	 */
	public String watch(String...keys){
		Jedis jedis=jedisPool.getResource();
		jedisThreadLocal.set(jedis);
		return jedis.watch(keys);
	}
	
	/**
	 * 取消监控
	 * 注：连接返回连接池
	 * @Title: unwatch 
	 * @Description: TODO
	 * @return  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-20下午01:16:16
	 */
	public String unwatch(){
		Jedis jedis =null;
		String back=null;
		try{
			jedis = jedisThreadLocal.get();
			back=jedis.unwatch();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=jedis){
				jedisPool.returnResource(jedis);
			}
			jedisThreadLocal.remove();
		}
		return back;
	}
	
    /**
     * 得到redis管道(常用于批量处理)
     * 注：连接返回连接池
     * @Title: getPipeline  
     * @Description: 当使用pipeline管理处理时,需Pipeline.sync();同步提交?
     * @return  
     * @throws 
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2012-12-20下午04:06:38
     */
    public Pipeline getPipeline(){
    	Jedis jedis=jedisPool.getResource();
    	jedisThreadLocal.set(jedis);
    	Pipeline pl = jedis.pipelined();
    	return pl;
    }
	
	/**
	 * 将 redis 连接返回连接池
	 * @Title: returnResource 
	 * @Description: TODO  
	 * @throws 
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2012-12-21上午01:50:29
	 */
	public void returnResource(){
		Jedis jedis = jedisThreadLocal.get();
		if(null!=jedis){
			jedisPool.returnResource(jedis);
		}
		jedisThreadLocal.remove();
	}
}
