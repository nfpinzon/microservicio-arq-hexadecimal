package com.npinzon.microservice.msproducto.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;
import com.npinzon.microservice.msproducto.domain.services.ProductoService;
import com.npinzon.microservice.msproducto.port.out.repositories.secondary.ProductoSecondaryDao;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("Secondary")
public class ProductoServiceSecondaryImp implements ProductoService{

     @Autowired
    private ProductoSecondaryDao dao;

    @CircuitBreaker(name = "productoServiceSecondary")
    @Override
    public Flux<Producto> findAll() {
        Flux<Producto> productos = dao.findAll();
        return productos;
    }

    @CircuitBreaker(name = "productoServiceSecondary")
    @Override
    public Mono<Producto> findById(String id) {
        if(id.contains("error")){
            return  Mono.just(id)
             .flatMap(a -> {
                 return Mono.error(new Exception("Error controlado para CircuitBreacker"));
             });
         }
        return dao.findById(id);
    }

    @CircuitBreaker(name = "productoServiceSecondary")
    @Override
    public Mono<Producto> save(Producto producto) {
        if(producto.getCreateAt() == null){
            producto.setCreateAt(new Date());
        }
        return dao.save(producto);
    }


    @CircuitBreaker(name = "productoServiceSecondary")
    @Override
    public Mono<Void> delete(Producto producto) {
        return dao.delete(producto);

    }


    @CircuitBreaker(name = "productoServiceSecondary")
    @Override
    public Mono<Long> count() {
        return dao.count();
    }
}

