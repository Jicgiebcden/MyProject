package com.huangym.springredis.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
}
