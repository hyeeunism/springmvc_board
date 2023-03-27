package com.web.dao;

import java.util.List;

import com.web.vo.BoardVO;

public interface BoardDAO {

	// 게시글 작성
	public void write(BoardVO boardVO) throws Exception;
	
	// 게시글 목록 조회
		public List<BoardVO> list() throws Exception;
		
	// 게시글 상세 조회
	public BoardVO read(int bno) throws Exception;
}