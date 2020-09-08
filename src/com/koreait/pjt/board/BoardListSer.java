package com.koreait.pjt.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.pjt.Const;
import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/list")
public class BoardListSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserVO loginUser = MyUtils.getLoginUser(request); //로그인 해서 세션을 받은 상태
		HttpSession hs = (HttpSession)request.getSession();
		if(loginUser == null) {
			response.sendRedirect("/login"); //response:응답.,sendRedirect는 페이지 주소로 이동시키는 기능
			return;
		}//세션이 null이면 로그인창으로 이동
		//list.jsp에서 name값인 searchText를 받아옴.
		String searchType = request.getParameter("searchType"); //문자열을 받는다.
		searchType = (searchType == null) ? "a" : searchType;
		
		String searchText = request.getParameter("searchText"); //getAttribute() 는 반환이 object형이다 클래스를 받아올때 쓴다.
		searchText = (searchText == null ? "": searchText);
		/*
		 *  getParameter()는 웹브라우저에서 전송받은 request영역의 값을 읽어오고
			getAttribute()의 경우 setAttribute()속성을 통한
			 설정이 없으면 무조건 null값을 리턴한다.
		 * */
		int page = MyUtils.getIntParameter(request, "page");
		page = (page == 0) ? 1:page;
		
		int recordCnt = MyUtils.getIntParameter(request,"record_cnt");
		recordCnt = (recordCnt == 0 ? 10 : recordCnt);
		
		BoardDomain param = new BoardDomain();
		param.setI_user(loginUser.getI_user());
		param.setRecord_cnt(recordCnt);
		param.setSearchType(searchType);
		param.setSearchText("%" + searchText + "%");
		int pagingCnt = BoardDAO.selPagingCnt(param);
		
		if(page > pagingCnt) {
			page = pagingCnt; //마지막 페이지 값으로 변경
		}
		request.setAttribute("searchType", searchType);
		request.setAttribute("page", page);
		System.out.println("page:"+page);
		
		int eIdx = page * recordCnt;
		int sIdx = eIdx - recordCnt;
		
		param.setsIdx(sIdx);
		param.seteIdx(eIdx);
		
		request.setAttribute("pagingCnt", pagingCnt);//request는 요청, setAttribute는 속성값을 받음.
		
		List<BoardDomain> list = BoardDAO.selBoardList(param);
		//리스트를 불러온다.
		//option의 value값으로 저장된 문자열들을 비교했을때 빈 문자거나 a나c인 경우엔 제목,내용
			if(!"".equals(searchText) && ("a".equals(searchType) || "c".equals(searchType)))
			{
				for(BoardDomain item : list) {
					//리스트를 나열함.
					String title = item.getTitle();//타이틀 값을 가져옴
					title = title.replace(searchText
							,"<span class=\"highlight\">"+searchText+"</span>"); //검색어가 나오는 문장에 하이라이트 치기
					item.setTitle(title); //적용된 검색어의 결과물이 list.jsp의 타이틀로 넘겨준다.
			}
		}
		
			request.setAttribute("list", list);
		ViewResolver.forward("board/list", request, response);
	}
}
