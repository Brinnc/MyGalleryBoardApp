package org.sp.boardapp.model.admin;

import org.sp.boardapp.domain.Admin;
import org.sp.boardapp.domain.Member;

public interface AdminDAO {
	
	public Member login(Member member);
	public void signup(Member member);
	public Member findID(Member member);

}
