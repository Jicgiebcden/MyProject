package com.huangym.dto;

import java.io.Serializable;
import java.util.List;

import com.huangym.base.annotation.DefineTypeAnnotation;

@DefineTypeAnnotation("lineString")
public class LineString implements Serializable {

	private static final long serialVersionUID = 3087804898081187148L;

	private String type;
	
	private List<List<Double>> coordinates;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<List<Double>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<Double>> coordinates) {
		this.coordinates = coordinates;
	}

}
