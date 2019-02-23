package com.msh.restdemo.domain.request;

import javax.validation.constraints.NotEmpty;

import com.msh.restdemo.product.pack.service.validate.ProductIdCheck;

public class ProductRequest {
	
	@NotEmpty
	@ProductIdCheck
	private String productId;

	public ProductRequest() {
		
	}
	
	public ProductRequest(String id) {
		this.productId = id;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String id) {
		this.productId = id;
	}

}
