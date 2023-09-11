package org.sp.boardapp.model.admin;

import org.sp.boardapp.domain.Admin;

public interface AdminDAO {
	
	public Admin login(Admin admin);
	public void signup(Admin admin);

}
