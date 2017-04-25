package org.base.component.common;

/**
 * 返回值常量:  0失败, 1成功  -1非法参数,无效参数   －2数据不存在    －3禁止访问   －4禁止更新   －5禁止删除
 */
public class ReturnCodeConstants {

	/**失败*/
	public static final int FAILURE = 0;
	/**成功*/
	public static final int SUCCESSS = 1;
	/**非法参数,无效参数*/
	public static final int ILLEGAL_ARGUMENT = -1;
	/**数据不存在*/
	public static final int DATA_NOT_FOUND = -2;
	/**禁止访问*/
	public static final int FORBIDDEN_ACCESS = -3;
	/**禁止更新*/
	public static final int FORBIDDEN_UPDATE = -4;
	/**禁止删除*/
	public static final int FORBIDDEN_DELETE = -5;
	
}
