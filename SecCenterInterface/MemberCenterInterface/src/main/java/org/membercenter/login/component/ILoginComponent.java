package org.membercenter.login.component;

import org.membercenter.login.model.MemberDo;

public interface ILoginComponent {

	public MemberDo findById(String id); 
}
