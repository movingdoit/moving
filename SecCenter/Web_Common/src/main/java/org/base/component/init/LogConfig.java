package org.base.component.init;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;



/**
 * 
 * @ClassName: LogConfig 
 * @Description: TODO
* @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-7-5 上午10:17:33
 *
 */
public class LogConfig{

	/**
	 * 外部调用端操作日志对象
	 */
	public static Logger ecosLog=null;
	
	public static Logger exceptionLog=null;

	public static Logger filterLog=null;

	
	public static void init() {
//			PropertyConfigurator.configure(LogConfig
//					.getPropFile("log4j.properties"));
			ecosLog = Logger.getLogger("EcosLog");
			exceptionLog = Logger.getLogger("ExceptionLog");
			filterLog = Logger.getLogger("FilterLog");
			LogConfig.setEcosLog(ecosLog);
			LogConfig.setFilterLog(filterLog);
			LogConfig.setExceptionLog(exceptionLog);
			
			LogConfig.getEcosLog().debug("日志初始化完成...");
		}

	public static Properties getPropFile(String fileName) {
		// 读取配置文件
		try {
			FileInputStream in = new FileInputStream(fileName);
			Properties objProperties = new Properties();
			objProperties.load(in);
			in.close();
			return (objProperties);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Logger getEcosLog() {
		return ecosLog;
	}

	public static void setEcosLog(Logger ecosLog) {
		LogConfig.ecosLog = ecosLog;
	}

	public static Logger getExceptionLog() {
		return exceptionLog;
	}

	public static void setExceptionLog(Logger exceptionLog) {
		LogConfig.exceptionLog = exceptionLog;
	}

	public static Logger getFilterLog() {
		return filterLog;
	}

	public static void setFilterLog(Logger filterLog) {
		LogConfig.filterLog = filterLog;
	}

}
