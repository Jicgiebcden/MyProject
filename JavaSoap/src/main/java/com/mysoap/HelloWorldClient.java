package com.mysoap;

import java.net.URL;
import java.util.Vector;

import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;

public class HelloWorldClient {
	public static void main(String args[]) throws Exception {
		String endPoint = "http://localhost:8080/soap/servlet/rpcrouter";
		Call call = new Call();// 创建一个RPC Call
		call.setTargetObjectURI("urn:HelloWorldService");// 远程的服务名
		//call.setMethodName("getMessage");// 访问方法
		call.setMethodName("showMessage");
		call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC); // 设置编码风格

		Vector params = new Vector();
//		Parameter p1 = new Parameter("name", String.class, "陈刚", null);
//		Parameter p2 = new Parameter("name2", String.class, "陈勇", null);
//		params.addElement(p1);
//		params.addElement(p2);
		call.setParams(params);

		URL url = new URL(endPoint); // SOAP 服务的网址
		// 开始发送RPC 请求，并返回服务器端的应答
		Response resp = call.invoke(url, "");
		// 检查应答报文中是否有错
		// 有错就打印出错信息，没错就打印到正确的返回值HelloWorld
		if (resp.generatedFault()) {
			Fault fault = resp.getFault();
			System.out.println("The Following Error Occured: ");
			System.out.println(" Fault Code = " + fault.getFaultCode());
			System.out.println(" Fault String =" + fault.getFaultString());
		} else {
			Parameter result = resp.getReturnValue();
			System.out.println(result.getValue());
		}
	}
}