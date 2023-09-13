package org.sp.boardapp.model.admin;

import org.sp.boardapp.domain.Admin;
import org.sp.boardapp.domain.Member;
import org.sp.boardapp.exception.AdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminDAO adminDAO;

	@Override
	public Member login(Member member) throws AdminException{
		
		Member dto=adminDAO.login(member);
		
		//로그인 실패 시 예외처리
		if(dto==null) {
			throw new AdminException("⚠️ Login Failed ! ⚠️");
		}
		
		return dto;
	}
	
	@Override
	public void signup(Member member) {
		adminDAO.signup(member);
		
	}
	
}
