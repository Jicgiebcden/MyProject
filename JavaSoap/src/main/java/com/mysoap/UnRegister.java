package com.mysoap;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.SOAPException;
import org.apache.soap.server.ServiceManagerClient;

/**
 * 相应的，这个类是取消发布的soap服务
 * @author Administrator
 *
 */
public class UnRegister {

	public static void main(String[] args) throws MalformedURLException, SOAPException {
		// soap服务地址
		URL url = new URL("http://127.0.0.1:8080/soap/servlet/rpcrouter");
		ServiceManagerClient client = new ServiceManagerClient(url);
		// 取消的服务名
		client.undeploy("urn:HelloWorldService");
	}
}
