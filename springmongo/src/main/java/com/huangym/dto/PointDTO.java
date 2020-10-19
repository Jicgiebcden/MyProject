package com.huangym.dto;

import java.io.Serializable;

/**
 * @author huangyaoming
 * @time 2016年7月16日下午10:53:16
 */
public class PointDTO implements Serializable {

	private static final long serialVersionUID = -8340007461821743460L;

	private String id;
	
	private String name;
	
	/**
	 * 地理位置信息
	 */
	private Point loc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getLoc() {
		return loc;
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}

}
