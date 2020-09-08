package com.koreait.pjt.board;

import java.io.IOException;

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
import com.koreait.pjt.vo.BoardVO;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/regmod")
public class BoardRegmodSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//화면 띄우는 용도(등록창/수정창)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		UserVO loginUser = MyUtils.getLoginUser(request);
		if(loginUser == null) {
			response.sendRedirect("/login");
			return;
		}
		BoardVO param = new BoardVO();
		param.setI_user(loginUser.getI_user());
		
		String strI_board = request.getParameter("i_board");
		if(strI_board != null) { //수정
			int i_board = MyUtils.parseStrToInt(strI_board);
			param.setI_board(i_board);
			request.setAttribute("data", BoardDAO.selBoard(param));
		}
		
		ViewResolver.forward("board/regmod", request, response);
	}

	//처리 용도(DB에 등록/수정)실시
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strI_board = request.getParameter("i_board");
		String title = request.getParameter("title");
		String ctnt = request.getParameter("ctnt");
		
		HttpSession hs = request.getSession();
		UserVO loginUser = (UserVO) hs.getAttribute(Const.LOGIN_USER);
		
		System.out.println("title : " + title);
		System.out.println("ctnt : " + ctnt);
		
		String filterCtnt1=scriptFilter(ctnt);
		String filterCtnt2=swearWordFilter(filterCtnt1);
		
		BoardVO param = new BoardVO();
		param.setTitle(title);
		param.setCtnt(filterCtnt2);
		param.setI_user(loginUser.getI_user());
		int result = 0;
		
		if("".equals(strI_board)) { //등록
			result = BoardDAO.insBoard(param);
			response.sendRedirect("/board/list");
		} else { //수정
			int i_board = MyUtils.parseStrToInt(strI_board);
			param.setI_board(i_board);
			result = BoardDAO.updBoard(param);
			response.sendRedirect("/board/detail?i_board=" + strI_board);
		}
		
	}
	//욕 필터
	private String swearWordFilter(final String ctnt) {
		String[] filters = {"개새끼","미친년","ㄱㅐㅅㅐㄲㅣ"}; //많은 문장중 특정 문장을 자바스크립트의 문자오가 끝
		String result = ctnt;
		for(int i=0;i<filters.length;i++) {
			result=result.replace(filters[i], "***");
		}
		return result;
	}
	//스크립트 필터
	private String scriptFilter(String ctnt) {
		String[] filter = {"<script>","</script>"}; //많은 문장중 특정 문장을 자바스크립트의 문자오가 끝
		String[] filterReplaces = {"&lt;script&gt;","&lt;/script&gt"}; //스크립트를 무력화 시킴.
		
		String result = ctnt;
		for(int i=0;i<filter.length;i++) {
			result = result.replace(filter[i],filterReplaces[i]); //비파괴형 자기건 파괴되지 않음.
		}
		return result; //바꾼거를 리턴해줌
	}
}
