package com.huangym.springredis.exception;

public class MyException extends RuntimeException {

	private static final long serialVersionUID = -3645861293526737599L;

	private String errorMsg;
	private Integer errorCode;

	public MyException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public MyException(Integer errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public MyException(Integer errorCode, String errorMsg, Throwable t) {
		super(t);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "StoreAppException [errorCode=" + errorCode + ", errorMsg="
				+ errorMsg + "]";
	}

}
