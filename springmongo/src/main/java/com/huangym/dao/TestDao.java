package com.huangym.dao;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.huangym.base.util.MongoUtil;
import com.huangym.dao.base.MongoDAO;
import com.huangym.dto.TestDTO;

/**
 * MongoDB包含了几种更新操作，它们都可以原子操作的方式更新元素。
 * $set
 * $unset
 * $inc
 * $push
 * $pull
 * $pullAll
 * @author huangym3
 * @time 2017年1月17日 下午4:48:42
 */
@Repository("testDao")
public class TestDao extends MongoDAO {

	private static final String COLLECTION_NAME = "test";
	
	private static final String ID = "id";
	
	public boolean add(TestDTO dto) {
		return save(TestDTO.class, COLLECTION_NAME, dto);
	}
	
	public boolean update(TestDTO dto) {
		return updateVO(COLLECTION_NAME, ID, dto);
	}
	
	public boolean updateSet(TestDTO dto) {
		return update(TestDTO.class, COLLECTION_NAME, dto);
	}
	
	public boolean update2() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{\"id\":1, \"name\":\"huang\", \"address\":\"guangzhou\"}";
		// upsert:true	如果不存在则insert，否则update
		// multi:true	如果存在多条，则更新多条
		String error = mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson), true, true).getError();
		return (error == null || "".equals(error));
	}
	
	public boolean update3() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{\"id\":1, \"name\":\"huang\", \"age\":23}";	// update2方法中的内容整个被重写，address字段将被移除掉。
		String error = mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson), true, true).getError();
		return (error == null || "".equals(error));
	}
	
	/**
	 * upsert作用的是整个记录文档
	 * $inc作用的是文档中的某个字段
	 */
	public void inc() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{$inc:{\"age\":1}}";	// 为age字段执行加1（原子）更新操作。如果字段存在，就将该值增加给定的增量；如果该字段不存在，就创建该字段，初始值为给定的增量。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));	// 只更新匹配到的第一条
		
		whereJson = "{\"name\":\"huangym\"}";	// 匹配不到记录
		objJson = "{$inc:{\"age\":1}}";	// 设置upsert为true，将会insert文档，文档将会包含name和age字段，age初始值为给定的增量。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson), true, true);
	}
	
	/**
	 * 设置/增加字段和值
	 * 删除指定字段
	 */
	public void set() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{$set:{\"address\":\"guangzhou china\", \"age\":30, \"weigth\":80}}";	// 将某个字段设置为指定值，如果某字段在文档中不存在，则会创建该字段。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$unset:{\"age\":30}}";	// 删除匹配文档中指定的字段和它的值。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
	}
	
	/**
	 * 往记录文档中添加数组字段，或往数组字段中添加值，还可以同时限制数组内元素的数量。
	 */
	public void array() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{$push:{\"play\":\"football\"}}";	// 如果play是数组，那么该值将被添加到数组中；如果该字段尚不存在，那么该字段的值将被设置为数组；如果该字段存在，但不是数组，那么将会抛出错误。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$push:{\"play\":{$each:[\"ping-pong\", \"football\"]}}}";	// 往给定的数组字段中添加几个不同的值（不会去掉重复的，也不会覆盖已有的值）。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$push:{\"play\":{$each:[\"ping-pong\", \"football\"], $slice:-3}}}";	// $slice可以限制$push操作符中数组内元素的数量。
		// $slice接受负数或0，使用负数将保证数组中的最后n个元素会被保留，而使用0则表示清空数组。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
	}
	
	/**
	 * 对数组进行类似Set的操作
	 */
	public void addToSet() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{$addToSet:{\"play\":\"football\"}}";
		// 如果play数组已经存在且存在重复的football值，该操作是不会去重的，并且无法再往里面添加football值。只有数据不存在的时候，该操作才能将数据添加到数组中。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$addToSet:{\"play\":{$each:[\"football\", \"basketball\", \"ping-pong\"]}}}";
		// $each数组中的值如果已经存在于数组中，则不会添加进数组；如果不存在，则会被添加。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
	}
	
	public void popPull() {
		String whereJson = "{\"name\":\"huang\"}";
		String objJson = "{$pop : {\"play\" : 1}}";	// 删除数组的最后一个元素
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$pop : {\"play\" : -1}}";	// 删除数组的第一个元素
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$pull : {\"play\" : \"football\"}}";	// 从数组中删除所有指定值。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
		
		objJson = "{$pullAll : {\"play\" : [\"ping-pong\", \"basketball\"]}}";	// 从文档数组中删除给定数组中所有指定的值。
		mongoTemplate.getCollection(COLLECTION_NAME).update(MongoUtil.toDbo(whereJson), MongoUtil.toDbo(objJson));
	}
	
	public void rename() {
		mongoTemplate.getCollection("media").rename("newname");	// 重命名集合
	}
	
	public void remove() {
		String whereJson = "{\"name\":\"huang\"}";
		mongoTemplate.getCollection("media").remove(MongoUtil.toDbo(whereJson));	// 删除匹配的所有文档
		
		whereJson = "{}";
		mongoTemplate.getCollection("media").remove(MongoUtil.toDbo(whereJson));	// 删除集合所有的文档
		
		mongoTemplate.getCollection("media").drop();	// 删除整个集合
		
		mongoTemplate.getDb().dropDatabase();	// 删除整个数据库
	}
	
	public void ensureIndex() {
		String keys = "{\"name\" : 1}";
		mongoTemplate.getCollection(COLLECTION_NAME).ensureIndex(keys);
		
		keys = "{\"name\" : 1, \"age\" : -1}";	// 创建复合索引
		mongoTemplate.getCollection(COLLECTION_NAME).ensureIndex(keys);
		
		mongoTemplate.getCollection(COLLECTION_NAME).getIndexInfo();	// 获取集合的索引
		
		String whereJson = "{\"name\":\"huang\"}";
		String indexKeys = "{\"name\":1}";
		// hint强制使用某个指定的索引查询数据，索引没有创建的情况下会返回错误信息。explain返回所选择的查询计划的相关信息，"indexBounds"项将显示出所使用的索引。
		mongoTemplate.getCollection(COLLECTION_NAME).find(MongoUtil.toDbo(whereJson)).hint(MongoUtil.toDbo(indexKeys)).explain();
		
		// db.member.find().min({"age" : 0}).max({"age":25})
		// min()和max()用于限制查询匹配，只有在指定min和max键之间的索引键才会返回。因此需要为指定的键创建索引，如果未创建索引，该命令将返回一条错误消息。
		// 结果中将包含min()值，但不包含max()值。
		// 一般来说，建议使用$gt和$lt而不是min()和max()，因为$gt和$lt不要求使用索引，函数min()和max()主要用于复合键。
	}
	
	/**
	 * count(),distinct(),group()函数
	 */
	public void function() {
		mongoTemplate.getCollection(COLLECTION_NAME).count();
		
		String whereJson = "{\"name\":\"huang\"}";
		mongoTemplate.getCollection(COLLECTION_NAME).find(MongoUtil.toDbo(whereJson)).count();
		
		// count()函数默认将忽略skip()和limit()参数。
		mongoTemplate.getCollection(COLLECTION_NAME).find(MongoUtil.toDbo(whereJson)).skip(2).count();
		
		// 将查询出的字段去重
		mongoTemplate.getCollection(COLLECTION_NAME).distinct("name");
		mongoTemplate.getCollection(COLLECTION_NAME).distinct("name", MongoUtil.toDbo(whereJson));
		
		String key = "{key : {\"name\" : true}}";	// 进行分组的键
		String cond = "{cond : {\"age\" : {$lt : 25}}}";	// 查询条件
		String initial = "{initial : {Total : 0}}";	// 元素开始统计的起始基数
		// items正在遍历的当前文档，prev聚集计数对象
		String reduce = "{reduce : function(items, prev) { if (items.age != null) prev.Total += items.age; }}";
		// group()目前在分片环境中无法正常工作，因此在这种环境中应该使用mapreduce()。另外，在group()函数的输出结果中包含的键不能超过10000个，否则将会抛出异常。这种情况也可以通过mapreduce()来处理。
		mongoTemplate.getCollection(COLLECTION_NAME).group(MongoUtil.toDbo(key), MongoUtil.toDbo(cond), MongoUtil.toDbo(initial), reduce);
	}
	
	public void aggregate() {
		// db.member.aggregate({$group : {"_id" : "$name"}})
		String firstOp = "{$group : {\"_id\" : \"$name\"}}";
		String additionalOps = "{}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$group : {"_id" : "$name", "count" : {$sum : 1}}})
		firstOp = "{$group : {\"_id\" : \"$name\", \"count\" : {$sum : 1}}}";	// count
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$group : {"_id" : {"name":"$name", "age":"$age"}, "count" : {$sum : 1}}})
		firstOp = "{$group : {\"_id\" : {\"name\":\"$name\", \"age\":\"$age\"}, \"count\" : {$sum : 1}}}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$group : {"_id" : {"name":"$name", "age":"$age"}, "count" : {$sum : 1}}}, {$limit : 2})
		additionalOps = "{$limit : 2}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$group : {"_id" : {"name":"$name", "age":"$age"}, "count" : {$sum : 1}}}, {$sort : {"_id" : 1}}, {$limit : 2})
		additionalOps = "[{$sort : {\"_id\" : 1}}, {$limit : 2}]";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$match : {"age" : {$lt : 25}}}, {$group : {"_id" : {"name":"$name", "age":"$age"}, "count" : {$sum : 1}}}, {$sort : {"_id" : 1}})
		additionalOps = "[{$match : {\"age\" : {$lt : 25}}}, {$sort : {\"_id\" : 1}}, {$limit : 2}]";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$unwind : "$play"})
		firstOp = "{$unwind : \"$play\"}";
		additionalOps = "{}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate({$unwind : "$play"}, {$project : {"_id" : 0, "name" : 1, "age" : 1}})
		// db.member.aggregate([{$unwind : "$play"}, {$project : {"_id" : 0, "name" : 1, "age" : 1}}])
		// $project用于限制返回的文档中的字段或者重命名其中的字段
		firstOp = "[{$unwind : \"$play\"}, {$project : {\"_id\" : 0, \"name\" : 1, \"age\" : 1}}]";
		additionalOps = "{}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
		
		// db.member.aggregate([{$unwind : "$play"}, {$project : {"_id" : 0, "name" : 1, "age" : 1}}, {$skip : 2}])
		firstOp = "[{$unwind : \"$play\"}, {$project : {\"_id\" : 0, \"name\" : 1, \"age\" : 1}}, {$skip : 2}]";
		additionalOps = "{}";
		mongoTemplate.getCollection(COLLECTION_NAME).aggregate(MongoUtil.toDbo(firstOp), MongoUtil.toDbo(additionalOps));
	}
	
	public void mapReduce() {
		String map = "{map : function() { var value = { num : this.age, count : 1 }; emit(this.name, value); }}";
		String reduce = "{reduce : function(name, val) { reduceValue = {num : 0, count : 0}; for (var i = 0; i < val.length; i++) { reduceValue.num += val[i].num; reduceValue.count += val[i].count; } return reduceValue; }}";
		String outputTarget = "{out : \"mrresult\"}";
		String query = "{}";
		mongoTemplate.getCollection(COLLECTION_NAME).mapReduce(map, reduce, outputTarget, MongoUtil.toDbo(query));
		
		// var map = function() { var value = { num : this.age, count : 1 }; emit(this.name, value); };
		// var reduce = function(name, val) { reduceValue = {num : 0, count : 0}; for (var i = 0; i < val.length; i++) { reduceValue.num += val[i].num; reduceValue.count += val[i].count; } return reduceValue; };
		// db.member.mapReduce(map, reduce, {out : "mrresult"})
		// db.mrresult.find()
		/**	输出结果如下：
		> db.mrresult.find()
		{ "_id" : "huang", "value" : { "num" : 27, "count" : 3 } }
		{ "_id" : "huangym", "value" : { "num" : 1, "count" : 1 } }
		*/
	}
	
}
