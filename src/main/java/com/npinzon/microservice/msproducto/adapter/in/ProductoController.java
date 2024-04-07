package com.npinzon.microservice.msproducto.adapter.in;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.npinzon.microservice.msproducto.domain.models.documents.Producto;
import com.npinzon.microservice.msproducto.domain.services.ProductoService;
import com.npinzon.microservice.msproducto.port.in.WebPort;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController implements WebPort{

    @Autowired
    ProductoService productoService;

    @GetMapping("/{repo}")
    public Mono<ResponseEntity<Flux<Producto>>> listar(@PathVariable String repo){
        return Mono.just(ResponseEntity.ok(productoService.findAll()));
    }
   
    @GetMapping("/{repo}/{id}")
    public Mono<ResponseEntity<Producto>> buscarProductoId(@PathVariable String repo,@PathVariable String id){ 
        return productoService.findById(id)
        .map(p -> ResponseEntity.ok(p))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{repo}")
    public Mono<ResponseEntity<Producto>> guardarProducto(@Valid @RequestBody Producto producto, @PathVariable String repo) {
        
        if(producto.getCreateAt() == null){
            producto.setCreateAt(new Date());
        }
        
        return productoService.save(producto)
        .map(p->ResponseEntity
                .created(URI.create("/api/v1/productos".concat(p.getId())))
                .body(p)
                );
        
    }
    
    @PutMapping("/{repo}/{id}")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@Valid @RequestBody Producto producto, @PathVariable String id, @PathVariable String repo){

        return productoService.findById(id).flatMap(p -> {
			p.setNombre(producto.getNombre());
			p.setPrecio(producto.getPrecio());
			return productoService.save(p);
		}).map(p->ResponseEntity.created(URI.create("/api/productos/".concat(p.getId())))
				.body(p))
		.defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{repo}/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id, @PathVariable String repo){
		return productoService.findById(id).flatMap(p ->{
			return productoService.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}
