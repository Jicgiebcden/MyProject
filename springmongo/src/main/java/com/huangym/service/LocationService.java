package com.huangym.service;

import java.util.List;

import com.huangym.dto.PointDTO;

public interface LocationService {
	
	public void addIndex();

	public boolean addLocation();
	
	public boolean addcorporation();
	
	public boolean addLine();
	
	public boolean addPolygon();
	
	public List<Object> findNearPoint();
	
}
