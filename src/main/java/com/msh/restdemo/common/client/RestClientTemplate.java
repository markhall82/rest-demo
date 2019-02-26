package com.msh.restdemo.common.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClientTemplate {

	private String service;

	private RestTemplate restTemplate;
	
	public RestClientTemplate(String service, RestTemplate restTemplate){
		this.service = service;
		this.restTemplate = restTemplate;
	}

	public <T> ResponseEntity<T> exchange(String uri, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)  {
		return restTemplate.exchange(service + uri, method, requestEntity, responseType);
	}

	public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
		return restTemplate.getForObject(service + url, responseType, uriVariables);
	}

}
