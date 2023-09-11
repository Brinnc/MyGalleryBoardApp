package org.sp.boardapp.model.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.sp.boardapp.domain.BoardImg;
import org.sp.boardapp.exception.BoardImgException;
import org.sp.boardapp.mybatis.MybatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisBoardImgDAO implements BoardImgDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public void insert(BoardImg boardImg) throws BoardImgException{
		//SqlSession sqlSession=mybatisConfig.getSqlSession();
		
		int result=sqlSessionTemplate.insert("BoardImg.insert", boardImg);
		//sqlSession.commit(); //DML
		//mybatisConfig.release(sqlSession);
		
		//result=0; //테스트
		
		if(result==0) {
			throw new BoardImgException("등록 실패 !");
		}
		
	}

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoardImg select(int board_img_idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(BoardImg boardImg) {
		//SqlSession sqlSession=mybatisConfig.getSqlSession();
		int result=sqlSessionTemplate.update("BoardImg.updateByBoardIdx", boardImg);
		
		//sqlSession.commit();
		//mybatisConfig.release(sqlSession);
	}

	@Override
	public void delete(int board_img_idx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deleteByBoardIdx(int board_idx) throws BoardImgException{
		//SqlSession sqlSession=mybatisConfig.getSqlSession();
		int result=sqlSessionTemplate.update("BoardImg.deleteByBoardIdx", board_idx);
		//sqlSession.commit();
		//mybatisConfig.release(sqlSession);
		
		if(result<1) { //삭제 실패 시
			throw new BoardImgException("cannot DELETE record of table named Board_img");
			
		}
		
	}

}
