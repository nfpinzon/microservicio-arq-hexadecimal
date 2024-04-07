package com.npinzon.microservice.msproducto.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;
import com.npinzon.microservice.msproducto.domain.services.ProductoService;
import com.npinzon.microservice.msproducto.port.out.repositories.secondary.ProductoSecondaryDao;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("Secondary")
public class ProductoServiceSecondaryImp implements ProductoService{

     @Autowired
    private ProductoSecondaryDao dao;

    @Override
    public Flux<Producto> findAll() {
        Flux<Producto> productos = dao.findAll();
        return productos;
    }

    @Override
    public Mono<Producto> findById(String id) {
        if(id.contains("error")){
            return  Mono.just(id)
             .flatMap(a -> {
                 return Mono.error(new Exception("Error controlado de CB"));
             });
         }
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return dao.save(producto);
    }


    @Override
    public Mono<Void> delete(Producto producto) {
        return dao.delete(producto);

    }


    @Override
    public Mono<Long> count() {
        return dao.count();
    }
}

