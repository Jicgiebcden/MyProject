package com.huangym.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.huangym.base.constant.BaseConstant;
import com.huangym.base.util.MongoUtil;
import com.huangym.dao.base.MongoDAO;
import com.huangym.dto.LineStringDTO;
import com.huangym.dto.PointDTO;
import com.huangym.dto.PolygonDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
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
	
	/**
	 * 查找坐标点附近n米内容的记录
	 * @param dto	坐标点
	 * @param metre	距坐标点最大距离（单位米）
	 * @return
	 */
	public List<Object> findNearPoint(PointDTO dto, Double metre) {
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
	}
	
	public List<Object> findWithinBox(PointDTO leftDown, PointDTO rightUP) {
		String json = "{loc : {$geoWithin : {$box : [[" + leftDown.getLoc().getCoordinates().get(0) 
				+ ", " + leftDown.getLoc().getCoordinates().get(1) + "], [" 
				+ rightUP.getLoc().getCoordinates().get(0) + ", " + rightUP.getLoc().getCoordinates().get(1) + "]]}}}";
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
	}
	
	public List<Object> findWithinPolygon(PolygonDTO dto) {
		String json = "{loc : {$geoWithin : {$geometry : {type : \"Polygon\", coordinates : " 
				+ dto.getLoc().getCoordinates().toString() + "}}}";
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
	}
	
	public List<Object> findWithinCircle(PointDTO dto, Double radian) {
		String json = "{loc : {$geoWithin : {$center : [" + dto.getLoc().getCoordinates().toString() + ", 10]}}}";
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
	}
	
	public List<Object> findWithinCenterSphere(PointDTO dto, Double radian) {
		String json = "{loc : {$geoWithin : {$centerSphere : [" + dto.getLoc().getCoordinates().toString() + ", 10]}}}";
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
	}
	
	public void command(PointDTO point) {
		// 查询指定点到每个记录的距离。spherical指定是否地理空间，maxDistance为搜索的最大距离条件，num指定返回多少条结果记录
		String json = "{geoNear : \"location\", near : {type : \"Point\", coordinates : [" 
				+ point.getLoc().getCoordinates().get(0) + ", " 
				+ point.getLoc().getCoordinates().get(1) + "]}, spherical : true, maxDistance : 4000, num : 6}";
		CommandResult result = mongoTemplate.getDb().command(MongoUtil.toDbo(json));
		System.out.println(result);
	}
}
