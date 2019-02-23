package com.msh.restdemo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.request.ProductRequest;
import com.msh.restdemo.domain.response.ProductPackageResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PackageControllerMockMvcTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext wac;

	ProductPackageRequest productPackageRequest1;
	ProductRequest productRequest1;
	ProductRequest productRequest2;
	ProductRequest productRequest3;

	ProductPackageRequest productPackageRequest2;
	ProductRequest productRequest4;
	ProductRequest productRequest5;
	ProductRequest productRequest6;
	
	ProductPackageRequest productPackageRequest3;
	ProductRequest productRequest7;
	ProductRequest productRequest8;
	ProductRequest productRequest9;

	@Before
	public void setup() {
		productPackageRequest1 = new ProductPackageRequest();
		productRequest1 = new ProductRequest("VqKb4tyj9V6i");
		productRequest2 = new ProductRequest("DXSQpv6XVeJm");
		productRequest3 = new ProductRequest("7dgX6XzU3Wds");

		final Set<ProductRequest> products1 = new HashSet<>();
		products1.add(productRequest1);
		products1.add(productRequest2);
		products1.add(productRequest3);

		productPackageRequest1.setName("Package name 1");
		productPackageRequest1.setDescription("Package description 1");
		productPackageRequest1.setProducts(products1);

		productPackageRequest2 = new ProductPackageRequest();
		productRequest4 = new ProductRequest("VqKb4tyj9V6i");
		productRequest5 = new ProductRequest("INVALID");
		productRequest6 = new ProductRequest("7dgX6XzU3Wds");

		final Set<ProductRequest> products2 = new HashSet<>();
		products2.add(productRequest4);
		products2.add(productRequest5);
		products2.add(productRequest6);

		productPackageRequest2.setName("Package name 2");
		productPackageRequest2.setDescription("Package description 2");
		productPackageRequest2.setProducts(products2);
		
		productPackageRequest3 = new ProductPackageRequest();
		productRequest4 = new ProductRequest("VqKb4tyj9V6i");
		productRequest5 = new ProductRequest("DXSQpv6XVeJm");

		final Set<ProductRequest> products3 = new HashSet<>();
		products3.add(productRequest4);
		products3.add(productRequest5);
		products3.add(productRequest6);

		productPackageRequest3.setName("Package name 3");
		productPackageRequest3.setDescription("Package description 3");
		productPackageRequest3.setProducts(products3);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testCRUDWithMockMVC() throws Exception {

		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);

		MvcResult postResult = mockMvc
				.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isCreated()).andReturn();

		ProductPackageResponse productPackageResponse = getProductPackageResponse(postResult);

		assertCRUDPostAndGetTest(productPackageResponse);
		
		long packageId = productPackageResponse.getPackageId();
		MvcResult getResult = mockMvc.perform(get(String.format("/v1/packages/%d", packageId)))
				.andExpect(status().isOk()).andReturn();

		productPackageResponse = getProductPackageResponse(getResult);

		assertCRUDPostAndGetTest(productPackageResponse);

		productPackageRequest1.setName("updated name");
		productPackageRequest1.setDescription("updated description");
		productRequest1 = new ProductRequest("VqKb4tyj9V6i");
		final Set<ProductRequest> products = new HashSet<>();
		products.add(productRequest1);
		productPackageRequest1.setProducts(products);

		jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		
		MvcResult putResult = mockMvc.perform(put(String.format("/v1/packages/%d", packageId))
				.contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk()).andReturn();

		productPackageResponse = assetCRUDPutTest(putResult);

		packageId = productPackageResponse.getPackageId();
		getResult = mockMvc.perform(get(String.format("/v1/packages/%d", packageId))).andExpect(status().isOk())
				.andReturn();

		productPackageResponse = getProductPackageResponse(getResult);

		productPackageResponse = assetCRUDPutTest(putResult);

		packageId = productPackageResponse.getPackageId();
		this.mockMvc.perform(delete(String.format("/v1/packages/%d", packageId))).andExpect(status().isOk())
				.andReturn();
		mockMvc.perform(get(String.format("/v1/packages/%d", packageId))).andExpect(status().isNotFound()).andReturn();
	}

	private ProductPackageResponse assetCRUDPutTest(MvcResult putResult)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		ProductPackageResponse productPackageResponse;
		productPackageResponse = getProductPackageResponse(putResult);

		assertThat(productPackageResponse.getName(), is("updated name"));
		assertThat(productPackageResponse.getDescription(), is("updated description"));
		assertThat(productPackageResponse.getPrice(), greaterThan(BigDecimal.valueOf(0)));
		assertThat(productPackageResponse.getProducts().size(), is(1));
		return productPackageResponse;
	}

	@SuppressWarnings("unchecked")
	private void assertCRUDPostAndGetTest(ProductPackageResponse productPackageResponse) {
		assertThat(productPackageResponse.getName(), is("Package name 1"));
		assertThat(productPackageResponse.getDescription(), is("Package description 1"));
		assertThat(productPackageResponse.getPrice(), greaterThan(BigDecimal.valueOf(0)));
		assertThat(productPackageResponse.getProducts(), containsInAnyOrder(
						hasProperty("productId", is("VqKb4tyj9V6i")),
						hasProperty("productId", is("DXSQpv6XVeJm")), 
						hasProperty("productId", is("7dgX6XzU3Wds"))));
		
		assertThat(productPackageResponse.getProducts(), containsInAnyOrder(
				hasProperty("price", greaterThan(BigDecimal.valueOf(0))),
				hasProperty("price", greaterThan(BigDecimal.valueOf(0))),
				hasProperty("price", greaterThan(BigDecimal.valueOf(0)))));
	
		assertThat(productPackageResponse.getProducts(), containsInAnyOrder(
				hasProperty("name", is(notNullValue())),
				hasProperty("name", is(notNullValue())),
				hasProperty("name", is(notNullValue()))));
	}

	@Test
	public void testCreateInvalidProductIdWithMockMVC() throws Exception {
		String jsonRequest = mapper.writeValueAsString(productPackageRequest2);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateProductNoNameWithMockMVC() throws Exception {
		productPackageRequest1.setName(null);
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateProductEmptyNameWithMockMVC() throws Exception {
		productPackageRequest1.setName("");
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateProductEmptyProductSetWithMockMVC() throws Exception {
		productPackageRequest1.setProducts(new HashSet<>());
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateProductNullProductSetWithMockMVC() throws Exception {
		productPackageRequest1.setProducts(null);
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateProductEmptyProductWithMockMVC() throws Exception {
		Set<ProductRequest> products = new HashSet<>();
		ProductRequest prod = new ProductRequest();
		products.add(prod);
		productPackageRequest1.setProducts(products);
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);
		this.mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetProductWithCurrency() throws Exception {
		String jsonRequest = mapper.writeValueAsString(productPackageRequest1);

		MvcResult postResult = mockMvc
				.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andExpect(status().isCreated()).andReturn();
		ProductPackageResponse productPackageResponse = getProductPackageResponse(postResult);
		
		long packageId = productPackageResponse.getPackageId();
		MvcResult getResult = mockMvc.perform(get(String.format("/v1/packages/%d", packageId)))
				.andExpect(status().isOk()).andReturn();

		productPackageResponse = getProductPackageResponse(getResult);
		
		BigDecimal priceBefore = productPackageResponse.getPrice();
		
		MvcResult getResultWithcurrency = mockMvc.perform(get(String.format("/v1/packages/%d", packageId))
				.param("currency", "GBP"))
				.andExpect(status().isOk()).andReturn();

		productPackageResponse = getProductPackageResponse(getResultWithcurrency);
		
		BigDecimal priceAfter = productPackageResponse.getPrice();

		assertThat(priceBefore, is(not(priceAfter)));
		
		this.mockMvc.perform(get(String.format("/v1/packages/%d", packageId))
				.param("currency", "bad"))
				.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(delete(String.format("/v1/packages/%d", packageId))).andExpect(status().isOk())
				.andReturn();
		mockMvc.perform(get(String.format("/v1/packages/%d", packageId))).andExpect(status().isNotFound());

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetProductList() throws Exception {
		String jsonRequest1 = mapper.writeValueAsString(productPackageRequest1);
		String jsonRequest3 = mapper.writeValueAsString(productPackageRequest3);

		mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest1))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/v1/packages").contentType(MediaType.APPLICATION_JSON).content(jsonRequest3))
				.andExpect(status().isCreated());
		
		MvcResult mvcResult = mockMvc.perform(get("/v1/packages/"))
                .andExpect(request().asyncStarted())
                .andReturn();
		
		MvcResult postResult = mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk()).andReturn();
		
		List<ProductPackageResponse> productPackageResponseList = getProductPackageResponseList(postResult);
		
		assertThat(productPackageResponseList.size(), greaterThan(1));
		
		assertThat(productPackageResponseList, containsInAnyOrder(
				hasProperty("description", is("Package description 1")),
				hasProperty("description", is("Package description 3"))));
	}

	private ProductPackageResponse getProductPackageResponse(MvcResult postResult)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String postJsonResult = postResult.getResponse().getContentAsString();
		TypeReference<ProductPackageResponse> postTypeRef = new TypeReference<ProductPackageResponse>() {
		};
		ProductPackageResponse productPackageResponse = mapper.readValue(postJsonResult, postTypeRef);
		return productPackageResponse;
	}
	
	private List<ProductPackageResponse> getProductPackageResponseList(MvcResult postResult)
			throws UnsupportedEncodingException, IOException, JsonParseException, JsonMappingException {
		String postJsonResult = postResult.getResponse().getContentAsString();
		TypeReference<List<ProductPackageResponse>> postTypeRef = new TypeReference<List<ProductPackageResponse>>() {
		};
		List<ProductPackageResponse> getProductPackageResponseList = mapper.readValue(postJsonResult, postTypeRef);
		return getProductPackageResponseList;
	}
}
