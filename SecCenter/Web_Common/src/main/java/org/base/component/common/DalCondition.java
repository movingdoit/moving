package org.base.component.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.base.component.utils.LogicUtil;

public class DalCondition extends BaseCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Entry<String, DalConditionValue>> conditions = new ArrayList<Entry<String, DalConditionValue>>();

	public List<Entry<String, DalConditionValue>> getConditionValues() {
		return this.conditions;
	}

	/**
	 * @Title: getConditionValue
	 * @Description: 取第一个条件名为conditionName的条件值对象
	 * @param conditionName
	 *        条件名
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-3-22下午04:33:30
	 */
	public DalConditionValue getConditionValue(String conditionName) {
		return this.getConditionValue(conditionName, null);
	}

	/**
	 * @Title: getConditionValue
	 * @Description: 取条件名为conditionName，且条件类型为conditionType的条件值对象
	 * @param conditionName
	 * @param conditionType
	 *        条件类型，当有值时标明，将取指定conditionType的条件值对象
	 * @return
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-3-22下午04:38:09
	 */
	public DalConditionValue getConditionValue(String conditionName, DalConditionType dalConditionType) {
		DalConditionValue cvalue = null;

		for (Entry<String, DalConditionValue> condition : this.conditions) {
			if (dalConditionType == null) {
				if (condition.getK().equals(conditionName)) {
					cvalue = condition.getV();
					break;
				}
			} else {
				if (condition.getV().getConditionType() == dalConditionType) {
					if (condition.getK().equals(conditionName)) {
						cvalue = condition.getV();
						break;
					}
				}
			}
		}

		return cvalue;
	}

	public static DalConditionValue convert(ViewConditionValue viewConditionValue) {
		DalConditionValue dcValue = new DalConditionValue();
		dcValue.setConditionType(convert(viewConditionValue.getConditionType()));

		dcValue.setBetweenAnd(viewConditionValue.getBetweenAnd());
		dcValue.setEqual(viewConditionValue.getEqual());
		dcValue.setGreater(viewConditionValue.getGreater());
		dcValue.setGreaterEqual(viewConditionValue.getGreaterEqual());
		dcValue.setIn(viewConditionValue.getIn());
		dcValue.setLess(viewConditionValue.getLess());
		dcValue.setLessEqual(viewConditionValue.getLessEqual());
		dcValue.setLike(viewConditionValue.getLike());
		dcValue.setlLike(viewConditionValue.getlLike());
		dcValue.setrLike(viewConditionValue.getrLike());
		dcValue.setNotBetweenAnd(viewConditionValue.getNotBetweenAnd());
		dcValue.setNotEqual(viewConditionValue.getNotEqual());
		dcValue.setNotIn(viewConditionValue.getNotIn());
		dcValue.setNotLike(viewConditionValue.getNotLike());
		dcValue.setNotLLike(viewConditionValue.getNotLLike());
		dcValue.setNotRLike(viewConditionValue.getNotRLike());
		dcValue.setOr(convertOr(viewConditionValue.getOr()));
		dcValue.setOrderAsc(viewConditionValue.getOrderAsc());
		dcValue.setOrderDesc(viewConditionValue.getOrderDesc());

		return dcValue;
	}

	// 设置or条件
	private static Object[] convertOr(Object[] or) {
		if (LogicUtil.isNotNull(or) && or.length > 0) {
			or[0] = convert((ViewConditionType) or[0]);
		}
		return or;
	}

	private static DalConditionType convert(ViewConditionType viewConditionType) {
		return DalConditionType.valueOf(viewConditionType.name());
	}

	/**
	 * @Title: putConditionValue
	 * @Description: 根据条件类型，条件名，条件值（标量值或字符串值）加入一个条件。
	 * @param dalConditionType
	 *        条件操作类型：大于，小于，不等于 等等
	 * @param conditionName
	 *        条件名，格式xx.yy 或 yy(xx为类名，yy为属性名，DB层需用PO),dalConditionType 当为排序，则该字段为空
	 * @param value
	 *        条件值：分为标量值，List&lt;String&gt; List&lt;Object&gt值，Object值
	 * @throws Exception
	 *         传入条件值类型异常 或 查询条件类型不存在 异常。
	 * @throws
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-2-21上午11:02:20
	 */
	public void putConditionValue(DalConditionType dalConditionType, String conditionName, Object value)
			throws Exception {

		this.putConditionValue(conditionName, new DalConditionValue(dalConditionType, value));
	}

	/**
	 * @Title: putConditionValue
	 * @Description: 根据条件类型，条件名，条件值（标量值或字符串值）加入一个条件。
	 * @param conditionName
	 *        条件名，格式xx.yy 或 yy(xx为类名，yy为属性名，DB层需用PO)
	 * @param dalConditionValue
	 *        条件值（对象）
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date: 2013-1-11上午11:46:48
	 */
	public void putConditionValue(String name, DalConditionValue dalConditionValue) {

		Entry<String, DalConditionValue> c = new Entry<String, DalConditionValue>();
		c.setK(name);
		c.setV(dalConditionValue);

		this.conditions.add(c);
	}

	public void clearCondition() {
		this.conditions.clear();
	}

}
