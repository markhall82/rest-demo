package com.msh.restdemo.config;

import com.google.common.collect.Lists;
import com.msh.restdemo.common.client.RestClientTemplate;
import com.msh.restdemo.common.client.interceptors.LoggingRequestInterceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

	@Value("${service.product}")
	private String productService;
	
	@Value("${service.currency}")
	private String currencyService;
	
	@Value("${service.product.user}")
	private String productServiceUsername;
	
	@Value("${service.product.pw}")
	private String productServicePassword;
	
	@Bean(name = "rest-template-supplier")
	public RestTemplate restTemplateBasicAuth(String username, String password) {
			final RestTemplate template = new RestTemplate();

			final ClientHttpRequestInterceptor bai = new BasicAuthenticationInterceptor(username, password);
			final ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();

			final List<ClientHttpRequestInterceptor> ris = Lists.newArrayList(bai, ri);
			template.setInterceptors(ris);

			template.setRequestFactory(
					new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
			return template;
	}

	public RestTemplate restTemplateNoAuth() {
			final RestTemplate template = new RestTemplate();

			final ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();

			final List<ClientHttpRequestInterceptor> ris = Lists.newArrayList(ri);
			template.setInterceptors(ris);

			template.setRequestFactory(
					new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
			return template;
	}
	
	@Bean(name="productStoreTemplate")
	public RestClientTemplate productStoreTemplate() {
		return new RestClientTemplate(productService, restTemplateBasicAuth(productServiceUsername, productServicePassword));
	}

	@Bean(name="currencyStoreTemplate")
	public RestClientTemplate currencyStoreTemplate() {
		return new RestClientTemplate(currencyService, restTemplateNoAuth());
	}
}
