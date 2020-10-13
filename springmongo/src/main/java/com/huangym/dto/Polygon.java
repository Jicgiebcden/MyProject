package com.huangym.dto;

import java.io.Serializable;
import java.util.List;

import com.huangym.base.annotation.DefineTypeAnnotation;

@DefineTypeAnnotation("polygon")
public class Polygon implements Serializable {

	private static final long serialVersionUID = -4470028334087950384L;

	private String type;
	
	private List<List<List<Double>>> coordinates;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<List<List<Double>>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<List<Double>>> coordinates) {
		this.coordinates = coordinates;
	}

}
