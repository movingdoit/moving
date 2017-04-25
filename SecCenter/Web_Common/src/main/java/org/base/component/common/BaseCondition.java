package org.base.component.common;

import java.io.Serializable;

public class BaseCondition implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public class Entry<K, V>  implements Serializable{

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private K k;
        private V v;

        public K getK() {
            return this.k;
        }

        public void setK(K k) {
            this.k = k;
        }

        public V getV() {
            return this.v;
        }

        public void setV(V v) {
            this.v = v;
        }
    }

    /**
     * 表示条件之间“或”的关系，用作 条件名。
     */
    public static final String C_OR = "_C_O";

    /**
     * 表示查询中按升序排序的属性名，用作 条件名。
     */
    public static final String C_ORDER_ASC = "_C_O_A";

    /**
     * 表示查询中按降序排序的属性名，用作 条件名。
     */
    public static final String C_ORDER_DESC = "_C_O_D";

    /**
     * 
     * @Title: getPart
     * @Description: 获取具有xx.yy格式的条件名中的xx和yy
     * @param conditionName
     *            条件名
     * @return
     * @throws
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-2-21上午11:36:33
     */
    private static String[] getPart(String conditionName) {
        String[] parts = null;

        if (null != conditionName && !"".equals(conditionName.trim())) {
            if (conditionName.indexOf(".") > 0 && conditionName.indexOf(".") < conditionName.length() - 1) {

                parts = new String[2];
                parts[0] = conditionName.substring(0, conditionName.indexOf("."));
                parts[1] = conditionName.substring(conditionName.indexOf(".") + 1);

            } else {
                parts = new String[1];
                parts[0] = conditionName;
            }
        }

        return parts;
    }

    /**
     * @Title: getClassName
     * @Description: 获取类名，如“User.userName”中的“User”
     * @param conditionName
     *            条件名
     * @return 类名
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-10上午09:19:51
     */
    public static String getClassName(String conditionName) {
        String className = null;

        String[] parts = getPart(conditionName);
        if (null != parts && 2 == parts.length) {
            className = parts[0];
        }

        return className;
    }

    /**
     * @Title: getPropName
     * @Description: 获取属性名，如“User.userName”中的“userName”
     * @param conditionName
     *            条件名，类似XX.yy。
     * @return 属性名
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-1-10上午09:19:51
     */
    public static String getPropName(String conditionName) {
        String propName = null;

        String[] parts = getPart(conditionName);
        if (null != parts) {
            if (1 == parts.length) {
                propName = parts[0];
            } else if (2 == parts.length) {
                propName = parts[1];
            }
        }

        return propName;
    }

}
