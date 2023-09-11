package org.sp.boardapp.controller.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sp.boardapp.domain.Board;
import org.sp.boardapp.domain.BoardImg;
import org.sp.boardapp.exception.BoardException;
import org.sp.boardapp.exception.BoardImgException;
import org.sp.boardapp.exception.FileException;
import org.sp.boardapp.model.board.BoardService;
import org.sp.boardapp.util.FileManager;
import org.sp.boardapp.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

//게시판과 관련된 요청을 처리하는 하위 컨트롤러
@Controller
public class BoardController {

	// 컨트롤러가 직접 DAO를 다루게 되면, 트랜잭션 처리까지 부담한다거나
	// 모델part 업무를 너무 전문적으로 처리하게 되는데
	// 이 경우, 컨트롤러와 모델의 업무 경계가 모호해짐
	// 즉 코드의 분리가 되지 않아 추후 비슷한 업무 시 코드의 재사용성이 떨어짐
	@Autowired
	private BoardService boardService;

	/*
	 * //DI를 이용해 느슨하게 보유
	 * 
	 * @Autowired private BoardDAO boardDAO;
	 * 
	 * @Autowired private BoardImgDAO boardImgDAO;
	 */

	@Autowired
	private FileManager fileManager;

	@Autowired
	private Pager pager;

	// 게시판 목록 요청 처리
	@RequestMapping(value = "/board/list", method = RequestMethod.GET)
	public ModelAndView getList(HttpServletRequest request) {
		// 3단계)
		List boardList = boardService.selectAll();

		// 4단계) 목록 저장
		pager.init(boardList, request);

		ModelAndView mav = new ModelAndView("board/list");
		mav.addObject("boardList", boardList); // 요청 객체에 boardList 저장
		mav.addObject("pager", pager); // 요청 객체에 pager 저장->포워딩

		return mav;
	}

	// 글쓰기 폼 요청
	@RequestMapping(value = "/board/registform", method = RequestMethod.GET)
	public String getForm() {

		return "board/regist";
	}

	// 글쓰기 요청 처리
	@RequestMapping(value = "/board/regist", method = RequestMethod.POST)
	public String regist(Board board, HttpServletRequest request) {
		
		// 3단계) DB(오라클)에 게시글 등록+파일 업로드
		System.out.println("title = " + board.getTitle());
		System.out.println("writer = " + board.getWriter());
		System.out.println("content = " + board.getContent());

		// jsp의 application 내장객체는 서블릿 api에서 ServletContext
		// 따라서 이 객체를 얻기 위해 HttpSession을 얻어야 함
		ServletContext context = request.getSession().getServletContext();
		String path = context.getRealPath("/resources/data/");
		System.out.println("파일이 저장될 풀 경로는 " + path);

		MultipartFile photo = board.getPhoto();

		// 업로드 한 경우만 boardImg DTO가 메모리에 올라옴
		if (photo.isEmpty() == false) { // not empty, 즉 업로드가 된 경우
			String filename = photo.getOriginalFilename();
			String name = fileManager.save(path, filename, photo);

			BoardImg boardImg = new BoardImg(); // empty
			// boardImg.setBoard(board); // 이 시점의 DTO엔 아직 board_idx는 채워지지 않음
			boardImg.setFilename(name); // 새로 바뀐 이름으로 대체
			board.setBoardImg(boardImg);
		}

		// 서비스에서 예외 발생 시, 스프링의 컨트롤러는 예외를 감지하는 이벤트가 발생함
		// 이때 이 이벤트를 처리할 수 있는 메서드를 정의해놓고 개발자가 알맞는 에러페이지 및 메세지를 구성
		boardService.regist(board); // 게시글 등록 요청

		return "redirect:/board/list"; // 상위인 DispatcherServlet이 ViewResolver를 이용해 해석함
	}

	// 상세보기 요청 처리
	@RequestMapping(value = "/board/content", method = RequestMethod.GET)
	public ModelAndView getContent(int board_idx) {
		// 3단계) 데이터 가져오기
		Board board = boardService.select(board_idx);

		// 4단계) 데이터(레코드) 저장
		ModelAndView mav = new ModelAndView();
		mav.addObject("board", board); // ==request.setAttribute()
		mav.setViewName("board/content");

		return mav;
	}

	// 삭제 요청 처리
	@RequestMapping(value = "/board/delete", method = RequestMethod.POST)
	public String del(Board board, HttpServletRequest request) {

		// 3단계) 삭제 처리
		ServletContext context = request.getSession().getServletContext();

		if(board.getBoardImg().getFilename()!=null){
		// 서버에서의 파일 삭제 !! 단, 사진이 없는 글에서는 동작하면 안됨

		// 삭제될 파일의 전체 경로 얻기
		String path = context.getRealPath("/resources/data/" + board.getBoardImg().getFilename());
		fileManager.remove(path);

		// BoardImg boardImg = board.getBoardImg();
		// System.out.println("boardImg.getFileName()"+boardImg.getFilename());
		// fileManager.remove(path+boardImg.getFilename());

		}

		// db에서의 삭제
		boardService.delete(board.getBoard_idx());

		// 4단계) 리스트 재요청만
		return "redirect:/board/list";
	}

	// 수정 요청 처리
	@RequestMapping(value = "/board/edit", method = RequestMethod.POST)
	public ModelAndView update(Board board, HttpSession session) {
		// 3단계)

		MultipartFile photo = board.getPhoto();

		if (photo.isEmpty() == false) {

			// 새로운 파일로 업로드를 의도했다면 (기존 사진 삭제 + 새로운파일 업로드 )
			ServletContext context = session.getServletContext();
			String path = context.getRealPath("/resources/data/");

			System.out.println("파일이 저장될 풀 경로는 " + path);

			// 기존 파일 지우기
			// 삭제할 이미지가 있을 경우만 ..
			BoardImg boardImg = board.getBoardImg();
			System.out.println("boardImg.getFileName()" + boardImg.getFilename());

			if (boardImg.getFilename() != null) {
				fileManager.remove(path + boardImg.getFilename());
			}

			String filename = photo.getOriginalFilename();
			String name = fileManager.save(path, filename, photo);

			// 기존에 board 가 보유하고 있던 파일명은 새롭게 생성된 파일명으로 대체

			boardImg.setFilename(name); // 새로 바뀐 이름으로 대체
			board.setBoardImg(boardImg);
		}

		boardService.update(board);

		// 4단계)
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/content?board_idx=" + board.getBoard_idx());

		return mav;
	}

	// 어떠한 예외가 발생했을 때, 어떤 처리를 할지 아래의 메서드에서 로직 작성
	@ExceptionHandler(FileException.class)
	public ModelAndView handle(FileException e) {
		// jsp에서 에러 메세지 출력, 따라서 요청이 유지되어야 함(저장)
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); // 에러 객체 저장
		mav.setViewName("error/result");

		return mav;
	}

	// 어떠한 예외가 발생했을 때, 어떤 처리를 할지 아래의 메서드에서 로직 작성
	@ExceptionHandler(BoardException.class)
	public ModelAndView handle(BoardException e) {
		// jsp에서 에러 메세지 출력, 따라서 요청이 유지되어야 함(저장)
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); // 에러 객체 저장
		mav.setViewName("error/result");

		return mav;
	}

	// 어떠한 예외가 발생했을 때, 어떤 처리를 할지 아래의 메서드에서 로직 작성
	@ExceptionHandler(BoardImgException.class)
	public ModelAndView handle(BoardImgException e) {
		// jsp에서 에러 메세지 출력, 따라서 요청이 유지되어야 함(저장)
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); // 에러 객체 저장
		mav.setViewName("error/result");

		return mav;
	}

}
