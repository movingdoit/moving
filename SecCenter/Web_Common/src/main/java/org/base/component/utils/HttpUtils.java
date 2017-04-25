/**
 * 
 */
package org.base.component.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP协议访问
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 *
 */
public class HttpUtils {
	/**
	 * GET方式访问网页，并返回内容
	 * @param getURL 网页链接
	 * @param encode 网页编码
	 * @return
	 */
	public static String getContent(String getURL, String encode){
        HttpURLConnection connection =null;
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
        	URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        connection = (HttpURLConnection) getUrl
                .openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),encode));//设置编码,否则中文乱码
        String lines;
        while ((lines = reader.readLine()) != null){
        	sb.append(lines);
        }
       
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 断开连接
			connection.disconnect();
		}
        return sb.toString();
	}
	
	public static void main(String[] args) {
		String Url="http://api.cnsms.cn/?ac=send&uid=100969&pwd=6B2244ECF5881E4AA6D4235D16BE7B48&mobile=13632524871&content=%D7%F0%BE%B4%B5%C4%CE%A2%B5%C3%BF%CD%BB%E1%D4%B1%A3%AC%B8%D0%D0%BB%C4%FA%B5%C4%D7%A2%B2%E1%21%CF%B5%CD%B3%D4%F9%CB%CD%C4%FA%C3%E6%D6%B55.0%D4%AA%D3%C5%BB%DD%C8%AF%A3%AC%D3%C5%BB%DD%C8%AF%BA%C5%C2%EB%3AALK6800A2N200672EE%2C%C7%EB%D3%DA2015%C4%EA11%D4%C228%C8%D5%D6%AE%C7%B0%CA%B9%D3%C3%2C%D7%A3%C4%FA%C9%FA%BB%EE%D3%E4%BF%EC%21";
		System.out.println(HttpUtils.getContent(Url,"UTF-8"));
	}
}
