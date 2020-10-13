package com.huangym.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

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
		MongoOptions options = m.getMongoOptions();
		options.autoConnectRetry = true;
		options.socketKeepAlive = true;
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
