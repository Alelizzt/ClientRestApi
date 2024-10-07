package com.api.rest.webflux.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.rest.webflux.dao.ClientApiDAO;
import com.api.rest.webflux.documents.ClientApi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientApiServiceImpl implements ClientApiService{

	@Autowired
	private ClientApiDAO clientApiDAO;
	
	@Override
	public Flux<ClientApi> findAll() {
		return clientApiDAO.findAll();
	}

	@Override
	public Mono<ClientApi> findById(String id) {
		return clientApiDAO.findById(id);
	}

	@Override
	public Mono<ClientApi> save(ClientApi client) {
		return clientApiDAO.save(client);
	}

	@Override
	public Mono<Void> delete(ClientApi client) {
		return clientApiDAO.delete(client);
	}

}
