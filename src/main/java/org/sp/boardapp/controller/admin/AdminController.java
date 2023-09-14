package org.sp.boardapp.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sp.boardapp.domain.Admin;
import org.sp.boardapp.domain.Member;
import org.sp.boardapp.exception.AdminException;
import org.sp.boardapp.model.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	// 로그인 폼 요청
	@GetMapping("/loginform")
	public ModelAndView loginform() {
		ModelAndView mav = new ModelAndView("admin/loginform");

		return mav;
	}
	
	// ID찾기 폼 요청
	@GetMapping("/forgetform")
	public ModelAndView forgetform() {
		ModelAndView mav=new ModelAndView("admin/forgetform");
		
		return mav;
	}
	
	// ID 확인 페이지 요청
	@PostMapping("/findID")
	public ModelAndView Find(Member member, HttpServletRequest request) {
		//ModelAndView mav=new ModelAndView("admin/findID");
		
		// 3단계) 일치하는 계정 정보가 있는지 조회
		Member dto = adminService.findID(member);

		// 4단계) 일치하는 정보가 있다면, 세션에 DTO를 담아 서비스
		HttpSession session = request.getSession();
		session.setAttribute("member", dto);

		// ModelAndView mav=new ModelAndView("admin/index"); //포워딩 시
		ModelAndView mav=new ModelAndView("admin/findID"); // redirect 시
		
		return mav;
	}
	
	// 회원 가입 폼 요청
	@GetMapping("/signupform")
	public ModelAndView signupform() {
		ModelAndView mav=new ModelAndView("admin/signupform");
		
		return mav;
	}
	
	// 회원 가입 약관 페이지 요청
	@GetMapping("/agree")
	public ModelAndView getAgree() {
		ModelAndView mav=new ModelAndView("admin/agree");
		
		return mav;
	}
	
	// 회원 가입 요청 처리
	@PostMapping("/signup")
	public String signUp(Member member, HttpServletRequest request) {
		
		adminService.signup(member);
		
		return "redirect:/admin/loginform";
	}

	// 관리자 메인 요청 처리
	@GetMapping("/main")
	public ModelAndView getMain() {
		ModelAndView mav = new ModelAndView("admin/main");

		return mav;
	}

	// 로그인 요청 처리
	@PostMapping("/login")
	public ModelAndView login(Member member, HttpServletRequest request) {
		// 3단계) 일치하는 계정 정보가 있는지 조회
		Member dto = adminService.login(member);

		// 4단계) 일치하는 정보가 있다면, 세션에 DTO를 담아 서비스
		HttpSession session = request.getSession();
		session.setAttribute("member", dto);

		// ModelAndView mav=new ModelAndView("admin/index"); //포워딩 시
		ModelAndView mav = new ModelAndView("redirect:/board/list"); // redirect 시

		return mav;
	}

	/*
	// 카카오 로그인 요청 처리
	// 1) 카카오톡에 사용자 코드 받기 (jsp의 a태그 href에 경로 있음)
	@RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
	public ModelAndView kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Throwable {

		// 1번
		System.out.println("code:" + code);
		return null;
		// return에 페이지를 해도 되고, 여기서는 코드가 넘어오는지만 확인할거기 때문에 따로 return 값을 두지는 않았음

	}
	*/

	@ExceptionHandler(AdminException.class)
	public ModelAndView handle(AdminException e) {
		ModelAndView mav = new ModelAndView("error/result");
		mav.addObject("e", e);

		return mav;
	}
}
