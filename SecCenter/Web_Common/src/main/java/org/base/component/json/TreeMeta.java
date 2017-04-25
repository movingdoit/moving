package org.base.component.json;

/** 
 * @ClassName: TreeMeta 
 * @Description: lazicats后台管理左侧树菜单
 * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
 * @date 2013-4-9 上午11:19:47
 *  
 */
public class TreeMeta {
	/** 
	 * @Fields id : 标识
	 */ 
	private String id;
	/** 
	 * @Fields pid : 父结点ID
	 */ 
	private String pid;
	/** 
	 * @Fields text : 显示标识
	 */ 
	private String text;
	/** 
	 * @Fields href : 访问路径
	 */ 
	private String href;

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getPid() {
		return pid;
	}

	void setPid(String pid) {
		this.pid = pid;
	}

	String getText() {
		return text;
	}

	void setText(String text) {
		this.text = text;
	}

	String getHref() {
		return href;
	}

	void setHref(String href) {
		this.href = href;
	}

}
