package com.huangym.base.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils {
	
	/* 随机字符串生成的方式（由字母和数字组成） */
	public static final int RANDOM_TYPE_NORMAL = 1;
	/* 随机字符串生成的方式（全部由数字组成） */
	public static final int RANDOM_TYPE_ALNUM = 2;
	/* 随机字符串生成的方式（全部由字母组成） */
	public static final int RANDOM_TYPE_ALPHA = 3;
	
	/**
	 * 判断str是null或者是""
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else {
			if (str.equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断str不是null并且不是""
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (null != str && !"".equals(str)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串对象是否为null或者只包含空白（包括包含空白符等）
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串对象是否为不为null或者不只包含空白（包括包含空白符等）
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}
	
	public static String numberFormat(Double number, String format) {
		if (number == null || format == null || "".equals(format)) {
			return "";
		}
		
		try {
			DecimalFormat df = new DecimalFormat(format);
			
			return df.format(number);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String hideString(String input, int startPos, int endPos) {
		if (isEmpty(input)) {
			return input;
		}
		
		int len = input.length();
		
		if (startPos < 0 || endPos > (len - 1)) {
			return input;
		}
		
		char[] chars = input.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < chars.length; i++) {
			if (i < startPos || i > endPos) {
				sb.append(chars[i]);
			}
			else {
				sb.append('*');
			}
		}
		
		return sb.toString();
	}
	
	
	public static String keepSecretString(String firstName, String lastName ) {
		
		String keepSecretString   = firstName +" "+ lastName;
		StringBuffer sb = new StringBuffer();
		if(keepSecretString.length() < keepSecretString.getBytes().length){
			keepSecretString = keepSecretString.replace(" ", "");
			//中文判断
			char[] chars = keepSecretString.toCharArray();
			System.out.println(chars.length);
			for (int i = 0; i < chars.length; i++) {
				if (i < 1 ) {
					sb.append(chars[i]);
				}
				else {
					sb.append('*');
				}
			}
		}else{
			
			if (StringUtil.isNotEmpty(firstName) && StringUtil.isNotEmpty(lastName) ) {
				char[] firstNameChars = firstName.toCharArray();
				char[] lastNameChars = lastName.toCharArray();
				for (int i = 0; i < firstNameChars.length; i++) {
					if (i < 1 || i > firstNameChars.length ) {
						sb.append(firstNameChars[i]);
					}
					else {
						sb.append('*');
					}
				}
				sb.append(" ");
				for (int i = 0; i < lastNameChars.length; i++) {
					if (i < 0 || i > lastNameChars.length-2 ) {
						sb.append(lastNameChars[i]);
					}
					else {
						sb.append('*');
					}
				}
			}
			else if(StringUtil.isEmpty(firstName)||StringUtil.isEmpty(lastName)) {
				keepSecretString = keepSecretString.replace(" ", "");
				char[] chars = keepSecretString.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					if (i < 1 || i > chars.length-2 ) {
						sb.append(chars[i]);
					}
					else {
						sb.append('*');
					}
				}
			}
		}
		return sb.toString();
		 
	}
	
	/**
	 * 格式化输出电话号码和传真号码
	 * 
	 * @author Simon.Ye <samlinye@163.com>
	 * 
	 * @param str 待处理的字符串
	 * @return
	 */
	public static String getFriendlyPhoneNumber(String str) {
		if (isEmpty(str)) {
			return "";
		}
		
		Pattern pattern;
		Matcher matcher;
		
		String countryCode = "";
		String areaCode = "";
		String number = "";
		
		pattern = Pattern.compile("^\\([+]*([0-9]*)\\)([0-9]+)[-]+([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			countryCode = matcher.group(1);
			areaCode = matcher.group(2);
			number = matcher.group(3);
			return "(+" + countryCode + ")" + areaCode + "-" + number;
		}
		
		matcher.reset();
		pattern = Pattern.compile("^([0-9]+)[-]+([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			areaCode = matcher.group(1);
			number = matcher.group(2);
			return areaCode + "-" + number;
		}
		
		matcher.reset();
		pattern = Pattern.compile("^([0-9]+)-([0-9]+)-([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			countryCode = matcher.group(1);
			areaCode = matcher.group(2);
			number = matcher.group(3);
			return "(+" + countryCode + ")" + areaCode + "-" + number;
		}
		
		matcher.reset();
		pattern = Pattern.compile("^([0-9]+)-([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			areaCode = matcher.group(1);
			number = matcher.group(2);
			return areaCode + "-" + number;
		}
		
		return str;
	}
	
	/**
	 * 格式化输出手机号码
	 * 
	 * @author Simon.Ye <samlinye@163.com>
	 * 
	 * @param str 待处理的字符串
	 * @return
	 */
	public static String getFriendlyMobileNumber(String str) {
		if (isEmpty(str)) {
			return "";
		}
		
		Pattern pattern;
		Matcher matcher;
		
		String countryCode = "";
		String number = "";
		
		pattern = Pattern.compile("^\\([+]*([0-9]*)\\)([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			countryCode = matcher.group(1);
			number = matcher.group(2);
			return "(+" + countryCode + ")" + number;
		}
		
		matcher.reset();
		pattern = Pattern.compile("^([0-9]+)-([0-9]+)$");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			countryCode = matcher.group(1);
			number = matcher.group(2);
			return "(+" + countryCode + ")" + number;
		}
		
		return str;
	}
	
	public static String getImageAbslouteUrl(String url, String mediaPath) {
		if (!isNotEmpty(url) || !isNotEmpty(mediaPath)) {
			return "";
		}
		if (url.startsWith("/")) {
			return mediaPath + url;
		}
		return mediaPath + "/" + url;
	}
	
	public static String replaceSign(String str) {
		if (isNotEmpty(str) && str.indexOf(",") != -1) {
			str = str.replaceAll(",", "\\|");
		}
		return str;
	}
	
	public static String replaceSign(String str, String from, String to) {
		if (isNotEmpty(str) && str.indexOf(from) != -1) {
			str = str.replaceAll(from, to);
		}
		return str;
	}
	/**
	 * 生成随机字符串
	 * 
	 * @author Simon.Ye <samlinye@163.com>
	 * 
	 * @param len 随机字符串的长度
	 * @return
	 */
	public static String getRandomString(int len) {
		return getRandomString(len, RANDOM_TYPE_NORMAL);
	}
	
	/**
	 * 按指定的方式生成随机字符串
	 * 
	 * @author Simon.Ye <samlinye@163.com>
	 * 
	 * @param len 随机字符串的长度
	 * @param randomType 生成随机字符串的方式 
	 * @return
	 */
	public static String getRandomString(int len, int randomType) {
		if (len < 1) {
			return "";
		}
		
		String str;
		
		if (randomType == RANDOM_TYPE_ALNUM) {
			str = "3456789987654334567899876543";
		}
		else if (randomType == RANDOM_TYPE_ALPHA) {
			str = "abcdefghjkmnpqrstuvwxyYXWVUTSRQPNMKJHGFEDCBAyxwvutsrqpnmkjhgfedcbaABCDEFGHJKMNPQRSTUVWXY";
		}
		else {
			str = "yxwvutsrqpnmkjhgfedcba3456789ABCDEFGHJKMNPQRSTUVWXY99876543ABCDEFGHJKMNPQRSTUVWXYabcdefghjkmnpqrstuvwxy";
		}
		
		Random rnd = new Random();
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < len; i++) {
			int index = rnd.nextInt(str.length());
			sb.append(str.charAt(index));
		}
		
		return sb.toString();
	}
	
	/**Long转String   
	 * liangtl 
	 * 2015-12-10
	 * */
	public static String LongToString(Long data){
		if(data == null){
			return "";
		}else{
			return String.valueOf(data);
		}
	}
}
