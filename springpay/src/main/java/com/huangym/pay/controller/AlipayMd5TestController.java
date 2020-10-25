package com.huangym.pay.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.md5.util.AlipaySubmit;
import com.alipay.md5.util.httpClient.HttpRequest;

@Controller
public class AlipayMd5TestController {

	@RequestMapping
	public ModelAndView confirm() {
		ModelAndView mv = new ModelAndView("confirm");
		
		return mv;
	}
	
	@RequestMapping
	public ModelAndView submit() {
		ModelAndView mv = new ModelAndView("submit");
		String html = AlipaySubmit.buildRequest(new HashMap<String, String>(), HttpRequest.METHOD_POST, "提交");
		mv.addObject("formHtml", html);
		return mv;
	}
}
