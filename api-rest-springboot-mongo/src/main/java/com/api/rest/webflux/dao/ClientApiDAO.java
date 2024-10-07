package com.api.rest.webflux.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.api.rest.webflux.documents.ClientApi;


public interface ClientApiDAO extends ReactiveMongoRepository<ClientApi, String> {

}
