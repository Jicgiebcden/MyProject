package com.huangym.service;

import java.util.List;

public interface LocationService {
	
	public void addIndex();

	public boolean addLocation();
	
	public boolean addcorporation();
	
	/**
	 * 增加线的地理位置信息
	 * @return
	 */
	public boolean addLine();
	
	/**
	 * 增加一个多边形的地理位置信息
	 * @return
	 */
	public boolean addPolygon();
	
	/**
	 * 查找坐标点附近的一定距离内的地理位置信息列表
	 * @return
	 */
	public List<Object> findNearPoint();
	
	/**
	 * 查找矩形范围内的地理位置信息列表
	 * @return
	 */
	public List<Object> findWithinBox();
	
	/**
	 * 查询多边形范围内的地理位置信息列表
	 * @return
	 */
	public List<Object> findWithinPolygon();
	
	/**
	 * 查询圆形范围内的地理位置信息列表
	 * @return
	 */
	public List<Object> findWithinCircle();
	
	/**
	 * 查询球体（例如地球）上某个圆形之内的记录
	 * @return
	 */
	public List<Object> findWithinCenterSphere();
	
	/**
	 * 执行command
	 * @return
	 */
	public void runCommand();
	
	/**
	 * 增加一个地理坐标点
	 * @param name	坐标名称
	 * @param longitude	经度
	 * @param latitude	纬度
	 * @return
	 */
	public boolean addPoint(String name, double longitude, double latitude);
	
}
