<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
</head>
<body>
<div><a href = "/profile">돌아가기</a></div>
<div>${msg}</div>
<c:if test="${isAuth == false || isAuth == null}"> <!-- 이전비밀번호 확인 -->
	<div>
	<form action="/changePw" method="post">
	<input type="hidden" name="type" value="1">
		<div>
			<label><input type="password" name="pw" placeholder="현재 비밀번호"></label>
		</div>		
		<div>
			<input type="submit" value="확인">
		</div>
	</form>
	</div>
</c:if>
<c:if test="${isAuth == true}"> <!-- 비밀번호 변경-->
	<div>
	<form id="changeFrm" action="/changePw" method="post" onsubmit="return chkChangePw()">
	<input type="hidden" name="type" value="2">
		<div>
			<label><input type="password" name="pw" placeholder="변경할 비밀번호"></label>
		</div>		
		<div>
			<label><input type="password" name="repw" placeholder="변경할 비밀번호 확인"></label>
		</div>		
		<div>
			<input type="submit" value="확인">
		</div>
	</form>
	</div>
</c:if>
</body>
<script>
	function chkChangePw(){
		if(changeFrm.pw.value.length == 0){
			alert('비밀번호를 작성해주세요');
			return false;
		}
		else if(changeFrm.pw.value != changeFrm.repw.value){
			alert('변경할비밀번호가 일치하지않습니다.');
			return false;
		}
	}
	//변경 비밀번호와 변경비밀번호확인이 다르면 알림창
	//같다면 프로필창으로 날림
	//성공하면 프로필 화면으로 간다.
</script>
</html>