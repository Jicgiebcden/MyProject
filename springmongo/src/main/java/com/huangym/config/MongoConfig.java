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
