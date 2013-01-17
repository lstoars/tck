package com.tickets;

public class Result {
	
	/**
	 * 访问是否成功
	 */
	private boolean success = true;
	
	/**
	 * 当返回的statusCode 为301 或者302 时 这个才有值
	 * 返回需要重定向的URL
	 */
	private String redirectUrl;
	
	/**
	 * 返回的状态码
	 */
	private int statusCode;
	
	/**
	 * 返回的html 源码,,当返回的statusCode 为301 或者302 时 这个为空
	 */
	private String responseHtml;
	
	/**
	 * 登录成功后页面返返回的token
	 */
	private String token;
	
	private String errorStackTrace;
	
	private String ypInfo;
	
	private String loginFailPrompt;
	
	private String leftTicketStr;
	
	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getYpInfo() {
		return ypInfo;
	}

	public void setYpInfo(String ypInfo) {
		this.ypInfo = ypInfo;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseHtml() {
		return responseHtml;
	}

	public void setResponseHtml(String responseHtml) {
		this.responseHtml = responseHtml;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginFailPrompt() {
		return loginFailPrompt;
	}

	public void setLoginFailPrompt(String loginFailPrompt) {
		this.loginFailPrompt = loginFailPrompt;
	}

	public String getLeftTicketStr() {
		return leftTicketStr;
	}

	public void setLeftTicketStr(String leftTicketStr) {
		this.leftTicketStr = leftTicketStr;
	}
}
