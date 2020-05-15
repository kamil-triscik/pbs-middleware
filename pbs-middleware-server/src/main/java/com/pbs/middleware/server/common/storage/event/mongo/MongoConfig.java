package com.pbs.middleware.server.common.storage.event.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@MongoEnabled
@Configuration
@EnableMongoRepositories(basePackages = "com.pbs.middleware.server")
public class MongoConfig {

}
