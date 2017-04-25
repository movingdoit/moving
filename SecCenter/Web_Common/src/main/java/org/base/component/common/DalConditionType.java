package org.base.component.common;

import java.io.Serializable;

public enum DalConditionType  implements Serializable{

	/** 数字与字符串条件：相等 */
	EQUAL,
	/** 数字与字符串条件：不相等 */
	NOT_EQUAL,
	/** 数字与字符串条件：等于多值 */
	IN,
	/** 数字与字符串条件：不等于任意值 */
	NOT_IN,
	/** 数字、字符、日期条件：小于 */
	LESS,
	/** 数字、字符、日期条件：小于 等于 */
	LESS_EQUAL,
	/** 数字、字符、日期条件：大于 */
	GREATER,
	/** 数字、字符、日期条件：大于 等于 */
	GREATER_EQUAL,
	/** 数字、字符、日期条件：在范围内 */
	BETWEEN_AND,
	/** 数字、字符、日期条件：不在范围内 */
	NOT_BETWEEN_AND,
	/** 数字、字符、日期串条件：包含字符串 */
	LIKE,
	/** 字符串条件：不包含字符串 */
	NOT_LIKE,
	/** 字符串条件：左边包含字符串 */
	LLIKE,
	/** 字符串条件：左边不包含字符串 */
	NOT_LLIKE,
	/** 字符串条件：右边包含字符串 */
	RLIKE,
	/** 字符串条件：右边不包含字符串 */
	NOT_RLIKE,
	/** 升序排序 */
	ORDER_ASC,
	/** 降序排序 */
	ORDER_DESC,
	/** 或 */
	OR

}
