package com.msh.restdemo.domain.request;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class ProductPackageRequest {

	@ApiModelProperty(notes = "The package name")
	@NotEmpty
	private String name;
	@ApiModelProperty(notes = "The package description")
	private String description;
	@ApiModelProperty(notes = "The products associated with the package")
	@NotEmpty @Valid
	private Set<ProductRequest> products;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ProductRequest> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductRequest> products) {
		this.products = products;
	}
	
}
