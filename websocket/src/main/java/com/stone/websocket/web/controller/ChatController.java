package com.stone.websocket.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@RequestMapping(value = "/server")
	public ModelAndView serverPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/chat/serverpage");
		return mv;
	}

	@RequestMapping(value = "/client")
	public ModelAndView clientPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/chat/clientpage");
		return mv;
	}

	@RequestMapping(value = "/room")
	public ModelAndView chatRoom() {
		String room = "room001";
		ModelAndView mv = new ModelAndView();
		mv.addObject("room", room);
		mv.setViewName("/chat/room");
		return mv;
	}
}
