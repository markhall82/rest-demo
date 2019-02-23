package com.msh.restdemo.product.pack.service.clients;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.msh.restdemo.common.client.RestClientTemplate;

public class CurrencyServiceClientTest {

	@InjectMocks
	private CurrencyServiceClient currencyServiceClient = new CurrencyServiceClient.Default();
	
	@Mock
	private RestClientTemplate productStoreTemplate;
	
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void getCurrenciesTest() throws IOException {
    	when(productStoreTemplate.getForObject(anyString(), any(Class.class))).thenReturn("{\"AUD\":\"Australian Dollar\",\"BGN\":\"Bulgarian Lev\"}");
    	List<String> currencies = currencyServiceClient.getCurrencies();
    	assertThat(currencies, contains("AUD","BGN"));

    }
    
	@Test
    @SuppressWarnings("unchecked")
    public void convertTest() throws IOException {
    	when(productStoreTemplate.getForObject(anyString(), any(Class.class))).thenReturn("{\"amount\":100.0,\"base\":\"USD\",\"date\":\"2019-02-21\",\"rates\":{\"GBP\":76.449}}");
    	BigDecimal converted = currencyServiceClient.convert("GBP", 100l);
    	assertThat(converted, is(BigDecimal.valueOf(76.449)));

    }
    
}
