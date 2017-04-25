package org.base.component.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewCondition extends BaseCondition  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Entry<String, ViewConditionValue>> conditions = new ArrayList<Entry<String, ViewConditionValue>>();

    /**
     * 
     * @Title: getConditionValue
     * @Description: 取第一个条件名为conditionName的条件值对象
     * @param conditionName
     *            条件名
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-3-22下午04:33:30
     */
    public ViewConditionValue getConditionValue(String conditionName) {
        return this.getConditionValue(conditionName, null);
    }

    /**
     * 
     * @Title: getConditionValue
     * @Description: 取条件名为conditionName，且条件类型为conditionType的条件值对象
     * @param conditionName
     * @param viewConditionType
     *            条件类型，当有值时标明，将取指定conditionType的条件值对象
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-3-22下午04:38:09
     */
    public ViewConditionValue getConditionValue(String conditionName, ViewConditionType viewConditionType) {
        ViewConditionValue cvalue = null;

        for (Entry<String, ViewConditionValue> condition : this.conditions) {
            if (viewConditionType == null) {
                if (condition.getK().equals(conditionName)) {
                    cvalue = condition.getV();
                    break;
                }
            } else {
                if (condition.getV().getConditionType() == viewConditionType) {
                    if (condition.getK().equals(conditionName)) {
                        cvalue = condition.getV();
                        break;
                    }
                }
            }
        }

        return cvalue;
    }

    /**
     * 
     * @Title: putConditionValue
     * @Description: 根据条件类型，条件名，条件值（标量值或字符串值）加入一个条件。
     * @param viewConditionType
     *            条件操作类型：大于，小于，不等于 等等
     * @param conditionName
     *            条件名，格式xx.yy 或 yy(xx为类名，yy为属性名，DB层需用PO)
     * @param value
     *            条件值：分为标量值，List&lt;String&gt; List&lt;Object&gt值，Object值
     * @throws Exception
     *             传入条件值类型异常 或 查询条件类型不存在 异常。
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-2-21上午11:02:20
     */
    public void putConditionValue(ViewConditionType viewConditionType, String conditionName, Object value)
            throws Exception {
        this.putConditionValue(conditionName, new ViewConditionValue(viewConditionType, value));
    }

    /**
     * 
     * @Title: putConditionValue
     * @Description: 根据条件类型，条件名，条件值（标量值或字符串值）加入一个条件。
     * @param conditionName
     *            条件名，格式xx.yy 或 yy(xx为类名，yy为属性名，DB层需用PO)
     * @param viewConditionValue
     *            条件值（对象）
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-11上午11:46:48
     */
    public void putConditionValue(String conditionName, ViewConditionValue viewConditionValue) {
        Entry<String, ViewConditionValue> c = new Entry<String, ViewConditionValue>();
        c.setK(conditionName);
        c.setV(viewConditionValue);

        this.conditions.add(c);
    }

    public void clearCondition() {
        this.conditions.clear();
    }
}
