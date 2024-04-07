package com.npinzon.microservice.msproducto.domain.services;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
       Flux<Producto> findAll();

	 Mono<Producto> findById(String id);

	 Mono<Producto> save(Producto producto);

	 Mono<Void> delete(Producto producto);

	 Mono<Long> count();
}
