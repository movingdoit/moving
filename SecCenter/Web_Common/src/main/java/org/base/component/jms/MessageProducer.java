package org.base.component.jms;

import java.util.List;
import java.util.Map;

import javax.jms.Destination;


import org.springframework.jms.core.JmsTemplate;

/**
 * JMS用户变更消息生产者.
 * 
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 * 
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 */
public abstract class MessageProducer {

	public void sendQueue(final Map<String,Object> map) throws Exception {
		sendMessage(map, getDestination());
	}
	
	public void sendQueue(final String json) throws Exception {
		sendMessage(json, getDestination());
	}

	public void sendTopic(final Map<String,Object> map) throws Exception {
		sendMessage(map, getDestination());
	}
	
	public void sendTopic(final String json) throws Exception {
		sendMessage(json, getDestination());
	}

	private void sendMessage(Map<String,Object> map, Destination destination) throws Exception{
		getJmsTemplate().convertAndSend(destination, map);
	}
	
	private void sendMessage(String json, Destination destination) throws Exception{
		getJmsTemplate().convertAndSend(destination, json);
	}

	/**
	 * 将单个对象放入对列
	 * @param <T>
	 * @param t
	 */
	public abstract <T> void pushObject(T t) throws Exception;
	
	/**
	 * 将对象集合放入对列
	 * @param <T>
	 * @param t
	 */
	public abstract <T> void pushList(List<T> t) throws Exception;
	
	/**
	 * 设置jms目标
	 * @param Destination
	 */
	protected abstract Destination getDestination() throws Exception;
	
	/**
	 * 设置JmsTemplate
	 * @param JmsTemplate
	 */
	protected abstract JmsTemplate getJmsTemplate() throws Exception;
}
