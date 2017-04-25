package org.membercenter.login.model;

import java.io.Serializable;

public class MemberDo implements Serializable{

	private static final long serialVersionUID = -6076595472640139853L;

	private String name ;
	
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
