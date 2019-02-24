package com.msh.restdemo.domain.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.msh.restdemo.domain.response.ProductPackageResponse;
import com.msh.restdemo.domain.response.ProductResponse;

public class ProductPackageResponseTests {

	@Test
	public void testProductPackageRepsonseIsEqual() {
		ProductResponse productResponse1 = new ProductResponse();
		productResponse1.setName("Name 1");
		productResponse1.setPrice(BigDecimal.valueOf(1.0));
		productResponse1.setProductId("1");
		
		ProductResponse productResponse2 = new ProductResponse();
		productResponse2.setName("Name 2");
		productResponse2.setPrice(BigDecimal.valueOf(2.0));
		productResponse2.setProductId("2");
		
		Set<ProductResponse> products = new HashSet<>();
		products.add(productResponse1);
		products.add(productResponse2);
		
		ProductPackageResponse productPackageResponse = new ProductPackageResponse();
		productPackageResponse.setDescription("Description 1");
		productPackageResponse.setName("Name 1");
		productPackageResponse.setPackageId(1l);
		productPackageResponse.setProducts(products);
		
		ProductResponse productResponse4 = new ProductResponse();
		productResponse4.setName("Name 1");
		productResponse4.setPrice(BigDecimal.valueOf(1.0));
		productResponse4.setProductId("1");
		
		ProductResponse productResponse5 = new ProductResponse();
		productResponse5.setName("Name 2");
		productResponse5.setPrice(BigDecimal.valueOf(2.0));
		productResponse5.setProductId("2");
		
		Set<ProductResponse> products2 = new HashSet<>();
		products2.add(productResponse4);
		products2.add(productResponse5);
		
		ProductPackageResponse productPackageResponse2 = new ProductPackageResponse();
		productPackageResponse2.setDescription("Description 1");
		productPackageResponse2.setName("Name 1");
		productPackageResponse2.setPackageId(1l);
		productPackageResponse2.setProducts(products);
		
    	assertThat(productPackageResponse, equalTo(productPackageResponse2));

	}
	
}
