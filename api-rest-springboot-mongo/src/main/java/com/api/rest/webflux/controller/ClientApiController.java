package com.api.rest.webflux.controller;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.webflux.documents.ClientApi;
import com.api.rest.webflux.services.ClientApiService;

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
}
