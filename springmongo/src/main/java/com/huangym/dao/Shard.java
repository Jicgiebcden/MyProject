package com.huangym.dao;

public class Shard {

	public void shard() {
		// http://www.2cto.com/database/201307/230419.html
		
		// 假设有三台物理服务器，每台物理服务器上启动三个mongoDB服务实例，这三个实例分别属于shard1、shard2、shard3三个复制集。三个复制集将作为整个mongo集群的分片服务。
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10001 --dbpath /data/s1 --replSet shard1
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10002 --dbpath /data/s2 --replSet shard2
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10003 --dbpath /data/s3 --replSet shard3
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10004 --dbpath /data/s4 --replSet shard1
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10005 --dbpath /data/s5 --replSet shard2
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10006 --dbpath /data/s6 --replSet shard3
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10007 --dbpath /data/s7 --replSet shard1
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10008 --dbpath /data/s8 --replSet shard2
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --shardsvr --port 10009 --dbpath /data/s9 --replSet shard3
		
		// 在三台物理服务器上，分别各启动一个分片配置服务器。
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --configsvr --port 20001 --dbpath /data/config1
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --configsvr --port 20002 --dbpath /data/config2
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongod.exe --configsvr --port 20003 --dbpath /data/config3
		
		// 在三台物理服务器上，分别各启动一个mongos分片控制器。
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongos.exe --port 30001 --chunkSize 1 --configdb 9RV4Q52:20001,9RV4Q52:20002,9RV4Q52:20003
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongos.exe --port 30002 --chunkSize 1 --configdb 9RV4Q52:20001,9RV4Q52:20002,9RV4Q52:20003
		// E:\Tools\mongodb-win32-x86_64-3.0.6\bin>mongos.exe --port 30003 --chunkSize 1 --configdb 9RV4Q52:20001,9RV4Q52:20002,9RV4Q52:20003
		
		
		// 连接复制集shard1的10001端口的成员，然后初始化复制集配置。
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:10001/admin
		// > config = {_id : "shard1", members:[{_id : 0, host:"9RV4Q52:10001"}, {_id : 1, host:"9RV4Q52:10004"}, {_id : 2, host:"9RV4Q52:10007"}]}
		// > rs.initiate(config)
		
		// 连接复制集shard2的10005端口的成员，然后初始化复制集配置。
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:10005/admin
		// > config = {_id : "shard2", members:[{_id : 0, host:"9RV4Q52:10002"}, {_id : 1, host:"9RV4Q52:10005"}, {_id : 2, host:"9RV4Q52:10008"}]}
		// > rs.initiate(config)
		
		// 连接复制集shard3的10009端口的成员，然后初始化复制集配置。
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:10009/admin
		// > config = {_id : "shard3", members:[{_id : 0, host:"9RV4Q52:10003"}, {_id : 1, host:"9RV4Q52:10006"}, {_id : 2, host:"9RV4Q52:10009"}]}
		// > rs.initiate(config)
		
		// 连接mongos分片控制器，向集群中添加复制集分片。
		// （按理说，每一个分片控制器都应该添加shard1、shard2、shard3这三个复制集分片的，但是现在只要往其中一个分片控制器添加任何一个复制集分片，其他的分片控制器就有了这个复制集分片，不能再添加进去了。难道是因为所有的实例都是在一台机器上，然后机器上的配置信息是共享的？）
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:30001/admin
		// mongos> sh.addShard("shard1/9RV4Q52:10001")
		
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:30002/admin
		// mongos> sh.addShard("shard2/9RV4Q52:10002")
		
		// E:\Tools\mongodb\mongodb-win32-x86_64-3.0.6\bin>mongo.exe 9RV4Q52:30003/admin
		// mongos> sh.addShard("shard3/9RV4Q52:10003")
		/*
		mongos> sh.status()
		--- Sharding Status ---
		  sharding version: {
		        "_id" : 1,
		        "minCompatibleVersion" : 5,
		        "currentVersion" : 6,
		        "clusterId" : ObjectId("58819537bcf9ee12151c9dd2")
		}
		  shards:
		        {  "_id" : "shard0000",  "host" : "9RV4Q52:27023" }	// 把以前的shard0000、shard0001也查出来了，说明以前的分片也添加到分片控制器了。
		        {  "_id" : "shard0001",  "host" : "9RV4Q52:27024" }
		        {  "_id" : "shard1",  "host" : "shard1/9RV4Q52:10001,9RV4Q52:10004,9RV4Q52:10007" }
		        {  "_id" : "shard2",  "host" : "shard2/9RV4Q52:10002,9RV4Q52:10005,9RV4Q52:10008" }
		        {  "_id" : "shard3",  "host" : "shard3/9RV4Q52:10003,9RV4Q52:10006,9RV4Q52:10009" }
		  balancer:
		        Currently enabled:  yes
		        Currently running:  no
		        Failed balancer rounds in last 5 attempts:  3
		        Last reported error:  DBClientBase::findN: transport error: 9RV4Q52:27023 ns: admin.$cmd query: { features: 1 }
		        Time of Reported error:  Fri Jan 20 2017 17:03:20 GMT+0800 (中国标准时间)
		        Migration Results for the last 24 hours:
		                No recent migrations
		  databases:
		        {  "_id" : "admin",  "partitioned" : false,  "primary" : "config" }
		        {  "_id" : "test",  "partitioned" : false,  "primary" : "shard0000" }
		        {  "_id" : "testdb",  "partitioned" : true,  "primary" : "shard0000" }
		                testdb.testcollection
		                        shard key: { "testkey" : 1 }
		                        chunks:
		                                shard0000       10
		                                shard0001       9
		                        { "testkey" : { "$minKey" : 1 } } -->> { "testkey" : 1 } on : shard0000 Timestamp(16, 0)
		                        { "testkey" : 1 } -->> { "testkey" : 9 } on : shard0000 Timestamp(17, 0)
		                        { "testkey" : 9 } -->> { "testkey" : 4690 } on : shard0001 Timestamp(18, 0)
		                        { "testkey" : 4690 } -->> { "testkey" : 12393 } on : shard0000 Timestamp(15, 1)
		                        { "testkey" : 12393 } -->> { "testkey" : 17074 } on : shard0000 Timestamp(5, 0)
		                        { "testkey" : 17074 } -->> { "testkey" : 24339 } on : shard0000 Timestamp(6, 0)
		                        { "testkey" : 24339 } -->> { "testkey" : 29020 } on : shard0000 Timestamp(7, 0)
		                        { "testkey" : 29020 } -->> { "testkey" : 36456 } on : shard0000 Timestamp(8, 0)
		                        { "testkey" : 36456 } -->> { "testkey" : 41137 } on : shard0000 Timestamp(9, 0)
		                        { "testkey" : 41137 } -->> { "testkey" : 48673 } on : shard0000 Timestamp(19, 0)
		                        { "testkey" : 48673 } -->> { "testkey" : 53354 } on : shard0001 Timestamp(20, 0)
		                        { "testkey" : 53354 } -->> { "testkey" : 61021 } on : shard0000 Timestamp(21, 0)
		                        { "testkey" : 61021 } -->> { "testkey" : 65702 } on : shard0001 Timestamp(14, 1)
		                        { "testkey" : 65702 } -->> { "testkey" : 73402 } on : shard0001 Timestamp(3, 15)
		                        { "testkey" : 73402 } -->> { "testkey" : 78083 } on : shard0001 Timestamp(3, 17)
		                        { "testkey" : 78083 } -->> { "testkey" : 85783 } on : shard0001 Timestamp(3, 18)
		                        { "testkey" : 85783 } -->> { "testkey" : 90464 } on : shard0001 Timestamp(3, 20)
		                        { "testkey" : 90464 } -->> { "testkey" : 98164 } on : shard0001 Timestamp(3, 21)
		                        { "testkey" : 98164 } -->> { "testkey" : { "$maxKey" : 1 } } on : shard0001 Timestamp(3, 22)
		*/
		
		// 开启数据库的分片功能
		// sh.enableSharding("page_db")
		
		// 开启数据库中users集合的分片功能，并指定_id的散列值组作为片键
		// sh.shardCollection("page_db.users", {_id : "hashed"})
	}
}
