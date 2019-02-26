package com.msh.restdemo.product.pack.service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.msh.restdemo.common.client.RestClientTemplate;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;

@Service
public class ProductServiceClientImpl implements ProductServiceClient {

	@Autowired
	@Qualifier("productStoreTemplate")
	private RestClientTemplate productStoreTemplate;

	@Value("${service.product.productid.url}")
	private String productIdUrl;
	
	@Override
	@Cacheable("productService")
	public ProductServiceProductResponse getProductServiceProduct(String id) {
		return productStoreTemplate.getForObject(String.format(productIdUrl, id), ProductServiceProductResponse.class);
	}
}
