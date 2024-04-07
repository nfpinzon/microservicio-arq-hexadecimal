package com.npinzon.microservice.msproducto.port.out.repositories.secondary;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;

public interface ProductoSecondaryDao extends ReactiveMongoRepository<Producto, String>{

}
