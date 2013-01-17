package com.tickets;

public class Result {
	
	/**
	 * �����Ƿ�ɹ�
	 */
	private boolean success = true;
	
	/**
	 * �����ص�statusCode Ϊ301 ����302 ʱ �������ֵ
	 * ������Ҫ�ض����URL
	 */
	private String redirectUrl;
	
	/**
	 * ���ص�״̬��
	 */
	private int statusCode;
	
	/**
	 * ���ص�html Դ��,,�����ص�statusCode Ϊ301 ����302 ʱ ���Ϊ��
	 */
	private String responseHtml;
	
	/**
	 * ��¼�ɹ���ҳ�淵���ص�token
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
