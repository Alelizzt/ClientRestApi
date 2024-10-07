package com.api.rest.webflux.services;

import com.api.rest.webflux.documents.ClientApi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientApiService {

	public Flux<ClientApi> findAll();
	
	public Mono<ClientApi> findById(String id);
	
	public Mono<ClientApi> save(ClientApi client);
	
	public Mono<Void> delete(ClientApi client);
}
