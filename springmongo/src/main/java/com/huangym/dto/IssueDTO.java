package com.huangym.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯
 * @author huangyaoming
 * @time 2016年7月25日下午9:49:53
 */
public class IssueDTO implements Serializable {

	private static final long serialVersionUID = -8535939431241086043L;

	private String title;
	
	private String content;
	
	private Date createTime;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
