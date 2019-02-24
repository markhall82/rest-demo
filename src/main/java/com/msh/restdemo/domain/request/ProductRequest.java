package com.msh.restdemo.domain.request;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import com.msh.restdemo.domain.response.ProductResponse;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ProductRequest)) {
			return false;
		}
		ProductRequest other = (ProductRequest) obj;
		return Objects.equals(productId, other.productId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId);
	}
}
