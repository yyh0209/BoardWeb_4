package com.koreait.pjt.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreait.pjt.MyUtils;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;

/**
 * Servlet implementation class BoardLikeListser
 */
@WebServlet("/board/like")
public class BoardLikeListser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 리스트 가져옴
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int i_board = MyUtils.getIntParameter(request, "i_board");
		System.out.println("i_board : " + i_board);

		List<BoardDomain> likeList = BoardDAO.selBoardLikelist(i_board);

		Gson gson = new Gson();

		String json = gson.toJson(likeList);
		response.setCharacterEncoding("UTF-8"); // 한글이 안깨지는 용도.
		response.setContentType("application/json"); // 웹 페이지에서 보낼때 제이슨이라고 인식하게 만든다.
		PrintWriter out = response.getWriter();
		out.print(json); // 제이슨식 선언."{\"name\"}: 1" 감싸는건 쌍따옴표.
	}

	// 리스트에서 좋아요를 처리함
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
