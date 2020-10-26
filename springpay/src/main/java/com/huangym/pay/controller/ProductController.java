package com.huangym.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

	@RequestMapping
	public ModelAndView detail() {
		ModelAndView mv = new ModelAndView("detail");
		return mv;
	}
}
