package org.membercenter.login.component;

import org.membercenter.login.model.MemberDo;
import org.springframework.stereotype.Service;

@Service
public class LoginComponentImpl implements ILoginComponent{

	public MemberDo findById(String id) {
		MemberDo memberDo = new MemberDo();
		memberDo.setId("213123123");
		memberDo.setName("name");
		return null;
	}

	
}
