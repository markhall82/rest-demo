package com.msh.restdemo.domain.entities;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.request.ProductRequest;

@Entity
public class ProductPackageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long packageId;
    @Version
    private Integer version;
    @NotNull
    private String name;
    private String description;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProductEntity> products;

	public static final ProductPackageEntity from(final ProductPackageRequest productPackage) {
		return new ProductPackageEntity(productPackage);
	}
	
    public ProductPackageEntity() {
    	
    }
    
    public ProductPackageEntity(ProductPackageRequest productPackage) {
		this.description = productPackage.getDescription();
		this.name = productPackage.getName();
		setEntityFrom(productPackage);
    }
    
    public void setEntityFrom(ProductPackageRequest productPackage) {
		this.description = productPackage.getDescription();
		this.name = productPackage.getName();
		Set<ProductEntity> productsWithIdOnly = Optional.ofNullable(productPackage
				.getProducts())
				.orElse(Collections.emptySet())
				.stream()
				.map(ProductRequest::getProductId)
				.collect(Collectors.toSet())
				.stream()
				.map(ProductEntity::new)
				.collect(Collectors.toSet());
		this.setProducts(productsWithIdOnly);    	
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

	public Set<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductEntity> productEntity) {
		this.products = productEntity;
	}
}