package com.koreait.pjt.vo;

public class BoardDomain extends BoardVO {
	private String nm; //글쓴이 이름
	private int yn_like; //좋아요 수
	private int record_cnt; //레코드 수
	private int sIdx; // eidx -레코드 수
	private int eIdx;//레코드 수 *페이지
	private String searchText; //글자 찾기
	private String searchType; //찾으려는 제목,내용,혹은 제목+내용
	private String profile_img; //프사
	private int like_cnt; //좋아요
	private int cmt_cnt; //댓글
	//홈페이지의 구성 
	
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public int getLike_cnt() {
		return like_cnt;
	}

	public void setLike_cnt(int like_cnt) {
		this.like_cnt = like_cnt;
	}
	//좋아요 수 누른수가 인자.

	public int getCmt_cnt() {
		return cmt_cnt;
	}

	public void setCmt_cnt(int cmt_cnt) {
		this.cmt_cnt = cmt_cnt;
	}

	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public int getsIdx() {
		return sIdx;
	}

	public void setsIdx(int sIdx) {
		this.sIdx = sIdx;
	}

	public int geteIdx() {
		return eIdx;
	}

	public void seteIdx(int eIdx) {
		this.eIdx = eIdx;
	}

	public int getRecord_cnt() {
		return record_cnt;
	}

	public void setRecord_cnt(int record_cnt) {
		this.record_cnt = record_cnt;
	}

	public int getYn_like() {
		return yn_like;
	}

	public void setYn_like(int yn_like) {
		this.yn_like = yn_like;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}
}
