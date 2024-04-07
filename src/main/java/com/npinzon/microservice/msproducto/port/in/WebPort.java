package com.npinzon.microservice.msproducto.port.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WebPort {

    @GetMapping("/{repo}")
    public Mono<ResponseEntity<Flux<Producto>>> listar(@PathVariable String repo);
    
    @GetMapping("/{repo}/{id}")
    public Mono<ResponseEntity<Producto>> buscarProductoId(@PathVariable String repo,@PathVariable String id);
    
    @PostMapping("/{repo}")
    public Mono<ResponseEntity<Producto>> guardarProducto(@Valid @RequestBody Producto producto, @PathVariable String repo) ;
    
    @PutMapping("/{repo}/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@Valid @RequestBody Producto producto, @PathVariable String id, @PathVariable String repo);
    
    @DeleteMapping("/{repo}/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id, @PathVariable String repo);
}
