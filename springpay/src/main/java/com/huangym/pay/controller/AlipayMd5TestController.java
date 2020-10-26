package com.huangym.pay.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.md5.config.AlipayConfig;
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
	public ModelAndView submit(@RequestParam String out_trade_no, @RequestParam String subject, 
			@RequestParam String total_fee) {
		ModelAndView mv = new ModelAndView("submit");
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", AlipayConfig.service);
		map.put("partner", AlipayConfig.partner);
		map.put("_input_charset", AlipayConfig.input_charset);
		map.put("notify_url", AlipayConfig.notify_url);
		map.put("return_url", AlipayConfig.return_url);
		map.put("out_trade_no", out_trade_no);
		map.put("subject", subject);
		map.put("total_fee", total_fee);
		map.put("seller_id", AlipayConfig.seller_id);
		map.put("payment_type", AlipayConfig.payment_type);
		String show_url = "http://yzt-test.4000916916.com/springpay/product/detail.do";
		map.put("show_url", show_url);
		// 设置未付款交易的超时时间为1分钟
		map.put("it_b_pay", "1m");
		String html = AlipaySubmit.buildRequest(map, HttpRequest.METHOD_POST, "提交");
		System.out.println(html);
		mv.addObject("formHtml", html);
		return mv;
	}
	
	@RequestMapping
	public ModelAndView result() {
		ModelAndView mv = new ModelAndView("result");
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping
	public String dealNotify() {
		
		return "success";
	}
}
