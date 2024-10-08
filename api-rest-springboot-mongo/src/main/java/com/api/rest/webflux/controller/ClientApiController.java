package com.api.rest.webflux.controller;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.webflux.documents.ClientApi;
import com.api.rest.webflux.services.ClientApiService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clients")
public class ClientApiController {

	@Autowired
	private ClientApiService clientApiService;
	
	@Value("${config.uploads.path}")
	private String path;
	
	@PostMapping("/clientRegistrationWithPhoto")
	public Mono<ResponseEntity<ClientApi>> clientRegistrationWithPhoto(ClientApi clientApi, @RequestPart FilePart filePhoto) {
		clientApi.setPhoto(UUID.randomUUID().toString() + "-" + filePhoto.filename()
		.replace(" ", "")
		.replace(":", "")
		.replace("//", ""));
		
		return filePhoto.transferTo(new File(path + clientApi.getPhoto()))
				.then(clientApiService.save(clientApi))
				.map(c -> ResponseEntity.created(URI.create("/api/clients".concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(c));
				
	}
	
	@PostMapping("/upload/{id}")
	public Mono<ResponseEntity<ClientApi>> uploadPhoto(@PathVariable String id, @RequestPart FilePart filePhoto) {
		return clientApiService.findById(id).flatMap(c -> {
			c.setPhoto(UUID.randomUUID().toString() + "-" + filePhoto.filename()
			.replace(" ", "")
			.replace(":", "")
			.replace("//", ""));
			
			return filePhoto.transferTo(new File(path + c.getPhoto())).then(clientApiService.save(c));
		}).map(c -> ResponseEntity.ok(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping
	public Mono<ResponseEntity<Flux<ClientApi>>> showClients() {
		return Mono.just(
				ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientApiService.findAll())
				);				
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<ClientApi>> showClientDetails(@PathVariable String id) {
		return clientApiService.findById(id).map(c -> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(c))
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
}
