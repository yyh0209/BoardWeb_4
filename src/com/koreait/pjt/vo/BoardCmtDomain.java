package com.koreait.pjt.vo;

public class BoardCmtDomain extends BoardCmtVO {
	private String nm;
	private String profile_img;
	
	//댓글의 주체에는 이름과 프로필 사진이 필요하다.
	public String getProfile_img() {
		return profile_img;
	}

	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}
}
