package com.koreait.pjt.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/toggleLike")
public class BoardToggleLike extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//좋아요 누름
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String strI_board = request.getParameter("i_board"); //넘어오는 값이 키값이면 겟방식. 호출해서 명령을 내리는건 아규먼트
	String strYn_like = request.getParameter("yn_like"); //=붙이고 변수명이 string이면 오른쪽의 값을 복사해 왼쪽에 준다면 문자열타입이다. 
	String page = request.getParameter("page");
	String record_cnt = request.getParameter("record_cnt");
	String searchText = request.getParameter("searchText");
	String searchType = request.getParameter("searchType");
	
	searchText = URLEncoder.encode(searchText, "UTF-8");
	
	UserVO loginUser = MyUtils.getLoginUser(request);
	
	int i_board = MyUtils.parseStrToInt(strI_board);
	int yn_like = MyUtils.parseStrToInt(strYn_like);
	
	BoardDomain param = new BoardDomain();
	param.setI_board(i_board); //로그인 정보는 세션에 있다. 
	param.setI_user(loginUser.getI_user());
	//get방식으로 보낸다. location.href가 나오면 겟 방식
	if(yn_like == 0) {
		BoardDAO.insBoardLike(param);	
	}else if(yn_like == 1) {
		BoardDAO.delBoardLike(param);
		}
	
	String target = String.format("/board/detail?i_board=%s&page=%s&record_cnt=%s&searchText=%s&searchType=%s",strI_board, page, record_cnt, searchText, searchType);
	
	response.sendRedirect(target);
	}
}
