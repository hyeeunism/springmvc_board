package com.web.service;

import com.web.vo.MemberVO;

public interface MemberService {
	
	// 회원가입
	public void register(MemberVO vo) throws Exception;
	
	// 로그인
	public MemberVO login(MemberVO vo) throws Exception;
	
	// 회원정보 수정
	public void memberUpdate(MemberVO vo) throws Exception;
	
	// 회원 탈퇴
	public void memberDelete(MemberVO vo) throws Exception;
	
	// 패스워드 체크
	public int passChk(MemberVO vo) throws Exception;
	
	// id중복 체크(회원가입)
	public int idChk(MemberVO vo) throws Exception;

}
