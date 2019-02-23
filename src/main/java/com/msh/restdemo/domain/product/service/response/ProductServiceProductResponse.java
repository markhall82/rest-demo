package com.msh.restdemo.domain.product.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductServiceProductResponse {

	private String id;
	private String name;
	private Long usdPrice;

	public ProductServiceProductResponse() {
		
	}
	
	public ProductServiceProductResponse(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUsdPrice() {
		return usdPrice;
	}

	public void setUsdPrice(Long usdPrice) {
		this.usdPrice = usdPrice;
	}
}
