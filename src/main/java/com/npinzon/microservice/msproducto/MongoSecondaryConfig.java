package com.npinzon.microservice.msproducto;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.npinzon.microservice.msproducto.port.out.repositories.secondary", reactiveMongoTemplateRef = "mongoTemplateSecondary" )
public class MongoSecondaryConfig extends AbstractReactiveMongoConfiguration{

    @Value("${mongo.secondary.db}")
    private String mongoConnection;

    @Value("${mongo.secondary.db.name}")
    private String mongoDatabaseName;

    @Override
    protected String getDatabaseName() {
      return mongoDatabaseName;
    }

    @Bean(name = "secondaryMongoClient")
    public MongoClient reactiveMongoClient() {
        // Configura la conexión a la segunda base de datos
        return MongoClients.create(mongoConnection);
    }

    @Bean(name = "secondaryMongoDBFactory")
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(@Qualifier("secondaryMongoClient") MongoClient mongoClient) {
        return new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), getDatabaseName());
    }

    @Bean(name = "mongoTemplateSecondary")
    public ReactiveMongoTemplate reactiveMongoTemplate(@Qualifier("secondaryMongoDBFactory") ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
        return new ReactiveMongoTemplate(mongoDatabaseFactory);
    }
}
