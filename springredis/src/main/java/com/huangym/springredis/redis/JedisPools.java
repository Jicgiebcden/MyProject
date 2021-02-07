package com.huangym.springredis.redis;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 散列类型（hash）、列表类型（list）、集合类型（set）、有序集合类型（sorted set）
 * 使用列表类型实现队列
 * @author huangym3
 * @time 2016年11月9日 上午11:25:43
 */
@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository("jedisPools")
public class JedisPools {
	
	private static final Logger logger = LoggerFactory.getLogger(JedisPools.class);
	
	private static JedisPool pool = null;

	@Value("${jedis.host}")
	private String host;

	@Value("${jedis.port}")
	private Integer port;

	@Value("${jedis.pwd}")
	private String pwd;

	@Value("${jedis.timeout}")
	private Integer timeout;

	@Value("${jedis.continuetime}")
	private Integer continuetime;

	private JedisPools jedisPool = null;

	public JedisPools getInstance() {
		if (jedisPool == null) {
			jedisPool = new JedisPools();
		}
		return jedisPool;
	}

	/**
	 * 构建redis连接池
	 */
	public JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 是否启用pool的jmx管理功能, 默认true
			config.setJmxEnabled(true);

			// 是否启用后进先出, 默认true
			config.setLifo(true);

			// 最大连接数, 默认8个
			config.setMaxTotal(100);

			// 最小空闲连接数, 默认0
			config.setMinIdle(0);

			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(300);

			// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
			// 默认-1
			config.setMaxWaitMillis(-1);

			// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
			config.setMinEvictableIdleTimeMillis(1800000);

			// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
			config.setNumTestsPerEvictionRun(3);

			// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
			// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
			config.setSoftMinEvictableIdleTimeMillis(1800000);

			// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
			config.setTimeBetweenEvictionRunsMillis(-1);

			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);

			if (!"".equals(pwd)) {
				pool = new JedisPool(config, host, port, timeout, pwd);
			} else {
				pool = new JedisPool(config, host, port, timeout);
			}
		}
		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param redis
	 */
	@SuppressWarnings("deprecation")
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 保存String数据
	 * 
	 * @param key
	 *            标识符
	 * @param jsonString
	 *            保存的参数
	 * @param mtimeout
	 *            保存时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean setString(String key, String jsonString, int mtimeout) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.set(key, jsonString);
			if (mtimeout != -1) {
				if (mtimeout == 0) {
					jedis.expire(key, continuetime);
				} else {
					jedis.expire(key, mtimeout);
				}
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return true;
	}

	/**
	 * 保存Object数据
	 */
	@SuppressWarnings("deprecation")
	public boolean setObject(String key, Object object, int mtimeout) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			JSONArray testjson = JSONArray.fromObject(object);
			jedis.set(key, testjson.toString());
			if (mtimeout != -1) {
				if (mtimeout == 0) {
					jedis.expire(key, continuetime);
				} else {
					jedis.expire(key, mtimeout);
				}
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return true;
	}
	
	/**
	 * 使用hashes存储对象
	 * @param key
	 * @param object
	 * @param mtimeout
	 * @return
	 */
	public boolean saveObject(String key, Object object, int mtimeout) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			Class c = object.getClass();
			Field[] fields = c.getDeclaredFields();
			Map<String, String> map = new HashMap<String, String>();
			for (Field field : fields) {
				field.setAccessible(true);
				Class fc = field.getType();
				String value = "";
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
					logger.error("不支持的字段类型：" + fc.getName());
					value = fValue.toString();
				}
				map.put(field.getName(), value);
			}
			jedis.hmset(key, map);
			jedis.expire(key, mtimeout);
			return true;
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
	}

	/**
	 * 获取json数据
	 */
	@SuppressWarnings("deprecation")
	public String getString(String key) {
		String value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取队列列头元素
	 * @param key
	 * @return
	 */
	public String getQueue(String key) {
		List<String> value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// 以阻塞方式读取列表元素
			value = jedis.brpop(0, key);
			// 按优先级，依次从key1、key2、key3...队列中获取一个元素
			// jedis.brpop(timeout, key1, key2, key3...);
			System.out.println(value.get(0));System.out.println(value.get(1));
			return value.get(1);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return null;
	}
	
	/**
	 * 将元素放入队列
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setQueue(String key, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.lpush(key, value);
			System.out.println("set value:" + value);
			return true;
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
	}
	
	/**
	 * 更新自动释放内存时间
	 */
	@SuppressWarnings("deprecation")
	public boolean updatetime(String key, int time) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			if (time != 0) {
				jedis.expire(key, time);
			} else {
				jedis.expire(key, continuetime);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return true;
	}

	/**
	 * 删除数据
	 */
	@SuppressWarnings("deprecation")
	public boolean delString(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return false;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return true;
	}

	/**
	 * 取<T> T数据
	 */
	public <T> T getObject(String key, T t) {
		return getObject(key, t, 0);
	}

	/**
	 * 从redis中取key对应的对象，同时更新对象的超时销毁时间
	 * 
	 * @param key
	 * @param t
	 * @param time
	 *            超时时间
	 * @return
	 */
	public <T> T getObject(String key, T t, int time) {
		try {
			String loginjson = getString(key);
			if (loginjson == null) {
				return null;
			}
			// 如果设置有超时时间，则更新
			if (time > 0) {
				updatetime(key, time);
			}
			JSONObject json = JSONObject.fromObject(loginjson.substring(1,
					loginjson.length() - 1));
			Map<String, Class> classMap = new HashMap<String, Class>();
			// 存在List时，加入以下代码
			// 例子：classMap.put("roles", RoleDTO.class);
			t = (T) JSONObject.toBean(json, t.getClass(), classMap);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public <T> T getObject2(String key, T t) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			Class c = t.getClass();
			Field[] fields = c.getDeclaredFields();
			String[] fieldNames = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				fieldNames[i] = fields[i].getName();
			}
			List<String> values = jedis.hmget(key, fieldNames);
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				Class fc = fields[i].getType();
				if (values.get(i) == null || (!String.class.equals(fc) && "".equals(values.get(i)))) {
					fields[i].set(t, null);
					continue ;
				}
				if (String.class.equals(fc)) {
					fields[i].set(t, values.get(i));
				} else if (Long.class.equals(fc)) {
					Long value = Long.valueOf(values.get(i));
					fields[i].set(t, value);
				} else if (Integer.class.equals(fc)) {
					Integer value = Integer.valueOf(values.get(i));
					fields[i].set(t, value);
				} else if (Double.class.equals(fc)) {
					Double value = Double.valueOf(values.get(i));
					fields[i].set(t, value);
				} else if (Float.class.equals(fc)) {
					Float value = Float.valueOf(values.get(i));
					fields[i].set(t, value);
				} else if (Boolean.class.equals(fc)) {
					Boolean value = Boolean.valueOf(values.get(i));
					fields[i].set(t, value);
				} else {
					logger.error("不支持的字段类型：" + fc.getName());
					fields[i].set(t, values.get(i));
				}
			}
			return t;
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return null;
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
	}
}
