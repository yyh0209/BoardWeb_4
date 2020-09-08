<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>상세페이지</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.0.0/animate.min.css" />
<style>
* {
	font-family: 'Noto Sans KR', sans-serif;
	background-color: #faf9f7;
}

*:focus {
	outline: none;
}

.container {
	width: 700px;
	margin: 30px auto;
}

table {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	/* border: 1px solid black; */
	padding: 8px;
}

#title {
	border-bottom: 1px solid #58585a;
}

/* .boardInfo {
            border-bottom: 1px solid #58585a;
        } */
#nm {
	width: 10%;
}

#nm-1 {
	width: 33%;
}

#date {
	width: 12%;
}

#date-1 {
	width: 25%;
}

#hits {
	width: 10%;
}

#hits-1 {
	width: 10%;
}

.ctnt {
	border-right: 1px solid #58585a;
	border-left: 1px solid #58585a;
	border-bottom: 1px solid #58585a;
	height: 200px;
	padding: 10px;
}

.btn a {
	text-decoration: none;
	color: #58585a;
	background-color: #f5d1ca;
}

.btn button {
	width: 100px;
	background-color: #f5d1ca;
	text-align: center;
	border: none;
	padding: 8px;
	color: #58585a;
	border-radius: 10px;
	margin-top: 20px;
	margin-right: 20px;
	font-weight: bold;
	font-size: 0.9em;
}

#delFrm {
	display: inline-block;
}

.pointCursor {
	cursor: pointer;
}

.marginTop30 {
	margin-top: 30px;
}

.containerPImg {
	width: 30px;
	height: 30px;
	border-radius: 50%;
	display: inline-block;
	overflow: hidden;
}

.pImg {
	object-fit: cover;
	height: 100%;
	width: 100%;
}

#cmt {
	width: 580px
}

.highlight {
	color: red;
	font-weight: bold;
}

#likeListContainer {
	border: ipx solid #bdc3c7;
	position: absolute;
	left: 0px;
	top: 30px;
	width: 100px;
	height: 200px;
	overflow-y: auto;
	background-color: white !important;
	opacity: 0;
	transition-duration: 500ms;
	z-index: 0;
}

#id_like {
	position: relative;
	font-size: 1em;
}

#id_like:hover  #likeListContainer {
	opacity: 1;
	animation: fadeIn;
	animation-duration: 0.5s;
}

.likeItemContainer {
	display: flex;
	background-color: white !important;
}

.likeItemContainer .nm {
	margin-left: 7px;
	font-size: 0.7em;
	display: flex;
}
</style>

</head>
<body class="preload">
	<div class="container">
		<table>
			<tr id="title">
				<th>제목</th>
				<th colspan="6" id="elTitle">${data.title}</th>
			</tr>
			<tr class="boardInfo">
				<th id="nm">작성자</th>
				<td id="nm-1">
					<div class="containerPImg">
						<c:choose>
							<c:when test="${data.profile_img != null}">
								<!--  -->
								<img class="pImg"
									src="/img/user/${data.i_user}/${data.profile_img}">
							</c:when>
							<c:otherwise>
								<img class="pImg" src="/img/profile2.jpg">
							</c:otherwise>
						</c:choose>
					</div> ${data.nm}
				</td>
				<th id="date">작성일시</th>
				<td id="date-1">${data.r_dt }<small>${data == null ? '' : '수정' }</small>
				</td>
				<th id="hits">조회수</th>
				<td id="hits-1">${data.hits }</td>
				<td class="pointCursor" onclick="toggleLike(${data.yn_like})">
					<!-- 좋아요 위치. --> <c:if test="${data.yn_like == 0}">
						<span class="material-icons">favorite_border</span>
					</c:if> <c:if test="${data.yn_like == 1}">
						<span class="material-icons" style="color: red;">favorite</span>
					</c:if>
				</td>
			</tr>
			<c:if test="${data.like_cnt > 0}">
				<tr>
					<td colspan="7"><span id="id_like" class="pointerCursor">좋아요
							${data.like_cnt}개
							<div id="likeListContainer">
								<!-- 위치값 -->
								<c:forEach items="${likeList}" var="item">
									<div class="likeItemContainer">
										<div class="profileContainer">
											<div class="profile">
												<c:choose>
													<c:when test="${item.profile_img == null}">
														<img class="pImg" src="/img/default_profile.jpg">
													</c:when>
													<c:otherwise>
														<img class="pImg"
															src="/img/user/${item.i_user}/${item.profile_img}">
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div class="nm">${item.nm}</div>
									</div>
								</c:forEach>
							</div>
					</span></td>
				</tr>
			</c:if>
		</table>
		<div class="ctnt" id="elCtnt">${data.ctnt}</div>
		<div class="btn">
			<button type="button">
				<a href="/board/list?page=${param.page}&record_cnt=${param.record_cnt}&searchText=${param.searchText}">목록</a>
								<!--?KEY=VALUE  -->
			</button>
			<c:if test="${loginUser.i_user == data.i_user }">
				<button type="submit">
					<a href="/board/regmod?i_board=${data.i_board}">수정</a>
				</button>
				<form id="delFrm" action="/board/del" method="post">
					<input type="hidden" name="i_board" value="${data.i_board}">
					<button type="submit">
						<a href="#" onclick="submitDel()">삭제</a>
					</button>
				</form>
			</c:if>
		</div>
		<!-- 댓글 전송 -->
		<div class="marginTop30">
			<form id="cmtFrm" action="/board/cmt" method="post">
				<input type="hidden" name="i_cmt" value="0"> <input
					type="hidden" name="i_board" value="${data.i_board}">
				<div>
					<input type="text" id="cmt" name="cmt" placeholder="댓글내용">
					<input type="submit" id="cmtSubmit" value="전송"> <input
						type="button" value="취소" onclick="clkCmtCancle()">
				</div>
			</form>
		</div>

		<!-- 댓글의 시작 -->
		<div class="marginTop30">
			<table>
				<tr>
					<th>내용</th>
					<th></th>
					<th>글쓴이</th>
					<th>등록일</th>
					<th>비고</th>
				</tr>

				<c:forEach items="${cmtList}" var="item">
					<tr>
						<td>${item.cmt}</td>
						<td>
							<div class="containerPImg">
								<c:choose>
									<c:when test="${item.profile_img != null}">
										<img class="pImg"
											src="/img/user/${item.i_user}/${item.profile_img}">
									</c:when>
									<c:otherwise>
										<img class="pImg" src="/img/profile2.jpg">
									</c:otherwise>
								</c:choose>
							</div>
						</td>
						<td>${item.nm}</td>
						<td>${item.r_dt}</td>
						<td><c:if test="${item.i_user == loginUser.i_user}">
								<button onclick="clkCmtDel(${item.i_cmt})">삭제</button>
								<button onclick="clkCmtMod(${item.i_cmt},'${item.cmt}')">수정</button>
							</c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

	<script>
    function clkCmtCancle(){
    	cmtFrm.i_cmt.value = 0;
    	cmtFrm.cmt.value = ''
    	cmtSubmit.value = '전송'	
    }
    function clkCmtDel(i_cmt){
    	if(confirm('삭제 하시겠습니까?')){
    		location.href = '/board/cmt?i_board=${data.i_board}&i_cmt='+i_cmt;
    	}
    }
    function clkCmtMod(i_cmt, cmt){
    	console.log('i_cmt : '+i_cmt)
    	
    	cmtFrm.i_cmt.value = i_cmt
    	
    	cmtFrm.cmt.value = cmt;
    	cmtSubmit.value = '수정';
    }
    function toggleLike(yn_like){
    	location.href='/board/toggleLike?page=${param.page}&record_cnt=${param.record_cnt}&searchType=${param.searchType}&searchText=${param.searchText}&i_board=${data.i_board}&yn_like=' + yn_like    			//i_board값도 구현 쿼리스트링을 나누는데 있어서 =을 기준으로 key와 value로 이루어져있다. 키값으로 value를 받는것.
    }
    function submitDel() {
            delFrm.submit()
        }
    function doHighlight(){
    	var searchText = '${param.searchText}';//searchType은 검색유형을 정하기 위한것 select의 name값을 지정한것
    	var searchType = '${param.searchType}';//param.setSearchType(searchType); 
    	//ser에서 boardDomain의 searchtype 의 용도는 찾으려는 검색방법이다.
    	if(searchText == ''){
    		return;
    	}
    	//검색유형의 분기
    	switch(searchType){
    	case 'a'://제목
    		var txt = elTitle.innerText; //innertext는 id 값을 넣은 태그
    		txt = txt.replace(new RegExp('${param.searchText}','gi'),'<span class = "highlight">'+ searchText + '</span>');
    		elTitle.innerHTML = txt; //정규표현식
    		break;
    	case 'b'://내용
    		var txt = elCtnt.innerText; //id값으로 저장된 이름의 태그안에다 넣을 텍스트를 변수로 지정.
    		txt = txt.replace(new RegExp('${param.searchText}','gi'),'<span class = "highlight">'+ searchText + '</span>')
    		elCtnt.innerHTML = txt;
    	case 'c'://제목 내용
    		var txt = elTitle.innerText;
    		txt = txt.replace(new RegExp('${param.searchText}','gi'),'<span class = "highlight">'+ searchText + '</span>')
    		elTitle.innerHTML = txt;
    		
    		txt = elCtnt.innerText; //덮어쓰기
    		txt = txt.replace(new RegExp('${param.searchText}','gi'),'<span class = "highlight">'+ searchText + '</span>')
    		elCtnt.innerHTML = txt;
    		break;
    	}
    }
    doHighlight();
    //메소드가 바로 실행
    </script>
</body>
</html>