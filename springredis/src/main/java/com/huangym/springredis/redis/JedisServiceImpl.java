package com.huangym.springredis.redis;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huangym.springredis.controller.Test;

@Service("jedisService")
public class JedisServiceImpl implements JedisService {
	
	@Autowired
	private JedisPools jedisPools;
	/**
	 * 保存字符串
	 * @param key
	 * @return
	 */
	public String getString(String key){
		return jedisPools.getString(key);
	}
	
	/**
	 * 保存字符串（自动销毁）
	 * @param key 关键字
	 * @param jsonString 保存的字符串
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean setString(String key,String jsonString,int mtimeout){
		return jedisPools.setString(key,jsonString,mtimeout);
	}

	/**
	 * 保存对象（自动销毁）
	 * @param key 关键字
	 * @param dto 对象
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean setObject(String key, Object dto,int mtimeout){
		return jedisPools.setObject(key, dto,mtimeout);
	}
	
	/**
	 * 更新销毁时间
	 * @param key 关键字
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean updatetime(String key,int mtimeout){
		return jedisPools.updatetime(key,mtimeout);
	}

	/**
	 * 删除
	 * @param key 关键字
	 * @return
	 */
	public boolean delString(String key){
		return jedisPools.delString(key);
	}
	
	/**
	 * 取<T> T 
	 * @param key 关键字
	 * @param t 泛型
	 * @return
	 */
	public <T> T getObject(String key,T t){
		return jedisPools.getObject(key, t);
	}

	@Override
	public void saveObject(int count) {
		Date start = new Date();
		for (Long i = 0L; i < count; i++) {
			Test test = new Test(i + 1, "huang", null, 1.2, false);
			jedisPools.saveObject("object" + i, test, 100);
		}
		Date end = new Date();
		long cost = end.getTime() - start.getTime();
		System.out.println("新的存储方式花费了" + cost + "毫秒");
		
		Test t = new Test();
		jedisPools.getObject2("object0", t);
		System.out.println(t.getId() + " " + t.getName() + " " + t.getAge() + " " + t.getPrice() + " " + t.getIsTrue());
		
//		Date start = new Date();
//		for (Long i = 0L; i < count; i++) {
//			Test test = new Test(i, "huang", 20);
//			jedisPools.setObject("object" + i, test, 100);
//		}
//		Date end = new Date();
//		long cost = end.getTime() - start.getTime();
//		System.out.println("旧的存储方式花费了" + cost + "毫秒");
	}

	@Override
	public void send2Queue(String key, String value) {
		jedisPools.setQueue(key, value);
	}

	@Override
	public void getFromQueue(String key) {
		jedisPools.getQueue(key);
	}
	
}
