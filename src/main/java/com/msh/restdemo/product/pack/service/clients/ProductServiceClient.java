package com.msh.restdemo.product.pack.service.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.msh.restdemo.common.client.RestClientTemplate;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;
import com.msh.restdemo.domain.response.ProductResponse;

public interface ProductServiceClient {

	ProductResponse getProduct(String id);

	ProductServiceProductResponse getProductServiceProduct(String id);

	ProductResponse getCacheableProduct(String id);

	@Service
	public class Default implements ProductServiceClient {

		private final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

		@Autowired
		@Qualifier("productStoreTemplate")
		private RestClientTemplate productStoreTemplate;

		@Autowired
		CurrencyServiceClient currencyServiceClient;

		@Override
		@CachePut("product")
		public ProductResponse getProduct(String id) {
	
			ProductServiceProductResponse productServiceProductResponse = getProductServiceProduct(id);
			return ProductResponse.from(productServiceProductResponse);
		}

		@Override
		public ProductServiceProductResponse getProductServiceProduct(String id) {
			return productStoreTemplate.getForObject(String.format("/%s", id), ProductServiceProductResponse.class);
		}

		@Override
		@Cacheable("product")
		public ProductResponse getCacheableProduct(String id) {
			return getProduct(id);
		}
	}
}
