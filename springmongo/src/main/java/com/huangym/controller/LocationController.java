package com.huangym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huangym.base.model.ResultModel;
import com.huangym.service.LocationService;

@Controller
public class LocationController {

	@Autowired
	private LocationService service;
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addIndex() {
		service.addIndex();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(true);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addLocation() {
		boolean success = service.addLocation();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addCorporation() {
		boolean success = service.addcorporation();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addLine() {
		boolean success = service.addLine();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addPolygon() {
		boolean success = service.addPolygon();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<Object>> findNearPoint() {
		List<Object> list = service.findNearPoint();
		ResultModel<List<Object>> result = new ResultModel<List<Object>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<Object>> findWithinBox() {
		List<Object> list = service.findWithinBox();
		ResultModel<List<Object>> result = new ResultModel<List<Object>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<Object>> findWithinPolygon() {
		List<Object> list = service.findWithinPolygon();
		ResultModel<List<Object>> result = new ResultModel<List<Object>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<Object>> findWithinCircle() {
		List<Object> list = service.findWithinCircle();
		ResultModel<List<Object>> result = new ResultModel<List<Object>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<Object>> findWithinCenterSphere() {
		List<Object> list = service.findWithinCenterSphere();
		ResultModel<List<Object>> result = new ResultModel<List<Object>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<String> runCommand() {
		service.runCommand();
		ResultModel<String> result = new ResultModel<String>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(null);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> addPoint(@RequestParam String name, 
			@RequestParam Double longitude, @RequestParam Double latitude) {
		boolean success = service.addPoint(name, longitude, latitude);
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
}
