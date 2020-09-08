<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리스트</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<style>
* {
	font-family: 'Noto Sans KR', sans-serif;
}

*:focus {
	outline: none;
}

body {
	background-color: #faf9f7;
}

.container {
	width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

#usr-color {
	color: #ef9173;
	font-weight: bold;
}

table {
	width: 800px;
	margin: 70px auto;
	border: 0.5px solid #58585a;
	border-collapse: collapse;
}

tr, td {
	text-align: center;
	padding: 7px;
}

th {
	text-align: center;
	padding: 7px;
	border-bottom: 0.5px solid #58585a;
}

.itemRow:hover {
	background: #f5d1ca;
	cursor: pointer;
}

button a {
	color: #58585a;
	text-decoration: none;
}

#logout {
	background-color: #f5d1ca;
	text-align: center;
	padding: 5px;
	color: #58585a;
	border: none;
	border-radius: 10px;
	font-weight: bold;
}

#write {
	width: 100px;
	background-color: #f5d1ca;
	text-align: center;
	border: none;
	padding: 8px;
	color: #58585a;
	border-radius: 10px;
	margin-left: 200px;
	font-weight: bold;
}

.fontCenter {
	text-align: center;
}

.pageSelected {
	color: red;
	font-weight: bold;
}

a {
	text-decoration: none;
	color: black;
}

.pagingFont {
	font-size: 1.3em;
}
#likeListContainer {			
		padding: 10px;		
		border: 1px solid #bdc3c7;
		position: absolute;
		left: 0px;
		top: 30px;
		width: 130px;
		height: 200px;
		overflow-y: auto;
		background-color: white !important;
		transition-duration : 500ms;
	}	
.profile {
		background-color: white !important;
		display: inline-block;	
		width: 25px;
		height: 25px;
	    border-radius: 50%;
	    overflow: hidden;
	}		
	
	.likeItemContainer {
		display: flex;
		width: 100%;
	}
	
	.likeItemContainer .nm {
		background-color: white !important;
		margin-left: 7px;
		font-size: 0.7em;
		display: flex;
		align-items: center;
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

.pagingFont:not(:first-child) {
	margin-left: 13px;
}

.highlight {
	color: red;
	font-weight: bold;
}
</style>
</head>
<body>

	<div class="container">
		<div class="usr-name">
			<span id="usr-color">${loginUser.nm}</span>님 환영합니다
			<button id="logout">
				<a href="/logout">로그아웃</a>
			</button>
		</div>
		<a href="/profile">프로필</a>
		<div>
			<form id="selFrm" action="/board/list" method="get">
				<input type="hidden" name="page" value="${page}"> <input
					type="hidden" name="searchText" value="${param.searchText}">
				레코드 수: <select name="record_cnt" onchange="changeRecordCnt()">
					<c:forEach begin="10" end="30" step="10" var="item">
						<c:choose>
							<c:when test="${param.record_cnt == item}">
								<option value="${item}" selected>${item}개</option>
							</c:when>
							<c:otherwise>
								<option value="${item}">${item}개</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</form>
		</div>
		<table>
			<tr>
				<th>No</th>
				<th>제목</th>
				<th>조회수</th>
				<th>좋아요</th>
				<th></th>
				<th></th>
				<th>작성자</th>
				<th>작성일</th>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr class="itemRow" onclick="moveToDetail(${item.i_board})">
					<td>${item.i_board}</td>
					<td>${item.title}(${item.cmt_cnt})</td>
					<td>${item.hits}</td>
					<!-- 조회수 -->
					<td><span onclick="getLikeList(${item.i_board})">${item.like_cnt}</span>
					</td>
					<!-- 좋아요 횟수. 클릭하면 함수호출 -->
					<td><c:if test="${item.yn_like == 0}">
							<span class="material-icons">favorite_border</span>
						</c:if> <c:if test="${item.yn_like == 1}">
							<span class="material-icons" style="color: red;">favorite</span>
							<!-- 좋아요를 눌렀을때의 애니메이션 -->
						</c:if></td>
					<td>

						<div class="containerPImg">
							<c:choose>
								<c:when test="${item.profile_img != null}">
									<img class="pImg"
										src="/img/user/${item.i_user}/${item.profile_img}">
								</c:when>
								<c:otherwise>
									<img class="pImg" src="/img/default_profile.jpg">
								</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td>${item.nm}</td>
					<td>${item.r_dt}</td>
				</tr>
			</c:forEach>
		</table>
		<div>
			<form action="/board/list">
				<select name="searchType">
					<option value="a" ${searchType == 'a' ? 'selected' : ''}>제목</option>
					<option value="b" ${searchType == 'b' ? 'selected' : ''}>내용</option>
					<option value="c" ${searchType == 'c' ? 'selected' : ''}>제목+내용</option>
				</select> <input type="search" name="searchText" value="${param.searchText}">
				<input type="submit" value="검색">
			</form>
		</div>
		<div class="fontCenter">
			<c:forEach begin="1" end="${pagingCnt}" var="item">
				<c:choose>
					<c:when
						test="${param.page == item || (param.page == null && item == 1)}">
						<span class="pagingFont pageSelected">${item}</span>
					</c:when>
					<c:otherwise>
						<span class="pagingFont"> <a
							href="/board/list?page=${item}&searchText=${param.searchText}">${item}</a>
						</span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<div>
			<a href="regmod"><button id="write">글작성</button></a>
		</div>
	</div>
	<div id="likeListContainer"></div>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<!-- 스크립트는 바디 위에다. -->
	<script>
	function getLikeList(i_board,cnt){
		likeListContainer.style.opacity = 1;
		likeListContainer.innerHTML = ""; //안해주면 쌓이기만 하기때문에 
		if(cnt == 0){
			return;
		}
			//axios다음에 .을 찍고 보내는 방식을 설정.
		axios.get('/board/like',{
			param:{
				i_board //key값, 변수명이 같을때는 이렇게 사용
			}
		}).then(function(res){
			if(res.data.length>0){
				for(let i=0; i<res.data.length;i++){
					const result = makeLikeUser(res.data[i])
					likeListContainer.innerHTML += result;
				}
			}
		})
	}
	//name값을 받는다.
	function makeLikeUser(item){
		const img = one.profile_img == null ? 
				`<img class="pImg" src="/img/profile2.jpg">`
				:
				`<img class="pImg" src="/img/user/\${one.i_user}/\${one.profile_img}">`
		//const를 주는 이유는 안전하기때문
		//\를 안쓰면 el식이다,
		//자바스크립트에서 쓰려면 `\` 문자열 사이에 변수값을 쓰려면 앞에다 \를 써서 값이 아닌 문자열을 보낸다
		const ele = `<div class="likeItemContainer">
		<div class="profileContainer">
				<div class="profile">
					\${img}
				</div>
			</div>
				<div class="nm">\${one.nm}</div>/*  이 문자열을 그대로 보여주려면 \를 붙여라*/
		</div>`
		return ele;
	}
	function changeRecordCnt(){
		selFrm.submit();
	}
		function moveToDetail(i_board) {
			location.href = '/board/detail?page=${page}&record_cnt=${param.record_cnt}&i_board=' + i_board +'&searchText=${param.searchText}';
		}
	</script>
</body>
</html>