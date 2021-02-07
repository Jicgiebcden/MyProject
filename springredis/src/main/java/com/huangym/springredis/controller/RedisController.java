package com.huangym.springredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huangym.base.model.ResultModel;
import com.huangym.springredis.redis.JedisService;

@Controller
public class RedisController {

	@Autowired
	private JedisService jedisService;
	
	@RequestMapping
	@ResponseBody
	public ResultModel<String> test() {
		jedisService.setString("key", "huangyaoming", 60000);
		String str = jedisService.getString("key");
		ResultModel<String> result = new ResultModel<String>();
		result.setStatus("0");
		result.setErrorMsg("操作成功！");
		result.setBody(str);
		return result;
	}
	
	@RequestMapping
	@ResponseBody
	public void saveTest(Integer count) {
		jedisService.saveObject(count);
	}
	
	@RequestMapping
	@ResponseBody
	public void getQueue() {
		jedisService.getFromQueue("QUEUE");
	}
	
	@RequestMapping
	@ResponseBody
	public void send2Queue(String value) {
		jedisService.send2Queue("QUEUE", value);
	}
	
}
