package com.tickets.domain;

public class LoginParam {
	
	private String loginRand;
	
	private String refundLogin = "N";
	
	private String refundFlag;
	
	private String username;
	
	private String password;
	
	private String randCode;

	public String getLoginRand() {
		return loginRand;
	}

	public void setLoginRand(String loginRand) {
		this.loginRand = loginRand;
	}

	public String getRefundLogin() {
		return refundLogin;
	}

	public void setRefundLogin(String refundLogin) {
		this.refundLogin = refundLogin;
	}

	public String getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRandCode() {
		return randCode;
	}

	public void setRandCode(String randCode) {
		this.randCode = randCode;
	}
	
	
}
