package com.koreait.pjt.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.BoardCmtDAO;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.BoardVO;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/detail")
public class BoardDetailSer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserVO loginUser = MyUtils.getLoginUser(request);
		if (loginUser == null) {
			response.sendRedirect("/login");
			return;
		}

		String strI_board = request.getParameter("i_board");
		int i_board = MyUtils.parseStrToInt(strI_board);
		// 단독으로 조회수 올리기 방지! --- [start]
		ServletContext application = getServletContext(); // 어플리케이션 내장객체 얻어오기
		Integer readI_user = (Integer) application.getAttribute("read_" + strI_board);
		if (readI_user == null || readI_user != loginUser.getI_user()) {
			BoardDAO.addHits(i_board);
			application.setAttribute("read_" + strI_board, loginUser.getI_user());
		}
		// 단독으로 조회수 올리기 방지! --- [end]
		// 검색유형들
		String searchType = request.getParameter("searchType"); // jsp에서 name값을 얻어온다. getParameter는
		searchType = (searchType == null) ? "a" : searchType;// searchType이 null일 경우 참: "a" 거짓:searchType
		String searchText = request.getParameter("searchText");// 문자열을 반환한다.
		searchText = (searchText == null ? "" : searchText); // searchText의 변수가 null과 같을때 참:빈문자열 거짓:searchText

		BoardDomain param = new BoardDomain(); // 보드 도메인은 주로 홈페이지를 이루는 요소를 저장 세션에 접근한 사람만 사용할수있다.
		param.setI_user(loginUser.getI_user());// 로그인 한 사람
		param.setI_board(i_board); // 칼럼수
		param.setSearchType(searchType);// 검색유형
		param.setSearchText("%" + searchText + "%"); // jsp내에서 자바 언어를 실행 시키기위한 식 찾고싶은 단어.
		request.setAttribute("data", BoardDAO.selBoard(param)); // jsp로 넘길 속성의 내장객체 명과 el식으로 저장할 값
		request.setAttribute("cmtList", BoardCmtDAO.selCmtList(i_board));
//		request.setAttribute("likeList", BoardCmtDAO.selBoardLikelist(i_board));
		// 댓글들

		// 디테일 들어가도 검식에 되고 하이라이트도 적용하도록 해라.
		// 내용을 검색했을때 제목이 딸려나오는 현상을 막고
		// 검색을 했을때 검색어가 적용된 디테일창으로 들어갔을땐 검색어에 css효과를 입혀라.
		List<BoardDomain> list = BoardDAO.selBoardList(param);
		if (!"".equals(searchText) && ("a".equals(searchType) || "c".equals(searchType))) {
			for (BoardDomain item : list) {
				// 리스트를 나열함.
				String title = item.getTitle();// 타이틀 값을 가져옴
				title = title.replace(searchText, "<span class=\"highlight\">" + searchText + "</span>"); // 검색어가 나오는
																											// 문장에 하이라이트
																											// 치기
				item.setTitle(title); // 타이틀 받는다.
			}
		}
		// response.sendRedirect("/board/list");
		ViewResolver.forward("board/detail", request, response);
		// board 폴더의 detail의 파일로 접근
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
