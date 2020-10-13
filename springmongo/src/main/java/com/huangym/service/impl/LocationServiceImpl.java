package com.huangym.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huangym.base.constant.BaseConstant;
import com.huangym.base.util.StringUtil;
import com.huangym.dao.LocationDao;
import com.huangym.dto.LineString;
import com.huangym.dto.LineStringDTO;
import com.huangym.dto.Point;
import com.huangym.dto.PointDTO;
import com.huangym.dto.Polygon;
import com.huangym.dto.PolygonDTO;
import com.huangym.service.LocationService;

@Service("locationService")
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationDao dao;
	
	@Override
	public void addIndex() {
		dao.addIndex("{ loc : \"2dsphere\"}");
	}

	@Override
	public boolean addLocation() {
		PointDTO dto = new PointDTO();
		dto.setId(StringUtil.getRandomString(32));
		dto.setName("my home");
		Point loc = new Point();
		loc.setType(BaseConstant.LocationType.TYPE_POINT);
		loc.setCoordinates(Arrays.asList(new Double(113.465496), new Double(23.10874)));
		dto.setLoc(loc);
		return dao.addLocation(dto);
	}

	@Override
	public boolean addcorporation() {
		PointDTO dto = new PointDTO();
		dto.setId(StringUtil.getRandomString(32));
		dto.setName("corporation");
		Point loc = new Point();
		loc.setType(BaseConstant.LocationType.TYPE_POINT);
		loc.setCoordinates(Arrays.asList(new Double(113.442118), new Double(23.173882)));
		dto.setLoc(loc);
		return dao.addLocation(dto);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addLine() {
		LineStringDTO dto = new LineStringDTO();
		dto.setId(StringUtil.getRandomString(32));
		dto.setName("I'm a line.");
		LineString loc = new LineString();
		loc.setType(BaseConstant.LocationType.TYPE_LINESTRING);
		loc.setCoordinates(Arrays.asList(
				Arrays.asList(new Double(113.465496), new Double(23.10874)),
				Arrays.asList(new Double(113.442118), new Double(23.173882))));
		dto.setLoc(loc);
		return dao.addLine(dto);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addPolygon() {
		PolygonDTO dto = new PolygonDTO();
		dto.setId(StringUtil.getRandomString(32));
		dto.setName("I'm a polygon.");
		Polygon loc = new Polygon();
		loc.setType(BaseConstant.LocationType.TYPE_POLYGON);
		loc.setCoordinates(Arrays.asList(Arrays.asList(
				Arrays.asList(new Double(113.465496), new Double(23.10874)),
				Arrays.asList(new Double(113.460057), new Double(23.112255)),
				Arrays.asList(new Double(113.442118), new Double(23.173882)),
				Arrays.asList(new Double(113.465496), new Double(23.10874)))));
		dto.setLoc(loc);
		return dao.addPolygon(dto);
	}

	@Override
	public List<Object> findNearPoint() {
		PointDTO dto = new PointDTO();
		Point loc = new Point();
		loc.setCoordinates(Arrays.asList(new Double(113.460919), new Double(23.125815)));
		dto.setLoc(loc);
		Double metre = new Double(2000);
		return dao.findNearPoint(dto, metre);
	}

}
