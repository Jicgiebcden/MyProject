package com.huangym.dao.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.huangym.base.util.MongoUtil;
import com.huangym.base.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * MongoDB DAO 基类
 * 
 * @author Hungel.zou
 * @time 2015年10月24日 上午11:09:30
 * @param <T>
 */
public abstract class MongoDAO extends Thread{

	protected static final Logger logger = LoggerFactory.getLogger(MongoDAO.class);

	@Autowired
	protected MongoTemplate mongoTemplate;

	/**
	 * 根据查询条件查找单条记录<br>
	 * 若使用外部输入内容拼接，则可能会有注入问题
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param fields
	 *            需要选取(用 1 表示)或过滤(用 -1 表示)的字段，例如：{"name":1, "sex":-1}
	 * @param where
	 *            查询条件，例如：{"id":3} 则表示查找 id 等于 3 的记录，更多高级用法请查阅 MongoDB 的相关资料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T find(Class<?> clazz, String collectionName, String fields,
			String where) {
		if (where == null || "".equals(where)) {
			return null;
		}

		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}

		if ("".equals(collectionName)) {
			return null;
		}

		try {
			DBObject dboWhere = MongoUtil.toDbo(where);

			DBObject dboFields = new BasicDBObject();
			if (fields != null && !"".equals(null)) {
				dboFields = MongoUtil.toDbo(fields);
			}

			DBObject dbObject = mongoTemplate.getCollection(collectionName)
					.findOne(dboWhere, dboFields);
			if (null != dbObject) {
				return (T) MongoUtil.toVo(clazz, dbObject);
			}
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 根据查询条件查找单条记录
	 * @param clazz
	 * @param collectionName
	 * @param dboFields
	 * @param dboWhere
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T find(Class<?> clazz, String collectionName, 
			DBObject dboFields, DBObject dboWhere) {
		if (dboWhere == null) {
			return null;
		}
		if (StringUtil.isEmpty(collectionName)) {
			collectionName = getCollectionName(clazz);
		}
		if (StringUtil.isEmpty(collectionName)) {
			throw new IllegalArgumentException("collectionName is necessary!");
		}
		if (dboFields == null) {
			dboFields = new BasicDBObject();
		}
		DBObject dbObject = mongoTemplate.getCollection(collectionName)
				.findOne(dboWhere, dboFields);
		if (null != dbObject) {
			return (T) MongoUtil.toVo(clazz, dbObject);
		}
		return null;
	}

	/**
	 * 根据查询条件查找多条记录并排序和分页<br>
	 * 若使用外部输入内容拼接，则可能会有注入问题
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param fields
	 *            需要选取(用 1 表示)或过滤(用 -1 表示)的字段，例如：{"name":1, "sex":-1}
	 * @param where
	 *            查询条件，例如：{"id":3} 则表示查找 id 等于 3 的记录，更多高级用法请查阅 MongoDB 的相关资料
	 * @param orderBy
	 *            排序，例如：{"id":-1} 类似于 MySQL 里面的 ORDER BY id DESC
	 * @param limit
	 *            分页，跟 MySQL 一样的用法，例如：10 或 10,20
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	protected <T> List<T> findAll(Class<?> clazz, String collectionName,
			String fields, String where, String orderBy, String limit) {
		List<T> records = new ArrayList<T>();

		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}

		if ("".equals(collectionName)) {
			return records;
		}

		try {
			DBObject dboWhere = new BasicDBObject();
			if (where != null && !"".equals(where)) {
				dboWhere = MongoUtil.toDbo(where);
				System.out.println("dboWhere:" + dboWhere + "  where:" + where);
			}

			DBObject dboFields = new BasicDBObject();
			DBCursor cursor = null;
			if (fields != null && !"".equals(null)) {
				dboFields = MongoUtil.toDbo(fields);
				cursor = mongoTemplate.getCollection(collectionName).find(
						dboWhere, dboFields);
			} else {
				cursor = mongoTemplate.getCollection(collectionName).find(
						dboWhere);
			}

			if (orderBy != null && !"".equals(orderBy)) {
				cursor.sort(MongoUtil.toDbo(orderBy));
			}

			if (limit != null
					&& (limit.matches("^[1-9]+$") || limit
							.matches("^[0-9]+[\\x20\\t]*,[\\x20\\t]*[1-9][0-9]*$"))) {
				if (limit.matches("^[1-9]+$")) {
					cursor = cursor.limit(Integer.valueOf(limit));
				} else if (limit
						.matches("^[0-9]+[\\x20\\t]*,[\\x20\\t]*[1-9][0-9]*$")) {
					String[] temp = limit.split("[\\x20\\t]*,[\\x20\\t]*");

					int skipNum = Integer.valueOf(temp[0]);
					if (skipNum > 0) {
						cursor = cursor.skip(skipNum);
					}

					int limitNum = Integer.valueOf(temp[1]);
					if (limitNum > 0) {
						cursor = cursor.limit(limitNum);
					}
				}
			}

			while (cursor.hasNext()) {
				DBObject record = cursor.next();
				records.add((T) MongoUtil.toVo(clazz, record));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return records;
	}
	
	/**
	 * 根据查询条件查找多条记录并排序和分页
	 * @param clazz
	 * @param collectionName
	 * @param dboFields
	 * @param dboWhere
	 * @param pageable
	 * @param orderBy
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> find(Class<?> clazz, String collectionName,
			DBObject dboFields, DBObject dboWhere, Pageable pageable,
			DBObject orderBy) {
		if (StringUtil.isEmpty(collectionName)) {
			collectionName = getCollectionName(clazz);
		}
		if (StringUtil.isEmpty(collectionName)) {
			throw new IllegalArgumentException("collectionName is necessary!");
		}
		if (dboFields == null) {
			dboFields = new BasicDBObject();
		}
		if (dboWhere == null) {
			dboWhere = new BasicDBObject();
		}
		DBCursor cursor = mongoTemplate.getCollection(collectionName).find(
				dboWhere, dboFields);
		if (pageable != null) {
			cursor.skip(pageable.getOffset());
			cursor.limit(pageable.getPageSize());
		}
		if (orderBy != null) {
			cursor.sort(orderBy);
		}
		List<T> records = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject record = cursor.next();
			records.add((T) MongoUtil.toVo(clazz, record));
		}
		return records;
	}

	/**
	 * 新增记录
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param vo
	 *            VO 对象
	 * @return
	 */
	protected <T> boolean save(Class<?> clazz, String collectionName, T vo) {
		if (vo == null) {
			return false;
		}
		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}
		if ("".equals(collectionName)) {
			return false;
		}
		try {
			String error = mongoTemplate.getCollection(collectionName)
					.insert(MongoUtil.toDbo(clazz, vo)).getError();
			if (StringUtil.isNotBlank(error)) {
				logger.error(error);
			}
			return (error == null || "".equals(error));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 更新记录
	 * @param collectionName 表名称
	 * @param id 表里面的ID或者唯一标示的东西
	 * @param vo 实体
	 * 
	 * @return
	 */
	protected <T> boolean updateVO(String collectionName, String id, T vo) {
		try {
			Field[] fields = vo.getClass().getDeclaredFields();
			Criteria criteria = null;
			Update update = new Update();
			for (Field field : fields) {
				String fieldName = field.getName();

				if ("serialVersionUID".equals(fieldName)
						|| field.getAnnotation(Transient.class) != null) {
					continue;
				}

				if ("_id".equals(fieldName)) {
					continue;
				}

				// 大写切换成 “_小写”
				String columnName = MongoUtil.getColumnName(fieldName);

				// 查询条件
				if (fieldName.equals(id)) {
					criteria = Criteria.where(columnName).is(field.get(vo));
				} else {
					// 日期转换
					if ("class java.util.Date".equals(field.getGenericType()
							.toString())) {
						update.set(columnName, (Date) field.get(vo));
					} else {
						update.set(columnName, field.get(vo));
					}
				}
			}

			if (criteria == null) {
				return false;
			}
			String error = mongoTemplate.updateMulti(new Query(criteria),
					update, collectionName).getError();
			return (error == null || "".equals(error));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新记录
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param vo
	 *            VO 对象
	 * @return
	 */
	protected <T> boolean update(Class<?> clazz, String collectionName, T vo) {
		if (vo == null) {
			return false;
		}

		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}

		if ("".equals(collectionName)) {
			return false;
		}

		try {
			Field pk = getPk(clazz);

			if (pk == null) {
				return false;
			}

			Object pkValue = pk.get(vo);
			if (pkValue == null) {
				return false;
			}

			DBObject dboWhere = new BasicDBObject();
			dboWhere.put(MongoUtil.getColumnName(pk.getName()), pkValue);

			String error = mongoTemplate.getCollection(collectionName)
					.update(dboWhere, MongoUtil.toDbo(clazz, vo)).getError();

			return (error == null || "".equals(error));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 删除符合查询条件的记录<br>
	 * 若使用外部输入内容拼接，则可能会有注入问题
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param where
	 *            查询条件，例如：{"id":3} 则表示查找 id 等于 3 的记录，更多高级用法请查阅 MongoDB 的相关资料
	 * @return
	 */
	protected boolean delete(Class<?> clazz, String collectionName, String where) {
		if (where == null || "".equals(where)) {
			return false;
		}

		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}

		if ("".equals(collectionName)) {
			return false;
		}

		try {
			DBObject dboWhere = MongoUtil.toDbo(where);

			String error = mongoTemplate.getCollection(collectionName)
					.remove(dboWhere).getError();

			return (error == null || "".equals(error));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 统计符合查询条件的记录数<br>
	 * 若使用外部输入内容拼接，则可能会有注入问题
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @param collectionName
	 *            MongoDB 里面的 collection 名称(相当于数据库的表名)
	 * @param where
	 *            查询条件，例如：{"id":3} 则表示查找 id 等于 3 的记录，更多高级用法请查阅 MongoDB 的相关资料
	 * @return
	 */
	protected int count(Class<?> clazz, String collectionName, String where) {
		if (collectionName == null || "".equals(collectionName)) {
			collectionName = getCollectionName(clazz);
		}

		if ("".equals(collectionName)) {
			return 0;
		}

		try {
			DBObject dboWhere = new BasicDBObject();
			if (where != null && !"".equals(where)) {
				dboWhere = MongoUtil.toDbo(where);
			}

			DBCursor cursor = mongoTemplate.getCollection(collectionName).find(
					dboWhere);

			return cursor.count();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}
	
	/**
	 * 统计符合查询条件的记录数
	 * @param clazz
	 * @param collectionName
	 * @param dboWhere
	 * @return
	 */
	protected long count(Class<?> clazz, String collectionName,
			DBObject dboWhere) {
		if (StringUtil.isEmpty(collectionName)) {
			collectionName = getCollectionName(clazz);
		}
		if (StringUtil.isEmpty(collectionName)) {
			throw new IllegalArgumentException("collectionName is necessary!");
		}
		if (dboWhere == null) {
			dboWhere = new BasicDBObject();
		}
		return mongoTemplate.getCollection(collectionName).count(dboWhere);
	}

	/**
	 * 通过 VO 类的注解取得 MongoDB 的 collection 名
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @return
	 */
	protected String getCollectionName(Class<?> clazz) {
		try {
			Table table = clazz.getAnnotation(Table.class);
			return table.name();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	/**
	 * 取得所有 VO 类的所有字段
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @return
	 */
	protected Field[] getFields(Class<?> clazz) {
		try {
			Field[] fields = clazz.getDeclaredFields();
			if (fields == null) {
				return new Field[] {};
			} else {
				return fields;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Field[] {};
		}
	}

	/**
	 * 取得主键的 Field 对象
	 * 
	 * @param clazz
	 *            VO 类的 Class 对象
	 * @return
	 */
	protected Field getPk(Class<?> clazz) {
		Field[] fields = getFields(clazz);

		if (fields == null || fields.length == 0) {
			return null;
		}

		for (Field field : fields) {
			if (field.getAnnotation(Id.class) != null) {
				return field;
			}
		}

		return null;
	}
}