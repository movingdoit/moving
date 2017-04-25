package org.base.component.common;

import java.io.Serializable;
import java.util.List;

public class DalConditionValue extends ConditionValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private DalConditionType conditionType = DalConditionType.EQUAL;

    public DalConditionValue() {

    }

    public DalConditionValue(DalConditionType conditionType, Object value) throws Exception {
        this.conditionType = conditionType;
        this.putValue(value);
    }

    public DalConditionType getConditionType() {
        return this.conditionType;
    }

    public void setConditionType(DalConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public Object getValue() throws Exception {
        Object object = null;
        switch (this.conditionType) {
        /** 数字、字符、日期条件 */
        case EQUAL:
            object = this.getEqual();
            break;
        case NOT_EQUAL:
            object = this.getNotEqual();
            break;
        case LESS:
            object = this.getLess();
            break;
        case LESS_EQUAL:
            object = this.getLessEqual();
            break;
        case GREATER:
            object = this.getGreater();
            break;
        case GREATER_EQUAL:
            object = this.getGreaterEqual();
            break;
        case IN:
            object = this.getIn();
            break;
        case NOT_IN:
            object = this.getNotIn();
            break;
        case BETWEEN_AND:
            object = this.getBetweenAnd();
            break;
        case NOT_BETWEEN_AND:
            object = this.getNotBetweenAnd();
            break;
        /** 字符串条件 */
        case LIKE:
            object = this.getLike();
            break;
        case NOT_LIKE:
            object = this.getNotLike();
            break;
        case LLIKE:
            object = this.getlLike();
            break;
        case NOT_LLIKE:
            object = this.getNotLLike();
            break;
        case RLIKE:
            object = this.getrLike();
            break;
        case NOT_RLIKE:
            object = this.getNotRLike();
            break;

        /** 或条件 */
        case OR:
            object = this.getOr();
            break;
        /** 条件排序条件 */
        case ORDER_ASC:
            object = this.getOrderAsc();
            break;
        case ORDER_DESC:
            object = this.getOrderDesc();
            break;
        default:
            throw new Exception("Condition type not exist!");
        }

        return object;
    }

    public void putValue(Object value) throws Exception {
        switch (this.conditionType) {
        /** 数字、字符、日期条件与值的关系符 */
        case EQUAL:
            this.setEqual(value);
            break;
        case NOT_EQUAL:
            this.setNotEqual(value);
            break;
        case LESS:
            this.setLess(value);
            break;
        case LESS_EQUAL:
            this.setLessEqual(value);
            break;
        case GREATER:
            this.setGreater(value);
            break;
        case GREATER_EQUAL:
            this.setGreaterEqual(value);
            break;

        /** 字符串条件与值的关系符 */
        case LIKE:
            this.setLike((String) value);
            break;
        case NOT_LIKE:
            this.setNotLike((String) value);
            break;
        case LLIKE:
            this.setlLike((String) value);
            break;
        case NOT_LLIKE:
            this.setNotLLike((String) value);
            break;
        case RLIKE:
            this.setrLike((String) value);
            break;
        case NOT_RLIKE:
            this.setNotRLike((String) value);
            break;

        /** 数字、字符、日期条件与值的关系符 */
        case IN:
            this.setIn((List<Object>) value);
            break;
        case NOT_IN:
            this.setNotIn((List<Object>) value);
            break;
        case BETWEEN_AND:
            this.setBetweenAnd((Object[]) value);
            break;
        case NOT_BETWEEN_AND:
            this.setNotBetweenAnd((Object[]) value);
            break;

        /** 条件之间的关系 */
        case OR:
            this.setOr((Object[]) value);
            break;
        /** 条件排序 */
        case ORDER_ASC:
            this.setOrderAsc((List<String>) value);
            break;
        case ORDER_DESC:
            this.setOrderDesc((List<String>) value);
            break;

        default:
            throw new Exception("Condition type not exist!");
        }
    }
}
