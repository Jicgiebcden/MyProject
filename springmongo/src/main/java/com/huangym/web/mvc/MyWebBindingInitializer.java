package com.huangym.web.mvc;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.StringMultipartFileEditor;

public class MyWebBindingInitializer implements WebBindingInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.bind.support.WebBindingInitializer#initBinder
	 * (org.springframework.web.bind.WebDataBinder,
	 * org.springframework.web.context.request.WebRequest)
	 */
	public void initBinder(WebDataBinder binder, WebRequest req) {
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
		binder.registerCustomEditor(String.class,
				new StringMultipartFileEditor());
	}

}
