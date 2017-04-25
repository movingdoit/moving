package org.base.component.permission;

public final class PermissionConstant {

	
	public static final String spit_dot = ",";
	
	
	/**
	 * 系统用户密码错误
	 */
	public static final String SYSTEM_USER_PASSWORD_NOT_CORRECT = "wrongpassword";
	/**
	 * 登录用户
	 */
	public static final String LOGIN_SYSTEM_USER = "login_system_user";
	/**
	 * 登录用户
	 */
	public static final String LOGIN_USER = "login_user";
	/**
	 * 供应商登录用户
	 */
	public static final String LOGIN_SUPPLIER_USER = "login_supplier";
	/**
	 * 系统登陆的用户名
	 */
	public static final String LOGIN_USERNAME = "login_username";
	/**
	 * 用户资源列表
	 */
	public static final String USER_RES_LIST = "resList";
	
	/**
	 * 用户资源URL列表
	 */
	public static final String USER_RES_URL_LIST = "resUrlList";
	
	/**
	 * 用户资源数据集合
	 */
	public static final String USER_RES_MAP = "resMap";

	/**
	 * 超级管理员
	 */
	public static final int SYSTEM_USER_NATURE1 = 1;
	/**
	 * 普通用户
	 */
	public static final int SYSTEM_USER_NATURE0 = 0;
	/**
	 * 用户名或密码不正确
	 */
	public static final String LOGINUSER_OR_PASSWORD_NOT_CORRECT = "您输入的用户名或密码不正确！ ";
	/**
	 * 该用户已被锁定
	 */
	public static final String LOGINUSER_HASLOCKED = "该用户已被锁定 ，请联系管理员解锁！";
	/**
	 * 该用户已被删除
	 */
	public static final String LOGINUSER_HASDELETED = "无此账户或此账户已被管理员删除！";
	
	/**
	 * 该用户没任何权限
	 */
	public static final String LOGINUSER_NO_ONE_PERMISSION= "该用户没有任何资源访问权限！";
	
	/**
	 * 该用户没URL访问权限
	 */
	public static final String LOGINUSER_NO_URL_PERMISSION= "您没有该资源访问权限！";
	
	/**
	 * 该用户已存在
	 */
	public static final String USER_ALREADY_EXITS = "useralreadyexits";
	
	/**
	 * 权限
	 */
	public static final String SYSTEM_PRIVILEGE = "system_privilege";
//	public static final String PRIVILEGE_PLATFORM = "PLATFORM";
//	public static final String PRIVILEGE_SUPPLI = "SUPPLI";
	/**
	 * 已登陆
	 */
	public static final String PRIVILEGE_LOGIN = "LOGIN";

}
