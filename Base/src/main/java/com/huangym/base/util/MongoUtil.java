package com.huangym.base.util;

import java.lang.reflect.Field;
import java.util.Set;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huangym.base.annotation.DefineTypeAnnotation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * MongoDB Utilities
 * 
 */
public final class MongoUtil {
	/**
	 * 构造函数私有化
	 */
	private MongoUtil() {}
	
	private static final Logger logger = LoggerFactory.getLogger(MongoUtil.class);
	
	/**
	 * 将 DBObject 对象转换成 VO 对象
	 * 
	 * @param clazz VO 类的 Class 对象
	 * @param dbo DBObject 对象
	 * @return
	 */
	public static Object toVo(Class<?> clazz, DBObject dbo) {
		if (dbo == null) {
			return null;
		}
		
		Object vo = null;
		
		try {
			vo = clazz.newInstance();
			
			Set<String> columnNames = dbo.keySet();
			for (String columnName : columnNames) {
				
				if("_id".equals(columnName)){	// 芒果 id 不返回给用户
					continue;
				}
				
				Field field = clazz.getDeclaredField(getFieldName(columnName));
				if (field != null) {
					field.setAccessible(true);
					// 获取属性的自定义类型注解
					DefineTypeAnnotation da = field.getType().getAnnotation(DefineTypeAnnotation.class);
					// 若该属性有自定义类型注解，则表示该属性类型是自定义类型（开发人员可以在自己定义的类型上加上该@DefineTypeAnnotation注解）
					if (da != null) {
						Object cval = toVo(field.getType(), (DBObject) dbo.get(columnName));
						field.set(vo, cval);
					} else {
						Object cval = dbo.get(columnName);
						field.set(vo, cval);
					}
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
			logger.error("DBObject对象转换成VO对象时发生异常！", e);
		}
		return vo;
	}
	
	/**
	 * 将 VO 对象转换成 DBObject 对象
	 * 
	 * @param clazz VO 类的 Class 对象
	 * @param vo VO 对象
	 * @return
	 */
	public static DBObject toDbo(Class<?> clazz, Object vo) {
		if (vo == null) {
			return null;
		}
		
		try {
			DBObject dbo = new BasicDBObject();
			
			Field[] fields = clazz.getDeclaredFields();
			
			for (Field field : fields) {
				String fieldName = field.getName();
				
				if ("serialVersionUID".equals(fieldName) || field.getAnnotation(Transient.class) != null) {
					continue;
				}
				
				if("_id".equals(fieldName)){		// 芒果的 id 不用存，只会取出
					continue;
				}
				
				field.setAccessible(true);
				// 获取属性的自定义类型注解
				DefineTypeAnnotation da = field.getType().getAnnotation(DefineTypeAnnotation.class);
				// 若该属性有自定义类型注解，则表示该属性类型是自定义类型（开发人员可以在自己定义的类型上加上该@DefineTypeAnnotation注解）
				if (da != null) {
					DBObject dbObject = toDbo(field.getType(), field.get(vo));
					dbo.put(getColumnName(fieldName), dbObject);
				} else {
					dbo.put(getColumnName(fieldName), field.get(vo));
				}
				field.setAccessible(false);
			}
			
			return dbo;
		} catch (Exception e) {
			logger.error("VO对象转换成 DBObject对象时发生异常！", e);
			return null;
		}
	}
	
	/**
	 * 将 JSON 字符串转换成 DBObject 对象
	 * 
	 * @param json JSON 字符串
	 * @return
	 */
	public static DBObject toDbo(String json) {
		try {
			return (DBObject) JSON.parse(json);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 将 VO 类的字段名转换成数据表的字段名
	 * 
	 * @param fieldName VO 类的字段名
	 * @return
	 */
	public static String getColumnName(String fieldName) {
		if (fieldName == null || "".equals(fieldName)) {
			return "";
		}
		
		char[] chars = fieldName.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		
		for (char c : chars) {
			if (Character.isUpperCase(c)) {
				sb.append("_");
			}
			
			sb.append(Character.toLowerCase(c));
		}
		
		return sb.toString();
	}
	
	/**
	 * 将数据表的字段名转换成 VO 类的字段名
	 * 
	 * @param columnName 数据表的字段名
	 * @return
	 */
	public static String getFieldName(String columnName) {
		if (columnName == null || "".equals(columnName)) {
			return "";
		}
		
		char[] chars = columnName.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		
		while (i < chars.length) {
			if ("_".equals(String.valueOf(chars[i]))) {
				sb.append(Character.toUpperCase(chars[i + 1]));
				i += 2;
			}
			else {
				sb.append(chars[i]);
				i++;
			}
		}
		
		return sb.toString();
	}
}