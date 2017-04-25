
package org.base.component.common;

import java.io.Serializable;

/**     
 * Copyright (c) Waker360 SOFTWARE INFRASTRUCTURE ,LTD. 
 * @createDate: 2013-10-30 上午11:44:07
* @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @version 1.0
 */
public class Result<T> implements Serializable{
	
	
	private static final long serialVersionUID = -1637587105458276599L;

	private boolean success;
	
	private String message;
	
	private String code;
	
	private T data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public static Result error(String code,String message){
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(code);
		result.setMessage(message);
		return result;
	}
	public static Result ok(Object obj){
		Result<Object>  result = new Result<Object>();
		result.setSuccess(true);
		result.setData(obj);
		return result;
	}
	
	
}
