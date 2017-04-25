package org.base.component.exception;

import java.util.ResourceBundle;

/**
 * 
 * @ClassName: BusinessException 
 * @Description: 自定义业务异常处理类 友好提示
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-4-7 下午2:18:01
 *
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BusinessException(String key, ResourceBundle resourceBundle) {
		super(createFriendlyErrMsg(resourceBundle.getString(key)+"！", resourceBundle));
	}

	public BusinessException(Throwable throwable) {
		super(throwable);
	}

	public BusinessException(Throwable throwable, String frdMessage) {
		super(throwable);
	}

	private static String createFriendlyErrMsg(String msgBody, ResourceBundle resourceBundle) {
		
		String prefixStr = resourceBundle.getString("sorry")+"，";
		String suffixStr = resourceBundle.getString("contact.administrator")+"！";
		
		StringBuffer friendlyErrMsg = new StringBuffer("");

		friendlyErrMsg.append(prefixStr);

		friendlyErrMsg.append(msgBody);

		friendlyErrMsg.append(suffixStr);

		return friendlyErrMsg.toString();
	}
}
