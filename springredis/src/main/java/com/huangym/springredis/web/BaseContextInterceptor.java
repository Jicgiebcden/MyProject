package com.huangym.springredis.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 为JSP加载BASE参数
 */
public class BaseContextInterceptor extends HandlerInterceptorAdapter {
	
	private void setBase(HttpServletRequest request){
		String path = request.getContextPath();  
		String base = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path;
		
		request.setAttribute("base", base);
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		setBase(request);
	}

}
