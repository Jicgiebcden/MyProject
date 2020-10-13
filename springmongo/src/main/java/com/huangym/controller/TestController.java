package com.huangym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huangym.base.model.ResultModel;
import com.huangym.service.TestService;

@Controller
public class TestController {

	@Autowired
	private TestService service;
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> add() {
		boolean success = service.addTest();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> update() {
		boolean success = service.updateTest();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<Boolean> updateSet() {
		boolean success = service.updateSetTest();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
}
