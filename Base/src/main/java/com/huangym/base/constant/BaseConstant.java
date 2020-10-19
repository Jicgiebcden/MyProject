package com.huangym.base.constant;

public class BaseConstant {

	private BaseConstant() {}
	
	/**
	 * 地理位置类型
	 */
	public static class LocationType {
		
		/**
		 * 地理位置类型：点
		 */
		public static final String TYPE_POINT = "Point";
		
		/**
		 * 地理位置类型：线
		 */
		public static final String TYPE_LINESTRING = "LineString";
		
		/**
		 * 地理位置类型：多边形。需要保证起点坐标跟终点坐标一致
		 */
		public static final String TYPE_POLYGON = "Polygon";
	}
}
