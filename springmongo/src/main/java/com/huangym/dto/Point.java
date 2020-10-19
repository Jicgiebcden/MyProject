package com.huangym.dto;

import java.io.Serializable;
import java.util.List;

import com.huangym.base.annotation.DefineTypeAnnotation;

@DefineTypeAnnotation("point")
public class Point implements Serializable {

	private static final long serialVersionUID = -1160278323285004204L;
	
	/**
	 * 地理位置类型
	 * @see com.huangym.base.constant.BaseConstant.LocationType
	 */
	private String type;
	
	/**
	 * 坐标点
	 */
	private List<Double> coordinates;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

}
