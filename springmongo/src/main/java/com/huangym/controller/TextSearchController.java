package com.huangym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huangym.base.model.ResultModel;
import com.huangym.dto.IssueDTO;
import com.huangym.service.TextSearchService;

@Controller
public class TextSearchController {

	@Autowired
	private TextSearchService service;
	
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
	public ResultModel<Boolean> addContent() {
		boolean success = service.addContent();
		ResultModel<Boolean> result = new ResultModel<Boolean>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(success);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public ResultModel<List<IssueDTO>> search(String keyword) {
		List<IssueDTO> list = service.search(keyword);
		ResultModel<List<IssueDTO>> result = new ResultModel<List<IssueDTO>>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(list);
		return result;
	}
}
