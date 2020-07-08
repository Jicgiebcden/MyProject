package com.mysoap;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.SOAPException;
import org.apache.soap.server.DeploymentDescriptor;
import org.apache.soap.server.ServiceManagerClient;

/**
 * ���ڲ��������������еķ�ʽ����ע��SOAP�������ǳ����Ҳ���jar�������
 * �����Լ��ο������еķ��������ServiceManagerClient��HelleWorld.xml�ļ�
 * д���������ע�����
 * @author Administrator
 *
 */
public class Register {
	public static void main(String[] args) throws MalformedURLException, SOAPException  {
		// soap�����ַ
		URL url = new URL("http://127.0.0.1:8080/soap/servlet/rpcrouter");
		ServiceManagerClient client = new ServiceManagerClient(url);
		DeploymentDescriptor d = new DeploymentDescriptor();
		
		// urn:HelloWorldService �Ƿ���������Ҫ��ϵͳΨһ��������ȡ�ɺ�������ͬ����Ҳ����ȡ�������ơ�
		d.setID("urn:HelloWorldService");
		d.setScope(DeploymentDescriptor.SCOPE_REQUEST);
		d.setServiceType(DeploymentDescriptor.SERVICE_TYPE_RPC);
		// ����������Ҫ������ȫ������������������
		d.setProviderClass("com.mysoap.HelloWorldService");
		d.setProviderType(DeploymentDescriptor.PROVIDER_JAVA);
		// getMessage ���ṩ�ķ��񷽷���Ҳ������HelloWorldService�ķ�����
		d.setMethods(new String[] {"getMessage", "showMessage"});
		d.setIsStatic(false);
		
		client.deploy(d);
	}
}
