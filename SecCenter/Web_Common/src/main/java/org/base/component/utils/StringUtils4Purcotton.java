/**  
 * @Project: EC-BaseComponent
 * @Title: StringUtils.java
 * @Package org.meibaobao.ecos.utils
 * @Description: 字符串工具类，继承自Sping的StringUtils，另外加上其他需要的方法
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-24 下午05:01:57
 * @Copyright: Waker360 Software Services Co.,Ltd. All rights reserved.
 * @version V1.0  
 */
package org.base.component.utils;


/**
 * @ClassName: StringUtils
 * @Description: 字符串工具类，继承自apache的StringUtils，另外加上其他需要的方法
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2012-12-24 下午05:01:57
 */

public final class StringUtils4Purcotton extends org.apache.commons.lang.StringUtils {

	
	/**
	 * 判断字符串是否存在
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isExist(String str) {
		if (str != null && str.trim().length() > 0) {
			return true;
		}
		return false;
	}
}
