package com.huangym.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.huangym.base.constant.BaseConstant;
import com.huangym.base.util.MongoUtil;
import com.huangym.dao.base.MongoDAO;
import com.huangym.dto.LineStringDTO;
import com.huangym.dto.PointDTO;
import com.huangym.dto.PolygonDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("locationDao")
public class LocationDao extends MongoDAO {

	private static final String COLLECTION_NAME = "location";
	
	public void addIndex(String json) {
		DBObject keys = MongoUtil.toDbo(json);
		mongoTemplate.getCollection(COLLECTION_NAME).createIndex(keys);
	}
	
	public boolean addLocation(PointDTO dto) {
		return save(PointDTO.class, COLLECTION_NAME, dto);
	}
	
	public boolean addLine(LineStringDTO dto) {
		return save(LineStringDTO.class, COLLECTION_NAME, dto);
	}
	
	public boolean addPolygon(PolygonDTO dto) {
		return save(PolygonDTO.class, COLLECTION_NAME, dto);
	}
	
	public List<Object> findNearPoint(PointDTO dto, Double metre) {
//		Point point = new Point(dto.getLoc().getCoordinates()[0], dto.getLoc().getCoordinates()[1]);
//		Criteria criteria = Criteria.where("loc").nearSphere(point).maxDistance(metre);
		
		String json = "{loc : {$geoNear : {$geometry : {type : \"Point\", coordinates : [" 
				+ dto.getLoc().getCoordinates().get(0) + ", " + dto.getLoc().getCoordinates().get(1) + "]}, $maxDistance : " 
				+ metre + "}}}";
//		return find(PointDTO.class, COLLECTION_NAME, null, MongoUtil.toDbo(json), null, null);
		DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find(MongoUtil.toDbo(json), new BasicDBObject());
		List<Object> list = new ArrayList<Object>();
		while (cursor.hasNext()) {
			DBObject record = cursor.next();
			DBObject loc = (DBObject) record.get("loc");
			String type = (String) loc.get("type");
			if (BaseConstant.LocationType.TYPE_POINT.equals(type)) {
				PointDTO result = (PointDTO) MongoUtil.toVo(PointDTO.class, record);
				list.add(result);
			} else if (BaseConstant.LocationType.TYPE_LINESTRING.equals(type)) {
				LineStringDTO result = (LineStringDTO) MongoUtil.toVo(LineStringDTO.class, record);
				list.add(result);
			} else if (BaseConstant.LocationType.TYPE_POLYGON.equals(type)) {
				PolygonDTO result = (PolygonDTO) MongoUtil.toVo(PolygonDTO.class, record);
				list.add(result);
			}
		}
		return list;
		
//		String json = "{loc : {$geoNear : {$geometry : {type : \"Point\", coordinates : [" 
//				+ dto.getLoc().get(0) + ", " + dto.getLoc().get(1) + "]}, $maxDistance : " 
//				+ metre + "}}}";
//		return find(PointDTO.class, COLLECTION_NAME, null, MongoUtil.toDbo(json), null, null);
	}
}
