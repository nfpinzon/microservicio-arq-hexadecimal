package com.npinzon.microservice.msproducto.port.out.repositories.main;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;

public interface ProductoMainDao extends ReactiveMongoRepository<Producto, String>{

}
