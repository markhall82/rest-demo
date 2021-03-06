package com.msh.restdemo.domain.response;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.msh.restdemo.domain.entities.ProductEntity;
import com.msh.restdemo.domain.entities.ProductPackageEntity;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductPackageResponse {

	@ApiModelProperty(notes = "The application-specific package ID")
	private Long packageId;
	@ApiModelProperty(notes = "The package name")
	private String name;
	@ApiModelProperty(notes = "The package description")
	private String description;
	@ApiModelProperty(notes = "The price of the package", required = true)
	private BigDecimal price;
	@ApiModelProperty(notes = "The products associated with the package")
	private Set<ProductResponse> products;

	public static final ProductPackageResponse from(final ProductPackageEntity productPackageEntity) {
		return new ProductPackageResponse(productPackageEntity);
	}

	public ProductPackageResponse() {

	}

	public ProductPackageResponse(ProductPackageEntity productPackageEntity) {
		this.setDescription(productPackageEntity.getDescription());
		this.setName(productPackageEntity.getName());
		this.setPackageId(productPackageEntity.getPackageId());
		Set<ProductResponse> productsWithIdOnly = Optional.ofNullable(productPackageEntity.getProducts())
				.orElse(Collections.emptySet()).stream().map(ProductEntity::getIdentifier).collect(Collectors.toSet())
				.stream().map(ProductResponse::new).collect(Collectors.toSet());
		this.setProducts(productsWithIdOnly);
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

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

	public BigDecimal getPrice() {
		Optional<BigDecimal> returnPrice = Optional.ofNullable(this.getProducts()).orElse(Collections.emptySet())
				.stream().map(ProductResponse::getPrice).reduce(BigDecimal::add);

		return returnPrice.orElse(BigDecimal.ZERO);
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<ProductResponse> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductResponse> products) {
		this.products = products;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ProductPackageResponse)) {
			return false;
		}
		ProductPackageResponse other = (ProductPackageResponse) obj;
		return packageId == other.packageId && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name) && Objects.equals(products, other.products)
				&& Objects.equals(price, other.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, packageId, price, description, products);

	}
}
