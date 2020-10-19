package com.huangym.pay.util;

import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

public class WebUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext = null;
	
	public static HttpSession getSession() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(ra != null){ 
        	return ((ServletRequestAttributes)ra).getRequest().getSession();
        } 
        return null;
	}
	
	public static String getMessage(HttpServletRequest request, String code) {
		WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		return context.getMessage(code, null, locale);
	}
	public static final String htmlToCode(String s) { 
		if(s == null) 
		{ 
		return ""; 
		} else 
		{ s = s.replace("\n\r", "<br>&nbsp;&nbsp;");
		s = s.replace("\r\n", "<br>&nbsp;&nbsp;");
		s= s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		       s =s.replace(" ", "&nbsp;");
		    
		    s=s.replace("\"", "\\"+"\"");//如果原文含有双引号，这一句最关键！！！！！！
		return s; 
		} 
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		WebUtil.applicationContext = applicationContext;
	}  
	
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}
	
	public static <T> Map<String,T> getBeansOfType(Class<T> cls) {
		return applicationContext.getBeansOfType(cls);
	}
	
	/** 
     * 设置下载文件中文件的名称 
     *  
     * @param filename 
     * @param request 
     * @return 
     */  
    public static String encodeFilename(String filename, HttpServletRequest request) {  
      /** 
       * 获取客户端浏览器和操作系统信�?
       * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar) 
       * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6 
       */  
      String agent = request.getHeader("USER-AGENT");  
      try {  
        if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {  
          String newFileName = URLEncoder.encode(filename, "UTF-8");  
          newFileName = StringUtils.replace(newFileName, "+", "%20");  
          if (newFileName.length() > 150) {  
            newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");  
            newFileName = StringUtils.replace(newFileName, " ", "%20");  
          }  
          return newFileName;  
        }  
        if ((agent != null) && (-1 != agent.indexOf("Mozilla")))  
          return MimeUtility.encodeText(filename, "UTF-8", "B");  
    
        return filename;  
      } catch (Exception ex) {  
        return filename;  
      }  
    } 
    
}
