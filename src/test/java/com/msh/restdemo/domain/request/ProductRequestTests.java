package com.msh.restdemo.domain.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.msh.restdemo.domain.request.ProductRequest;

public class ProductRequestTests {
	
	@Test
	public void testProductRequestEqual() {
		ProductRequest productRequest1 = new ProductRequest();
		productRequest1.setProductId("1");
		
		ProductRequest productRequest2 = new ProductRequest();
		productRequest2.setProductId("1");		
		
    	assertThat(productRequest1, equalTo(productRequest2));

	}

}
