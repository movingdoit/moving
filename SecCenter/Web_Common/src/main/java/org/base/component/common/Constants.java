package org.base.component.common;

public class Constants {

	public static final int ZERO=0;
	public static final String EMPTY_STRING="";
	public static final String DOT=".";
	public static final String POUND_SIGN="#";
	public static final String COMMA=",";
	public static final String MIDDLE_LINE="-";

	/** 字符串查询条件操作类型. */
	public enum StringMatchType {
		EQUAL, 			//结果等于条件值
		EQUAL_LIST,		//结果全等于条件值
		NOT_EQUAL_LIST,	//结果不等于任意条件值
		LIKE,			//结果中包含条件值
		LIKE_LIST,		//结果中包含多个条件值
		NOT_LIKE,		//结果中不包含条件值
		NOT_LIKE_LIST,	//结果中不包含多个条件值
		L_LIKE, 		//结果左侧包含条件值
		R_LIKE,			//结果左侧包含条件值
		ORDER_ASC,		//结果升序排序
		ORDER_DESC,		//结果降序排序
	}

	/** 数字查询条件操作类型 */
	public enum DigitMatchType {
		EQUAL, 			//结果等于条件值
		EQUAL_LIST,		//结果全等于条件值
		NOT_EQUAL_LIST,	//结果不等于任意条件值
		GREATER,		//大于
		GREATER_EQUAL,	//大于等于
		LESS, 			//小于
		LESS_EQUAL,		//小于等于
		ORDER_ASC,		//升序
		ORDER_DESC		//降序
	}
	
	/**
	 * 渠道编码
	 */
	public static final String CHANNEL_JD = "jd";  		//京东
	public static final String CHANNEL_TB = "taobao";	//淘宝
	public static final String CHANNEL_YHD = "yhd";		//一号店
	public static final String CHANNEL_PP = "paipai";	//拍拍
	public static final String CHANNEL_GWSC = "gwsc";	//官网商城
	public static final String CHANNEL_DD = "dangdang";	//当当
}
