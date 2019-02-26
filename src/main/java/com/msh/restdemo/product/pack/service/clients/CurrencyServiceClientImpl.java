package com.msh.restdemo.product.pack.service.clients;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msh.restdemo.common.client.RestClientTemplate;

@Service
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

	@Autowired
	@Qualifier("currencyStoreTemplate")
	private RestClientTemplate currencyStoreTemplate;

	@Value("${service.currency.currencies.url}")
	private String currenciesUrl;
	
	@Value("${service.currency.convert.url}")
	private String converyCurrencyUrl;
	
	@Override
	@Cacheable("currencyServiceLong")
	public List<String> getCurrencies() throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(currencyStoreTemplate.getForObject(currenciesUrl, String.class));
		Iterator<String> iterator = node.fieldNames();

		Iterable<String> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
	}
	
	@Override
	@Cacheable("currencyService")
	public BigDecimal convert(String currency, Long amount) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(currencyStoreTemplate.getForObject(String.format(converyCurrencyUrl, amount, currency), String.class));
		Double converted = node.findValue(currency).asDouble();
		return BigDecimal.valueOf(converted);
	}
}