package com.huangym.dto;

import java.io.Serializable;

public class PolygonDTO implements Serializable {

	private static final long serialVersionUID = 1811649449850734648L;

	private String id;
	
	private String name;
	
	/**
	 * 地理位置信息
	 */
	private Polygon loc;

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

	public Polygon getLoc() {
		return loc;
	}

	public void setLoc(Polygon loc) {
		this.loc = loc;
	}

}
