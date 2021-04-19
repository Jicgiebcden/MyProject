package com.huangym.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * Mongo配置
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	private static final Logger logger = LoggerFactory
			.getLogger(MongoConfig.class);

	@Value("#{mdb.host}")
	private String host;

	@Value("#{mdb.port}")
	private int port;

	@Value("#{mdb.name}")
	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#
	 * getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo
	 * ()
	 */
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		logger.info("make mongo (" + host + ":" + port + ")");
		Mongo m = new Mongo(host, port);
		
		/** 使用多个服务器复制集初始化mongo
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		ServerAddress address = new ServerAddress(host, port);
		seeds.add(address);
		Mongo mongo = new Mongo(seeds);
		*/
		
		MongoOptions options = m.getMongoOptions();
		options.autoConnectRetry = true;
		options.socketKeepAlive = true;
		
		/**
		// 设置读取偏好，选择从哪个复制集成员读取数据。
		options.readPreference = ReadPreference.primary();	// 只从主服务器读取数据，这是默认的读取偏好。如果用户显示指定使用标签读取偏好，那么该读取偏好将被阻塞。
		options.readPreference = ReadPreference.primaryPreferred();	// 读取将被重定向至主服务器，如果没有可用的主服务器，那么读取将被重定向至某个辅助服务器。
		options.readPreference = ReadPreference.secondary();	// 读取将被重定向至辅助服务器节点。如果没有可用的辅助服务器，该选项将会生成异常。
		options.readPreference = ReadPreference.secondaryPreferred();	// 该读取将被重定向至辅助服务器；如果没有可用的辅助服务器，那么读取将被重定向至主服务器。该选项对应旧的slaveOk方法。
		options.readPreference = ReadPreference.nearest();	// 从最近的节点读取数据，不论它是主服务器还是辅助服务器。该选项通过网络延迟决定使用哪个节点。
		
		// 设置写顾虑。
		options.w = 1;
		options.writeConcern = WriteConcern.SAFE;
		options.wtimeout = 0;
		*/
		
		/**	http://www.cnblogs.com/xinghebuluo/archive/2011/12/01/2270896.html
		WriteConcern.NONE:没有异常抛出
		
		// 默认情况下，该操作会使用WriteConcern.NORMAL（仅在网络错误时抛出异常）。使用NORMAL模式参数，可以使得写操作效率非常高。但是如果此时服务器出错，也不会返回错误给客户端，而客户端会误认为操作成功。
		WriteConcern.NORMAL:仅抛出网络错误异常，没有服务器错误异常
		
		// 在很多重要写操作中需要使用WriteConcern.SAFE模式，保证可以感知到这个错误，保证客户端和服务器对一次操作的正确性认知保持一致。（根据笔者测试，如果服务器发生掉电情况，客户端依然得不到当时操作的错误返回，需要特别注意）
		WriteConcern.SAFE:抛出网络错误异常、服务器错误异常；并等待服务器完成写操作。
		
		WriteConcern.MAJORITY: 抛出网络错误异常、服务器错误异常；并等待一个主服务器完成写操作。
		WriteConcern.FSYNC_SAFE: 抛出网络错误异常、服务器错误异常；写操作等待服务器将数据刷新到磁盘。
		WriteConcern.JOURNAL_SAFE:抛出网络错误异常、服务器错误异常；写操作等待服务器提交到磁盘的日志文件。
		WriteConcern.REPLICAS_SAFE:抛出网络错误异常、服务器错误异常；等待至少2台服务器完成写操作。
		*/
		
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#
	 * mappingMongoConverter()
	 */
	@Override
	@Bean
	public MappingMongoConverter mappingMongoConverter() throws Exception {
		MappingMongoConverter converter = new MappingMongoConverter(
				mongoDbFactory(), mongoMappingContext());
		converter.setCustomConversions(customConversions());
		converter.setMapKeyDotReplacement("@");
		return converter;
	}

}
