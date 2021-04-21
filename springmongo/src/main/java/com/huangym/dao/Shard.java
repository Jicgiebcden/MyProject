package com.huangym.dao;

public class Shard {

	public void shard() {
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
		
		
		// 开启数据库的分片功能
		// sh.enableSharding("page_db")
		
		// 开启数据库中users集合的分片功能，并指定_id的散列值组作为片键
		// sh.shardCollection("page_db.users", {_id : "hashed"})
	}
}
