package com.msh.restdemo.domain.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponse {

	private String productId;
	private String name;
	private BigDecimal price;

	public static ProductResponse from(ProductServiceProductResponse productServiceProductResponse) {
		return new ProductResponse(productServiceProductResponse);
	}

	public ProductResponse() {

	}

	public ProductResponse(ProductServiceProductResponse productServiceProductResponse) {
		this.productId = productServiceProductResponse.getId();
		this.name = productServiceProductResponse.getName();
		this.price = BigDecimal.valueOf(productServiceProductResponse.getUsdPrice()).divide(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.CEILING);
	}

	public ProductResponse(String identifier) {
		this.productId = identifier;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String identifier) {
		this.productId = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ProductResponse)) {
			return false;
		}
		ProductResponse other = (ProductResponse) obj;
		return Objects.equals(productId, other.productId) && Objects.equals(name, other.name)
				&& Objects.equals(price, other.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, productId, price);
	}

}
