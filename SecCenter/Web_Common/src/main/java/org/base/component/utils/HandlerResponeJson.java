package org.base.component.utils;
/**
 * 跨域请求状态：因为直接返回json的字符串后，前台会报缺少冒号的错误。
 * 网上搜索到要添加callback
 * （2014-7-26）需要从前台接收callback参数的名字，再把这个名字与json格式的数据写到前台
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 *
 */
public class HandlerResponeJson {
	public static String handle(String func,String data){
		if(func == null){
			return data;
		}
		return func + "(" + data + ")";
	}
}
