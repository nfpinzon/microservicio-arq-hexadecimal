package com.npinzon.microservice.msproducto;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.npinzon.microservice.msproducto.port.out.repositories.main", reactiveMongoTemplateRef = "mongoTemplateMain" )
public class MongoMainConfig extends AbstractReactiveMongoConfiguration{

    @Value("${mongo.main.db}")
    private String mongoConnection;

    @Value("${mongo.main.db.name}")
    private String mongoDatabaseName;

    @Override
    protected String getDatabaseName() {
      return mongoDatabaseName;
    }
     @Bean(name = "mainMongoClient")
    public MongoClient reactiveMongoClient() {
        // Configura la conexión a la segunda base de datos
        return MongoClients.create(mongoConnection);
    }

    @Bean(name = "mainMongoDBFactory")
    @Primary
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(@Qualifier("mainMongoClient") MongoClient mongoClient) {
        return new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), getDatabaseName());
    }

    @Bean(name = "mongoTemplateMain")
    public ReactiveMongoTemplate reactiveMongoTemplate(@Qualifier("mainMongoDBFactory") ReactiveMongoDatabaseFactory mongoDatabaseFactory) {
        return new ReactiveMongoTemplate(mongoDatabaseFactory);
    }
}
