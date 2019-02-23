package com.msh.restdemo.common.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

public class RestClientTemplate {

	private String service;

	private Supplier<RestTemplate> restTemplateSupplier;
	
	public RestClientTemplate(String service, Supplier<RestTemplate> restTemplateSupplier){
		this.service = service;
		this.restTemplateSupplier = restTemplateSupplier;
	}

	public <T> ResponseEntity<T> exchange(String uri, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)  {
		return restTemplateSupplier.get().exchange(service + uri, method, requestEntity, responseType);
	}

	public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
		return restTemplateSupplier.get().getForObject(service + url, responseType, uriVariables);
	}

}
