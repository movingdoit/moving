package org.base.component.permission;

public enum PrivilegeType {
	
PLATFORM("PLATFORM"), SUPPLI("SUPPLI"), LOGIN("LOGIN");
	
	private final String type;
	
	private PrivilegeType(String type){
		this.type = type;
	}
	public String getType() {
		return type;
	}

}
