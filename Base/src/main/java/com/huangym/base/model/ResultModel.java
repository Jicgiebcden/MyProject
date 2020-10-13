package com.huangym.base.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 装载返回结果的实体对象
 * 
 * @author huangyaoming
 * @time 2016年7月15日下午8:57:35
 */
public class ResultModel<T> implements Serializable {

	private static final long serialVersionUID = -4235000009767703449L;

	private String status;
	private String errorMsg;
	private T body;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ApiModel [status=" + status + ", errorMsg=" + errorMsg
				+ ", body=" + body + "]";
	}

	public ResultModel(String status, String errorMsg, T body) {
		super();
		this.status = status;
		this.errorMsg = StringUtils.isNotBlank(errorMsg) ? errorMsg : "";
		this.body = body;
	}

	public ResultModel() {
		super();
	}

}
