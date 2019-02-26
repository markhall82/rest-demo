package com.msh.restdemo.product.pack.service.clients;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.List;



public interface CurrencyServiceClient {

	public List<String> getCurrencies() throws IOException;

	BigDecimal convert(String currency, Long amount) throws IOException;


}
