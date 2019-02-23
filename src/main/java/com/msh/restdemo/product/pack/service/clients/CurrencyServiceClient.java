package com.msh.restdemo.product.pack.service.clients;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msh.restdemo.common.client.RestClientTemplate;

public interface CurrencyServiceClient {

	public List<String> getCurrencies() throws IOException;

	BigDecimal convert(String currency, Long amount) throws IOException;

	@Service
	public class Default implements CurrencyServiceClient {

		private final Logger logger = LoggerFactory.getLogger(CurrencyServiceClient.class);

		@Autowired
		@Qualifier("currencyStoreTemplate")
		private RestClientTemplate currencyStoreTemplate;

		@Override
		@Cacheable
		public List<String> getCurrencies() throws IOException {
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(currencyStoreTemplate.getForObject("/currencies", String.class));
			Iterator<String> iterator = node.fieldNames();

			Iterable<String> iterable = () -> iterator;
			return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
		}
		
		@Override
		@Cacheable
		public BigDecimal convert(String currency, Long amount) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(currencyStoreTemplate.getForObject(String.format("/latest?amount=%d&from=USD&to=%s", amount, currency), String.class));
			Double converted = node.findValue(currency).asDouble();
			return BigDecimal.valueOf(converted);
		}
	}
}
