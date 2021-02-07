package com.huangym.springredis.redis;

public interface JedisService {

	/**
	 * 保存字符串
	 * @param key
	 * @return
	 */
	public String getString(String key);
	
	/**
	 * 保存字符串（自动销毁）
	 * @param key 关键字
	 * @param jsonString 保存的字符串
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean setString(String key,String jsonString,int mtimeout);

	/**
	 * 保存对象（自动销毁）
	 * @param key 关键字
	 * @param dto 对象
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean setObject(String key, Object dto,int mtimeout);
	
	/**
	 * 更新销毁时间
	 * @param key 关键字
	 * @param mtimeout 自动销毁时间(s)【-1：不设置，0：默认时间】
	 * @return
	 */
	public boolean updatetime(String key,int mtimeout);

	/**
	 * 删除
	 * @param key 关键字
	 * @return
	 */
	public boolean delString(String key);
	
	/**
	 * 取<T> T 
	 * @param key 关键字
	 * @param t 泛型
	 * @return
	 */
	public <T> T getObject(String key,T t) ;
	
	public void saveObject(int count);
	
	public void send2Queue(String key, String value);
	
	public void getFromQueue(String key);
	
}
