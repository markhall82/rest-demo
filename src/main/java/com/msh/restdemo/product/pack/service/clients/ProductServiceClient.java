package com.msh.restdemo.product.pack.service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.msh.restdemo.common.client.RestClientTemplate;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;

public interface ProductServiceClient {

	ProductServiceProductResponse getProductServiceProduct(String id);

	@Service
	public class Default implements ProductServiceClient {

		@Autowired
		@Qualifier("productStoreTemplate")
		private RestClientTemplate productStoreTemplate;

		@Autowired
		CurrencyServiceClient currencyServiceClient;

		@Override
		@Cacheable("productService")
		public ProductServiceProductResponse getProductServiceProduct(String id) {
			return productStoreTemplate.getForObject(String.format("/%s", id), ProductServiceProductResponse.class);
		}
	}
}
