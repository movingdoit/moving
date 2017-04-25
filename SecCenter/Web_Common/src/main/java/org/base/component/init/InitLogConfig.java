package org.base.component.init;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
/**
 * 初始化日志配置
 * @author wangbihong
 *
 */

public class InitLogConfig  implements InitializingBean{
	private Logger log=Logger.getLogger(InitLogConfig.class);
	private String path;
	@Override
	public void afterPropertiesSet() throws Exception {
		PropertyConfigurator.configure(getResourcePath()+"/../"+path);
		log.info("===========================开始日志");
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取资源文件的路径路径
	 * @return
	 */
	public static String getResourcePath(){
			String path = InitLogConfig.class.getProtectionDomain().getCodeSource().getLocation().getFile().toString();
			if(path.indexOf(".jar")>=0){
				return path.substring(0, path.lastIndexOf("/"));
			}else{
				return path;
			}
	}
	
}
