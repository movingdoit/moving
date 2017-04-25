package org.base.component.utils;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 *
 * @param <T>
 */
public class Message<T> {
	private String code = "0";//返回的状态码
	private String msg;//返回的信息
	private T obj;//返回的对象
	private String error;
	private String djzb_error_code;//大集众包错误代码
	private String ddzb_error_code;//达达众包错误代码
	/**
	 * @return the ddzb_error_code
	 */
	public String getDdzb_error_code() {
		return ddzb_error_code;
	}
	/**
	 * @param ddzb_error_code the ddzb_error_code to set
	 */
	public void setDdzb_error_code(String ddzb_error_code) {
		this.ddzb_error_code = ddzb_error_code;
	}
	/**
	 * @return the djzb_error_code
	 */
	public String getDjzb_error_code() {
		return djzb_error_code;
	}
	/**
	 * @param djzb_error_code the djzb_error_code to set
	 */
	public void setDjzb_error_code(String djzb_error_code) {
		this.djzb_error_code = djzb_error_code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
