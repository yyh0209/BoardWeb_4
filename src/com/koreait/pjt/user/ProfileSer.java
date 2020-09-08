package com.koreait.pjt.user;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.UserDAO;
import com.koreait.pjt.vo.UserVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/profile")

public class ProfileSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//프로필 화면(나의 프로필 이미지,이미지 변경 가능한 화면)
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		UserVO loginUser = MyUtils.getLoginUser(request);	
   		request.setAttribute("data", UserDAO.selUser(loginUser.getI_user()));
   		ViewResolver.forward("user/profile", request, response);
   		//주소값이 아닌 파일 경로다.
   	}

   	//이미지 변경 처리는 무조건 포스트 방식이고 길이에 한계가 없기 때문
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserVO loginUser = MyUtils.getLoginUser(request);
		
		String savePath = getServletContext().getRealPath("img") + "/user/" + loginUser.getI_user();//
		//저장 경로
		System.out.println("savePath : "+savePath);
		
		
		File directory = new File(savePath);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		//절대경로값 /img/user user 밑에 폴더가 없으면 에러가 터진다. 디렉토리가 아니라서 폴더를 만들어라.
		int maxFileSize = 10_485_760;//1024*1024*10(10mb)최대 사이즈 크기
		String fileNm = "";
		String saveFileNm = "";
		
		try {
			MultipartRequest mr = new MultipartRequest(request, savePath
					,maxFileSize, "UTF-8",new DefaultFileRenamePolicy() );
			Enumeration files = mr.getFileNames();//파일이름을 불러옴

//UUID :중복된 값이 나올수 있는 확률이 10년에 한번.
			if(files.hasMoreElements()) {
				String key = (String)files.nextElement();
				fileNm = mr.getFilesystemName(key);
				String extension = fileNm.substring(fileNm.lastIndexOf("."));
				saveFileNm = UUID.randomUUID()+extension; 
				System.out.println("key : "+ key);
				System.out.println("fileNm : "+ fileNm);
				System.out.println("savefile : "+ saveFileNm);	
				File oldFile = new File(savePath +"/"+ fileNm);//파일을 임포트한 파일의 확장자
				File newFile = new File(savePath +"/" + saveFileNm);//임포트한후의 확장자와 이름 db에다 저장
				oldFile.renameTo(newFile);//파일명 변경.
				//파일 확장자를 뽑아내 여기다 넣어라.
				//이미지경로,확장자.세이브path는 절대경로는 웹에서 안쓴다.
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(saveFileNm != null){ //db에 프로필 파일명을 저장.
			UserVO param = new UserVO();
			param.setProfile_img(saveFileNm);
			param.setI_user(loginUser.getI_user());
			UserDAO.updUser(param);
		}
		response.sendRedirect("/profile"); //넣었을때만 db에 저장.
	}

}
