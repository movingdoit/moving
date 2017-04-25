package org.base.component.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	
	/**
	 * 私有构造函数，不允许本类生成实例 
	 */
	private Md5Utils(){
		
	}
	
	private static final String sv = "0plo98ikmju76yhnbgt54rfvcde32wsxzaq1";

	/**
	 * 用MD5算法进行加密
	 * @param pStrPW
	 * @return
	 */
	public static String crypt(String pStrPW) {
		String strCrypt = hash(pStrPW);
		if(strCrypt.length() > 0) {
			strCrypt += sv;
			strCrypt = hash(strCrypt);
		}
		
		return strCrypt;
	}
	/**
	 * 用MD5算法进行加密(16位)
	 * @param strPW
	 * @return
	 */
	public static String encryptMD5(String strPW) {
		String strCrypt = hash(strPW);
		if(strCrypt.length() > 0) {
			strCrypt += sv;
			strCrypt = hash(strCrypt);
		}
		
		return strCrypt.substring(8,24);
	}
	
	
	/**
	 * MD5算法进行散列
	 * @param str
	 * @return
	 */
	public static String hash(String str) {
		String result = "";
		if (str == null || str.equals("")) { // 如果传入参数为空，则返回空字符串
			return result;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] data = str.getBytes();
			int l = data.length;
			for (int i = 0; i < l; i++)
				md.update(data[i]);
			byte[] digest = md.digest();
			
			result = ByteUtils.byteArrayToHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(),e);
		}

		return result;		
	}

	public static void main(String args[]) {
		String pwd = "123456";
		if (args != null && args.length > 0) {
			pwd = args[0];
		}
		
		try {
			System.out.println(Md5Utils.hash(pwd));
			System.out.println(Md5Utils.crypt(pwd));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
