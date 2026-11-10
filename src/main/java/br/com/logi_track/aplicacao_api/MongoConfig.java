package br.com.logi_track.aplicacao_api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

// tira o _class que o Spring coloca e que o validator do Mongo rejeita
@Configuration
public class MongoConfig {

	@Bean
	MappingMongoConverter mappingMongoConverter(
			MongoDatabaseFactory factory,
			MongoMappingContext context,
			MongoCustomConversions conversions) {
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
		converter.setCustomConversions(conversions);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		converter.afterPropertiesSet();
		return converter;
	}
}
