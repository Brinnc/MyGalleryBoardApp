package org.sp.boardapp.model.board;

import java.util.List;

import org.sp.boardapp.domain.Board;

public interface BoardDAO {
	
	public void insert(Board board);
	public List selectAll();
	public Board select(int board_idx);
	public void update(Board board);
	public void delete(int board_idx);
	
	public void updateHit(int board_idx); //조회수 구현 메서드 추가

}
