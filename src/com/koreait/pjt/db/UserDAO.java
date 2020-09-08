package com.koreait.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.koreait.pjt.vo.UserLoginHistoryVO;
import com.koreait.pjt.vo.UserVO;

public class UserDAO {
	
	public static int insUserLoginHistory(UserLoginHistoryVO param) {
		String sql = " INSERT INTO t_user_loginhistory "
				+ " (i_history, i_user, ip_addr, os, browser) "
				+ " VALUES "
				+ " (seq_userloginhistory.nextval, ?, ?, ?, ?) ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());//?는 인덱스를 맡고있다. 시퀀스의 값을 증가할려면 Nextval
				ps.setNString(2, param.getIp_addr());
				ps.setNString(3, param.getOs());
				ps.setNString(4,  param.getBrowser());
				
			}
		});
	}
	
	
	public static int insUser(UserVO param) {		
		String sql = " INSERT INTO t_user "
				+ " (i_user, user_id, user_pw, nm, email) "
				+ " VALUES "
				+ " (seq_user.nextval, ?, ?, ?, ?) ";
				
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {				
				ps.setNString(1, param.getUser_id());
				ps.setNString(2, param.getUser_pw());
				ps.setNString(3, param.getNm());
				ps.setNString(4, param.getEmail());				
			}
		});
	}
	
	//0:에러 발생, 1:로그인 성공, 2:비밀번호 틀림, 3:아이디 없음
	public static int login(UserVO param) {		
		String sql = " SELECT i_user, user_pw, nm "
				+ " FROM t_user "
				+ " WHERE user_id = ? ";
				
		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getUser_id());
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					String dbPw = rs.getNString("user_pw");					
					if(dbPw.equals(param.getUser_pw())) { //로그인 성공 문자열 끼리 비교할땐 equals
						int i_user = rs.getInt("i_user"); //getint에서 받은것
						String nm = rs.getNString("nm");						
						param.setUser_pw(null);
						param.setI_user(i_user);
						param.setNm(nm);						
						return 1;
					} else { //비밀번호 틀림
						return 2; 
					}
				} else { //아이디 없음
					return 3;
				}
			}		
		});
	}
	
	public static UserVO selUser(final int i_user) {
		String sql = " SELECT user_id, nm, profile_img, email, r_dt "
				+ " FROM t_user WHERE i_user = ? ";
		
		UserVO result = new UserVO();
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_user);
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					result.setUser_id(rs.getNString("user_id"));
					result.setNm(rs.getNString("nm"));
					result.setProfile_img(rs.getNString("profile_img"));
					result.setEmail(rs.getNString("email"));
					result.setR_dt(rs.getNString("r_dt"));
				}
				return 1;
			}
			
		});
		
		return result;
	}
	public static int updUser (UserVO param) {
		//str1+str2=는 새로은 string타입을 또 만들게 되어 메모리 낭비가 심한데
		//StringBuilder 는 문자열을 더할때 새로운 객체생성 대신 기존의 데이터에다 더하는 개념으로 탄생 속도도 빠르고 부하가 적음.
		//주로 긴 문자열이 필요할때 사용한다. append가 자주 쓰인다.
		StringBuilder sb = new StringBuilder(" UPDATE t_user SET m_dt = sysdate ");
		//null이 아닐때 붙이는 sql문들 변경
		if(param.getUser_pw() !=null) {
			sb.append(" , user_pw = '");
			sb.append(param.getUser_pw());
			sb.append("' ");
		}
		if(param.getNm() !=null) {
			sb.append(" , nm = '");
			sb.append(param.getNm());
			sb.append("' ");
		}
		if(param.getEmail() !=null) {
			sb.append(" , email = '");
			sb.append(param.getEmail());
			sb.append("' ");
		}
		if(param.getProfile_img() !=null) {
			sb.append(" , profile_img = '");
			sb.append(param.getProfile_img());
			sb.append("' ");
		}
		sb.append(" WHERE i_user = ");
		sb.append(param.getI_user());
		
		System.out.println("sb:"+sb.toString());
		// toString은 객체가 가지고 있는 값들을 문자열로만들어 리턴
		
		return JdbcTemplate.executeUpdate(sb.toString(), new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {}
		});
	}
}