package com.koreait.pjt.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.UserDAO;
import com.koreait.pjt.vo.UserVO;


@WebServlet("/changePw")
public class ChangePwSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("isAuth",true); //인증
		ViewResolver.forwardLoginChk("user/changePw", request, response); //user 폴더의 changepw 파일로 보냄.
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	UserVO loginUser = MyUtils.getLoginUser(request);//주소값이기 때문에 세션에 주소값이 바꿔버림.
	//로그인에 성공한 유저
	String type = request.getParameter("type"); //포스트로 전달된 input의 속성중 name의 이름
	String user_id = request.getParameter("user_id");// 비밀번호 변경은 id 값을 받아와야함.
	String pw = request.getParameter("pw"); //input의 속성 name의 이름이 pw를 받는다.
	String encrypt_pw = MyUtils.encryptString(pw);
	//비밀번호 암호화
	//세션에 박혀있는 id값과 
	
	UserVO param = new UserVO();
	switch(type) {
	case "1": //현재 비밀번호 확인.
		
		param.setUser_id(loginUser.getUser_id());
		param.setUser_pw(encrypt_pw);
		
		int result = UserDAO.login(param); //result 1이 뜨면 성공: 틀리면 1이 아닌값을 넣어라.
		//성공,실패를 정할때
		if(result == 1) {
			request.setAttribute("isAuth", true);
			//isAuth의 값에 true 값을 넣는다.
		}else {
			request.setAttribute("msg", "비밀번호를 다시 입력해주세요.");
		}
		doGet(request,response);
		break;
	case "2":
		param.setI_user(loginUser.getI_user());
		param.setUser_pw(encrypt_pw);
		UserDAO.updUser(param);
		response.sendRedirect("/profile?proc=1");
		break;
	}
	}

}
