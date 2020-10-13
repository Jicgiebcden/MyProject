package com.huangym.dto;

import java.io.Serializable;

public class LineStringDTO implements Serializable {

	private static final long serialVersionUID = 7896538124961367221L;

	private String id;
	
	private String name;
	
	/**
	 * 地理位置信息
	 */
	private LineString loc;

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

	public LineString getLoc() {
		return loc;
	}

	public void setLoc(LineString loc) {
		this.loc = loc;
	}

}
