package com.npinzon.microservice.msproducto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.google.common.net.MediaType;
import com.npinzon.microservice.msproducto.application.DroolsService;
import com.npinzon.microservice.msproducto.domain.models.documents.Producto;
import com.npinzon.microservice.msproducto.domain.models.documents.Repositorio;
import com.npinzon.microservice.msproducto.domain.services.ProductoService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class MsProductoApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService service;

	@Autowired
	DroolsService droolsService;

	 @Autowired
    private ApplicationContext context;

	@Value("${config.base.test.url}")
	private String path;

	@Test
	void listarTest() {
		client.get()
		.uri(path)
		.exchange()
		.expectStatus().isOk()
		.expectBodyList(Producto.class);
	}
	
	@Test
	void verProductoTest() {
		setDataAccess();
		String id = service.findAll().blockFirst().getId();
		client.get()
		.uri(path + "/{id}", id)
		.exchange()
		.expectStatus().isOk()
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto producto = response.getResponseBody();
			Assertions.assertThat(producto.getId()).isEqualTo(id);
			Assertions.assertThat(producto.getNombre()).isNotEmpty();
		});

	}

	@Test
	void crearProductoTest() {

		Producto producto = new Producto("Montitor LG Ultrawide 29 pulgadas", 1300000.0);
		client.post().uri(path)
		.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getCreateAt()).isNotNull();
			Assertions.assertThat(p.getNombre()).isEqualTo(producto.getNombre());
			Assertions.assertThat(p.getPrecio()).isEqualTo(producto.getPrecio());
		});

	}
	
	@Test
	void actualizarProductoTest() {

		setDataAccess();		

		Producto producto = service.findAll().blockLast();
		String beforeName = producto.getNombre();
		producto.setNombre(producto.getNombre() + " Actualizado");
		
		client.put().uri(path + "/{id}", producto.getId())
		.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto p = response.getResponseBody();
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getCreateAt()).isNotNull();
			Assertions.assertThat(p.getNombre()).isEqualTo(producto.getNombre());
			Assertions.assertThat(p.getNombre()).isNotEqualTo(beforeName);
			Assertions.assertThat(p.getPrecio()).isEqualTo(producto.getPrecio());
		});

	}

	@Test
	void eliminarProductoTest() {
		setDataAccess();

		Producto producto = service.findAll().blockLast();

		client.delete().uri(path + "/{id}", producto.getId())
		.exchange()
		.expectStatus().isNoContent()
		.expectBody()
		.isEmpty();

		client.get().uri(path+"/{id}", producto.getId())
		.exchange()
		.expectStatus().isNotFound()
		.expectBody()
		.isEmpty();
	}

	private void setDataAccess(){
		String reponame = path.split("/")[path.split("/").length -1];
		Repositorio repositorio = new Repositorio(reponame, "");
        droolsService.executeRuleRepo(repositorio);
        service = (ProductoService)context.getBean(repositorio.getConexion());
        
    }

}
