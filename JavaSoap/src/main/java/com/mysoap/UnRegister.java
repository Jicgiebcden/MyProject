package com.mysoap;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.SOAPException;
import org.apache.soap.server.ServiceManagerClient;

/**
 * ��Ӧ�ģ��������ȡ��������soap����
 * @author Administrator
 *
 */
public class UnRegister {

	public static void main(String[] args) throws MalformedURLException, SOAPException {
		// soap�����ַ
		URL url = new URL("http://127.0.0.1:8080/soap/servlet/rpcrouter");
		ServiceManagerClient client = new ServiceManagerClient(url);
		// ȡ���ķ�����
		client.undeploy("urn:HelloWorldService");
	}
}
