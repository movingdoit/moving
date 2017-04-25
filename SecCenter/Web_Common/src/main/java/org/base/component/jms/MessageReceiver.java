package org.base.component.jms;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageListener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

public abstract class MessageReceiver extends DefaultMessageListenerContainer{

	@Resource(name="connectionFactory")
	@Override
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		super.setConnectionFactory(connectionFactory);
	}
	
	@PostConstruct
	protected void init(){
		super.setMessageListener(getCustomMessageListener());
		super.setDestination(getCustomDestination());
	}
	
	/**
	 * 设置消息监听器
	 * @return
	 */
	public abstract MessageListener getCustomMessageListener();
	
	/**
	 * 设置消息源（对列或主题）
	 * @return
	 */
	public abstract Destination getCustomDestination();
	
}
