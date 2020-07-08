package com.mysoap;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.SOAPException;
import org.apache.soap.server.DeploymentDescriptor;
import org.apache.soap.server.ServiceManagerClient;

/**
 * 由于参照例子用命令行的方式发布注册SOAP服务，老是出现找不到jar类的现象
 * 所以自己参考命令行的服务管理类ServiceManagerClient，HelleWorld.xml文件
 * 写了这个发布注册程序
 * @author Administrator
 *
 */
public class Register {
	public static void main(String[] args) throws MalformedURLException, SOAPException  {
		// soap服务地址
		URL url = new URL("http://127.0.0.1:8080/soap/servlet/rpcrouter");
		ServiceManagerClient client = new ServiceManagerClient(url);
		DeploymentDescriptor d = new DeploymentDescriptor();
		
		// urn:HelloWorldService 是服务名，它要求系统唯一。这里是取成和类名相同，你也可以取其他名称。
		d.setID("urn:HelloWorldService");
		d.setScope(DeploymentDescriptor.SCOPE_REQUEST);
		d.setServiceType(DeploymentDescriptor.SERVICE_TYPE_RPC);
		// 服务类名，要求填入全类名（包名＋类名）
		d.setProviderClass("com.mysoap.HelloWorldService");
		d.setProviderType(DeploymentDescriptor.PROVIDER_JAVA);
		// getMessage 是提供的服务方法，也就是类HelloWorldService的方法名
		d.setMethods(new String[] {"getMessage", "showMessage"});
		d.setIsStatic(false);
		
		client.deploy(d);
	}
}
