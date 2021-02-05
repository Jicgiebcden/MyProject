package com.huangym.springredis.controller;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Compare {

	public static void main(String[] args) throws Exception {
		Date start = new Date();
		for (Long i = 0L; i < 100000; i++) {
			Test test = new Test(i, "huang", 20, 1.1, true);
			test2(test);
		}
		Date end = new Date();
		long cost = end.getTime() - start.getTime();
		System.out.println("新的存储方式花费了" + cost + "毫秒");
		
		Date start2 = new Date();
		for (Long i = 0L; i < 100000; i++) {
			Test test = new Test(i, "huang", 20, 1.1, true);
			test1(test);
		}
		Date end2 = new Date();
		long cost2 = end2.getTime() - start2.getTime();
		System.out.println("旧的存储方式花费了" + cost2 + "毫秒");
	}
	
	public static void test1(Object object) {
		JSONArray testjson = JSONArray.fromObject(object);
		testjson.toString();
	}
	
	public static void test2(Object object) throws Exception {
		Class c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		Map<String, String> map = new HashMap<String, String>();
		for (Field field : fields) {
			field.setAccessible(true);
			Class fc = field.getType();
			String value = null;
			Object fValue = field.get(object);
			if (fValue == null) {
				map.put(field.getName(), value);
				continue ;
			}
			if (String.class.equals(fc)) {
				value = (String) field.get(object);
			} else if (Long.class.equals(fc)) {
				Long v = (Long) fValue;
				value = String.valueOf(v);
			} else if (Integer.class.equals(fc)) {
				Integer v = (Integer) fValue;
				value = String.valueOf(v);
			} else if (Double.class.equals(fc)) {
				Double v = (Double) fValue;
				value = String.valueOf(v);
			} else if (Float.class.equals(fc)) {
				Float v = (Float) fValue;
				value = String.valueOf(v);
			} else if (Boolean.class.equals(fc)) {
				Boolean v = (Boolean) fValue;
				value = String.valueOf(v);
			} else {
				value = fValue.toString();
			}
			map.put(field.getName(), value);
		}
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key).append(" ").append(map.get(key));
		}
	}
	
}
