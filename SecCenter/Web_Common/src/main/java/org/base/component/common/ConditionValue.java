package org.base.component.common;

import java.io.Serializable;
import java.util.List;

public class ConditionValue implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 数字、字符串、日期条件 */
    private Object equal = null; // 单个结果等于单个值
    private Object notEqual = null; // 单个结果等于条件单个值
    private Object less = null; // 单个结果小于同一条件的单个值
    private Object lessEqual = null; // 单个结果小于等于同一条件的单个值
    private Object greater = null; // 单个结果大于同一条件的单个值
    private Object greaterEqual = null; // 单个结果大于等于同一条件的单个值

    private List<Object> in = null; // 单个结果等于同一条件给定的任意值
    private List<Object> notIn = null; // 单个结果不等于同一条件给定的任意值
    private Object[] betweenAnd = null; // 单个结果在同一条件给定的范围内
    private Object[] notBetweenAnd = null; // 单个结果不在同一条件给定的范围内

    /** 字符串条件 */
    private String like = null; // 单个结果中包含同一条件单个值
    private String notLike = null; // 单个结果中不包含同一条件单个值
    private String lLike = null; // 单个结果左侧包含同一条件单个值
    private String notLLike = null; // 单个结果左侧包含同一条件单个值
    private String rLike = null; // 单个结果右侧侧包含同一条件单个值
    private String notRLike = null; // 单个结果右侧侧包含同一条件单个

    /** 排序条件 */
    private List<String> orderAsc = null; // 单个结果按多个条件的单个值升序排序
    private List<String> orderDesc = null; // 单个结果按多个条件的单个值降序排序

    /** 条件之间的关系 */
    private Object[] or = null;// 条件之间为或的关系

    public Object getEqual() {
        return this.equal;
    }

    public void setEqual(Object equal) {
        this.equal = equal;
    }

    public Object getNotEqual() {
        return this.notEqual;
    }

    public void setNotEqual(Object notEqual) {
        this.notEqual = notEqual;
    }

    public Object getLess() {
        return this.less;
    }

    public void setLess(Object less) {
        this.less = less;
    }

    public Object getLessEqual() {
        return this.lessEqual;
    }

    public void setLessEqual(Object lessEqual) {
        this.lessEqual = lessEqual;
    }

    public Object getGreater() {
        return this.greater;
    }

    public void setGreater(Object greater) {
        this.greater = greater;
    }

    public Object getGreaterEqual() {
        return this.greaterEqual;
    }

    public void setGreaterEqual(Object greaterEqual) {
        this.greaterEqual = greaterEqual;
    }

    public List<Object> getIn() {
        return this.in;
    }

    public void setIn(List<Object> in) {
        this.in = in;
    }

    public List<Object> getNotIn() {
        return this.notIn;
    }

    public void setNotIn(List<Object> notIn) {
        this.notIn = notIn;
    }

    public Object[] getBetweenAnd() {
        return this.betweenAnd;
    }

    public void setBetweenAnd(Object[] betweenAnd) {
        this.betweenAnd = betweenAnd;
    }

    public Object[] getNotBetweenAnd() {
        return this.notBetweenAnd;
    }

    public void setNotBetweenAnd(Object[] notBetweenAnd) {
        this.notBetweenAnd = notBetweenAnd;
    }

    public String getLike() {
        return this.like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getNotLike() {
        return this.notLike;
    }

    public void setNotLike(String notLike) {
        this.notLike = notLike;
    }

    public String getlLike() {
        return this.lLike;
    }

    public void setlLike(String lLike) {
        this.lLike = lLike;
    }

    public String getNotLLike() {
        return this.notLLike;
    }

    public void setNotLLike(String notLLike) {
        this.notLLike = notLLike;
    }

    public String getrLike() {
        return this.rLike;
    }

    public void setrLike(String rLike) {
        this.rLike = rLike;
    }

    public String getNotRLike() {
        return this.notRLike;
    }

    public void setNotRLike(String notRLike) {
        this.notRLike = notRLike;
    }

    public List<String> getOrderAsc() {
        return this.orderAsc;
    }

    public void setOrderAsc(List<String> orderAsc) {
        this.orderAsc = orderAsc;
    }

    public List<String> getOrderDesc() {
        return this.orderDesc;
    }

    public void setOrderDesc(List<String> orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Object[] getOr() {
        return this.or;
    }

    public void setOr(Object[] or) {
        this.or = or;
    }

}
