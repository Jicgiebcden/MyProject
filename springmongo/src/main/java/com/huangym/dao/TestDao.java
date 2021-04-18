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
		
		mongoTemplate.getCollection(COLLECTION_NAME).dropIndex(MongoUtil.toDbo(keys));	// 删除指定的单个索引
		
		mongoTemplate.getCollection(COLLECTION_NAME).dropIndexes();	// 删除集合的所有索引
		
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
	
	public void replicaSet() {
		// 依次启动三个服务器实例，选项--replSet将告诉实例它所加入的复制集的名称。
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --dbpath /data/db1 --port 27021 --replSet testset
		// E:\Tools\mongodb-win32-x86_64-3.0.6-2\bin>mongod.exe --dbpath /data/db2 --port 27022 --replSet testset
		// E:\Tools\mongodb-win32-x86_64-3.0.6-3\bin>mongod.exe --dbpath /data/db3 --port 27023 --replSet testset
		
		// 连接第一个数据库服务器，9RV4Q52是服务器的主机名。需要使用机器名，因为localhost和127.0.0.1都无法正确运行。
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:27021
		// 2017-01-19T11:57:30.271+0800 I CONTROL  Hotfix KB2731284 or later update is not installed, will zero-out data files
		// MongoDB shell version: 3.0.6
		// connecting to: 9RV4Q52:27021/test
		
		// 此时执行查看数据库将会出错，因为没有初始化复制集，不知道那个是主服务器。
		// > show dbs
		// 2017-01-19T11:58:40.943+0800 E QUERY    Error: listDatabases failed:{ "note" : "from execCommand", "ok" : 0, "errmsg" : "not master" }
		//	    at Error (<anonymous>)
		//	    at Mongo.getDBs (src/mongo/shell/mongo.js:47:15)
		//	    at shellHelper.show (src/mongo/shell/utils.js:630:33)
		//	    at shellHelper (src/mongo/shell/utils.js:524:36)
		//	    at (shellhelp2):1:1 at src/mongo/shell/mongo.js:47
		
		// 初始化复制集，执行需要花一点时间。
		// > rs.initiate()
		// {
		//        "info2" : "no configuration explicitly specified -- making one",
		//        "me" : "9RV4Q52:27021",
		//        "ok" : 1
		// }
		// 检查服务器状态，判断它是否已经设置成功。
		// testset:OTHER> rs.status()
		
		// 向复制集中添加另外两个成员。
		// testset:PRIMARY> rs.add("9RV4Q52:27022")
		// testset:PRIMARY> rs.add("9RV4Q52:27023")
		
		// 获取当前复制集的配置文档。
		// testset:PRIMARY> conf = rs.conf()
		// 修改最后加入的第三台服务器，设置为隐藏并且优先级为0，这样它就不会被选举为主服务器。hidden该元素将从db.isMaster()的输出中隐藏该节点，从而阻止在该节点上发生读取操作，即使设置了辅助服务器读取偏好也不可以。
		// testset:PRIMARY> conf.members[2].hidden = true
		// true
		// priority浮点数，该元素代表在选举新的主服务器时，分配给该服务器的权重。如果主服务器不可用，那么复制集将根据该值从辅助服务器中选举出新的主服务器。
		// 任何含有非0的辅助服务器都认为是活跃的，也是可用的服务器。因此，将该值设置为0将强制辅助服务器变成被动模式。如果多个辅助服务器的优先级相同，那就需要进行投票，还可以调用仲裁服务器（如果已配置的话）解决任何死锁。
		// testset:PRIMARY> conf.members[2].priority = 0
		// 0
		// testset:PRIMARY> conf
		// 将修改后的配置文档更新为复制集的配置，整个复制集将先断开连接，然后再重新连接。
		// testset:PRIMARY> rs.reconfig(conf)
		// { "ok" : 1 }
		// testset:PRIMARY> rs.conf()
		
		// 启动一个服务器作为仲裁服务器。
		// E:\Tools\mongodb-win32-x86_64-3.0.6-4\bin>mongod.exe --dbpath /data/db4 --port 27024 --replSet testset -rest
		
		// 往复制集中添加仲裁服务器作为投票成员。
		// testset:PRIMARY> rs.addArb("9RV4Q52:27024")
		// { "ok" : 1 }
		
		// 将隐藏辅助服务器的votes（当前实例在选举主服务器时可投的票数）设置为0，这样被动节点完全变成了被动服务器：它永远不会变成主服务器；它被客户端看做复制集的一部分；并且不会参与选举，也不会被统计为选举的大多数。
		// testset:PRIMARY> conf = rs.conf()
		// testset:PRIMARY> conf.members[2].votes = 0
		// testset:PRIMARY> rs.reconfig(conf)
		
		// rs.status()检测实例的状态
		// testset:PRIMARY> rs.status()
		
		// 使用rs.stepDown()命令强制主服务器退出60秒；该命令将强制选出新的主服务器。该命令在下面的情况下非常有用：
		// 1、需要使托管主服务器实例的服务器离线，无论是调查服务器，还是进行硬件升级或维护；
		// 2、需要为数据结构运行诊断进程；
		// 3、需要模拟主服务器崩溃产生的影响，并强制集群执行故障切换，从而测试应用如何对这样的事件做出响应。
		// testset:PRIMARY> rs.stepDown()
		
		// 如果在辅助服务器上查询会报错，这是因为SECONDARY是不允许读写的，如果非要解决，执行rs.slaveOk()
		// testset:SECONDARY> db.mymember.find()
		// Error: error: { "$err" : "not master and slaveOk=false", "code" : 13435 }
		
		// 每台需要读取操作的辅助服务器都要执行。
		// testset:SECONDARY> rs.slaveOk()
		// testset:SECONDARY> show dbs
		// local  16.070GB
		// mydb    0.078GB
		// testset:SECONDARY> use mydb
		// switched to db mydb
		// testset:SECONDARY> show collections
		// mymember
		// system.indexes
		// testset:SECONDARY> db.mymember.find()
		// { "_id" : ObjectId("588064fbe7e8922d0eb91a29"), "name" : "huangym" }
	}
	
}
