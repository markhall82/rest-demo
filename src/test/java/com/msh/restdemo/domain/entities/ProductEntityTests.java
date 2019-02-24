package com.msh.restdemo.domain.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.msh.restdemo.domain.request.ProductRequest;

public class ProductEntityTests {
	
	@Test
	public void testProductEntitiestEqual() {
		 ProductEntity productEntity1 = new  ProductEntity();
		 productEntity1.setProductId(1l);
		 productEntity1.setIdentifier("A1");
		
		 ProductEntity productEntity2 = new  ProductEntity();
		 productEntity2.setProductId(1l);
		 productEntity2.setIdentifier("A1");	
		
    	assertThat(productEntity1, equalTo(productEntity2));

	}

}
