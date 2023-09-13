package org.sp.boardapp.model.admin;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.sp.boardapp.domain.Admin;
import org.sp.boardapp.domain.Member;
import org.sp.boardapp.mybatis.MybatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisAdminDAO implements AdminDAO{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public Member login(Member member) {
		//SqlSession sqlSession=mybatisConfig.getSqlSession();
		Member dto=sqlSessionTemplate.selectOne("Admin.login", member);
		
		//mybatisConfig.release(sqlSession);
		
		return dto;
	}
	
	@Override
	public void signup(Member member) {
		int result=sqlSessionTemplate.insert("Admin.signup", member);
		
	}

}

