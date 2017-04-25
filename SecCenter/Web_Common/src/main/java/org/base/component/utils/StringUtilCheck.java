package org.base.component.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验
 * 
 * @author 孙选
 * 
 */
public class StringUtilCheck {

	/**
	 * 手机号码校验
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1[35847]\\d{9}$");//^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	public static void main(String[] args) {
		System.out.println(StringUtilCheck.isMobileNO("15799066508"));
		System.out.println(RandomUtils.getRandomNumberStr(4));
	}
}
