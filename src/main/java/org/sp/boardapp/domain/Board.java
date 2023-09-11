package org.sp.boardapp.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Board {
	
	private int board_idx;
	private String title;
	private String writer;
	private String regdate;
	private int hit;
	//private String filename;
	private String content;
	
	//바이너리 파일을 받을 수 있는 자료형
	MultipartFile photo;
	BoardImg boardImg;
	
}
