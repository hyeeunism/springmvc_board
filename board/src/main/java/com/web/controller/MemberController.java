package com.web.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.service.MemberService;
import com.web.vo.MemberVO;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService service;
	
	@Inject
	BCryptPasswordEncoder pwdEncoder;
	
	// 회원가입폼으로 이동 GET | 회원가입 버튼 눌렀을 때 POST

	// 회원가입 Get
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void getRegister() throws Exception {
		logger.info("get register");
	}
	
	// 회원가입 Post
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String postRegister(MemberVO vo) throws Exception {
		logger.info("post register");
		int result = service.idChk(vo);
		try {
			if(result == 1) {
				return "/member/register";
			} else if (result == 0) {
				String inputPass = vo.getUserPass();
				String pwd = pwdEncoder.encode(inputPass);
				vo.setUserPass(pwd);
				
				service.register(vo);
			}
			// 존재 -> 다시 회원가입 페이지 / 존재 X -> register
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return "redirect:/";
	}
	
	// 로그인
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception {
		logger.info("post login");
		
		session.getAttribute("member");
		MemberVO login = service.login(vo); // 로그인 서비스 실행해서 login.getUerPass 가져옴
		boolean pwdMatch = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		
		if ( login != null && pwdMatch == true) {
			session.setAttribute("member", login);
		} else {
			session.setAttribute("member", null);
			rttr.addFlashAttribute("msg", false);
		}
		return "redirect:/";
		
	}
	
	// 로그아웃
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception{
		
		session.invalidate();
		
		return "redirect:/";
	}

	// 회원정보 수정
	@RequestMapping(value="/memberUpdateView", method = RequestMethod.GET)
	public String registerUpdateView() throws Exception { // 파라미터 안 받는 이유 (로그인하면 Member값들을 세션이 갖고있음)
		return "member/memberUpdateView";
	}
	
	// 회원정보 수정되면 세션 끊기고 로그인화면으로 이동. 수정된 비번으로 접속하면 로그인됨
	@RequestMapping(value="/memberUpdate", method = RequestMethod.POST)
	public String registerUpdate(MemberVO vo, HttpSession session) throws Exception {
		service.memberUpdate(vo);
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 회원 탈퇴 Get
	@RequestMapping(value="/memberDeleteView", method=RequestMethod.GET)
	public String memberDeleteView() throws Exception {
		return "member/memberDeleteView";
	}
	
	// 회원 탈퇴 Post
	@RequestMapping(value="/memberDelete", method=RequestMethod.POST)
	public String memberDelete(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception {

		service.memberDelete(vo);
		session.invalidate();

		return "redirect:/";		
	}
	
	// 패스워드 체크
	@ResponseBody
	@RequestMapping(value="/passChk", method = RequestMethod.POST)
	public int passChk(MemberVO vo) throws Exception {
		int result = service.passChk(vo);
		return result;
	}
	
	// 아이디 중복 체크(회원가입시)
	@ResponseBody
	@RequestMapping(value="/idChk", method=RequestMethod.POST)
	public int idChk(MemberVO vo) throws Exception {
		int result = service.idChk(vo);
		return result;
	}




}