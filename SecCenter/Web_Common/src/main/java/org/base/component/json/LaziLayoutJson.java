package org.base.component.json;

import java.util.List;

/**
 * @ClassName: LaziLayoutJson
 * @Description: lazicats后台管理返回JSON
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-4-9 上午11:23:05
 * 
 * @param <T>
 */
public class LaziLayoutJson {
	/**
	 * @Fields data : 数据域
	 */
	private List<TreeMeta> data;

	public LaziLayoutJson(List<TreeMeta> data) {
		super();
		this.data = data;
	}

	public LaziLayoutJson() {
		super();
	}

	List<TreeMeta> getData() {
		return data;
	}

	void setData(List<TreeMeta> data) {
		this.data = data;
	}

}
