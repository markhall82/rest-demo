package com.msh.restdemo.product.pack.service.clients;

import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;

public interface ProductServiceClient {

	ProductServiceProductResponse getProductServiceProduct(String id);

	
}
