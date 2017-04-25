/**  
 * @Project: EC-BaseComponent
 * @Title: Transaction.java
 * @Package org.meibaobao.ecos.cache
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-19 下午04:21:59
 * @Copyright: Waker360 Software Services Co.,Ltd. All rights reserved.
 * @version V1.0  
 */
package org.base.component.cache.jedis;

import redis.clients.jedis.Client;
import redis.clients.jedis.Pipeline;


/** 
 * @ClassName: Transaction 
 * @Description: TODO
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-19 下午04:21:59
 *  
 */
public abstract class PipelineExecute extends Pipeline{
	public PipelineExecute() {
	}
	public abstract void execute() throws Exception;
	public void setClient(Client client) {
		super.setClient(client);
	}
}

