package com.msh.restdemo.product.pack.service.clients;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.msh.restdemo.common.client.RestClientTemplate;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;
import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.request.ProductRequest;
import com.msh.restdemo.domain.response.ProductResponse;

public class ProductServiceClientTest {

	@InjectMocks
	private ProductServiceClient productServiceClient = new ProductServiceClient.Default();
	
	@Mock
	private RestClientTemplate productStoreTemplate;
	
	@Mock
	CurrencyServiceClient currencyServiceClient;
	
	private ProductServiceProductResponse productServiceProductResponse;
	
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.productServiceProductResponse = new ProductServiceProductResponse();
        this.productServiceProductResponse.setId("abc123");
        this.productServiceProductResponse.setName("a product");
        this.productServiceProductResponse.setUsdPrice(100l);
    }
    
    
}
