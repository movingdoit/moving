package org.base.component.orm.common;
//by will
import java.util.HashMap;
import java.util.Map;

public class CritMap {
	
	private int maxSize;//获取数据条件
	
	/** 属性比较类型. */
	public enum MatchType {
		EQUAL, 			//等于
		L_LIKE, 		//左模糊查询
		R_LIKE,			//右模糊查询
		LIKE,			//全模糊查询
		GREATER,		//大于
		LESS, 			//小于
		GREATER_EQUAL,	//大于等于
		LESS_EQUAL,		//小于等于
		ORDER_ASC,		//升序
		ORDER_DESC,		//降序
		FECH,			//级联
		ALIASFECH,		//别名级联
		IN;             //
	}
	
	
	Map<String, Map<String, Object>> rootMap;

	public CritMap() {
		rootMap = new HashMap<String, Map<String, Object>>();
		rootMap.put(MatchType.EQUAL.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.L_LIKE.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.R_LIKE.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.LIKE.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.GREATER.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.GREATER_EQUAL.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.LESS.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.LESS_EQUAL.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.ORDER_ASC.name(),new HashMap<String, Object>());
		rootMap.put(MatchType.ORDER_DESC.name(),new HashMap<String, Object>());
		rootMap.put(MatchType.FECH.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.ALIASFECH.name(), new HashMap<String, Object>());
		rootMap.put(MatchType.IN.name(), new HashMap<String, Object>());
	}
	
	public void addEqual(String fieldNme, Object value) {
		if(null != value)
			(rootMap.get(MatchType.EQUAL.name())).put(fieldNme, value);
	}
	
	public void addRightLike(String fieldNme, String value) {
		if(null != value && !"".equals(value.trim())) {
			rootMap.get(MatchType.R_LIKE.name()).put(fieldNme, value);
		}
	}
	
	public void addLeftLike(String fieldNme, String value) {
		if(null != value && !"".equals(value.trim())) {
			rootMap.get(MatchType.L_LIKE.name()).put(fieldNme, value);
		}
	}
	
	public void addLike(String fieldNme, String value) {
		if(null != value && !"".equals(value.trim())) {
			rootMap.get(MatchType.LIKE.name()).put(fieldNme, value);
		}
	}
	
	public void addGreat(String fieldNme, Object value) {
		if(null != value)
			rootMap.get(MatchType.GREATER.name()).put(fieldNme, value);
	}
	
	public void addGreatAndEq(String fieldNme, Object value) {
		if(null != value)
			rootMap.get(MatchType.GREATER_EQUAL.name()).put(fieldNme, value);
	}
	
	public void addLess(String fieldNme, Object value) {
		rootMap.get(MatchType.LESS.name()).put(fieldNme, value);
	}
	
	public void addLessAndEq(String fieldNme, Object value) {
		if(null != value)
			rootMap.get(MatchType.LESS_EQUAL.name()).put(fieldNme, value);
	}
	
	public void addAsc(String fieldNme) {
		rootMap.get(MatchType.ORDER_ASC.name()).put(fieldNme, fieldNme);
	}
	
	public void addDesc(String fieldNme) {
		rootMap.get(MatchType.ORDER_DESC.name()).put(fieldNme,fieldNme);
	}
	
	public void addFech(String fechTableName) {
		rootMap.get(MatchType.FECH.name()).put(fechTableName, fechTableName);
	}
	
	public void addMaxSize(int size) {
		this.maxSize = size;
	}
	
	public int getMaxSize(){
		return this.maxSize;
	}
	
	/**
	 * 设置别名  则可进行子查询
	 * @param alias   关联对象别名
	 * @param fechTableName  关联对象名
	 */
	public void addFech(String alias ,String fechTableName) {
		rootMap.get(MatchType.ALIASFECH.name()).put(alias, fechTableName);
	}
	
	/**
	 * 设置IN  可进行多字段包含查询
	 * @param alias   关联字段
	 * @param object  字段值集合
	 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
	 * @date 2011-04-21
	 */
	public void addIN(String fieldNme ,Object object) {
		rootMap.get(MatchType.IN.name()).put(fieldNme, object);
	}
	
	public Map<String, Object> getFieldMap (String str){
		return rootMap.get(str);
	}
	
	public static void main(String[] args) {
	}
	
}
