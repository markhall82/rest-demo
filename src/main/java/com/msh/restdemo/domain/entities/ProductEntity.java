package com.msh.restdemo.domain.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.msh.restdemo.domain.request.ProductPackageRequest;


@Entity
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long productId;

	private String identifier;
		
	public ProductEntity() {
		
	}
	
	public ProductEntity(String identifier) {
		this.identifier = identifier;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ProductEntity)) {
			return false;
		}
		ProductEntity other = (ProductEntity) obj;
		return Objects.equals(identifier, other.identifier)
				&& Objects.equals(productId, other.productId);	
		}
	
	@Override
	public int hashCode() {
		return Objects.hash(identifier, productId);
	}

}
