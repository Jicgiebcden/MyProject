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
		Call call = new Call();// ����һ��RPC Call
		call.setTargetObjectURI("urn:HelloWorldService");// Զ�̵ķ�����
		//call.setMethodName("getMessage");// ���ʷ���
		call.setMethodName("showMessage");
		call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC); // ���ñ�����

		Vector params = new Vector();
//		Parameter p1 = new Parameter("name", String.class, "�¸�", null);
//		Parameter p2 = new Parameter("name2", String.class, "����", null);
//		params.addElement(p1);
//		params.addElement(p2);
		call.setParams(params);

		URL url = new URL(endPoint); // SOAP �������ַ
		// ��ʼ����RPC ���󣬲����ط������˵�Ӧ��
		Response resp = call.invoke(url, "");
		// ���Ӧ�������Ƿ��д�
		// �д�ʹ�ӡ������Ϣ��û��ʹ�ӡ����ȷ�ķ���ֵHelloWorld
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