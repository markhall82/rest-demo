package com.restdemo.product.pack.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyLong;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.msh.restdemo.domain.entities.ProductEntity;
import com.msh.restdemo.domain.entities.ProductPackageEntity;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;
import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.request.ProductRequest;
import com.msh.restdemo.domain.response.ProductPackageResponse;
import com.msh.restdemo.domain.response.ProductResponse;
import com.msh.restdemo.exception.FailureAfterPersistException;
import com.msh.restdemo.product.pack.service.ProductPackageService;
import com.msh.restdemo.product.pack.service.ProductPackageServiceImpl;
import com.msh.restdemo.product.pack.service.clients.CurrencyServiceClient;
import com.msh.restdemo.product.pack.service.clients.ProductServiceClient;
import com.msh.restdemo.repository.ProductPackageRepository;

public class ProductPackageServiceTest {

	@InjectMocks
	private ProductPackageService productPackageService = new ProductPackageServiceImpl();

	@Mock
	private ProductServiceClient productServiceClient;

	@Mock
	private CurrencyServiceClient currencyServiceClient;

	@Mock
	private ProductPackageRepository productPackageRepository;

	private ProductPackageRequest productPackageRequest;
	private ProductRequest productRequest1;
	private ProductRequest productRequest2;
	private ProductRequest productRequest3;

	private ProductEntity productEntity1;
	private ProductEntity productEntity2;
	private ProductEntity productEntity3;

	private Set<ProductEntity> productEntities;

	private ProductPackageEntity productPackageEntity;
	private ProductResponse productResponse1;
	private ProductResponse productResponse2;
	private ProductResponse productResponse3;
	
	private ProductServiceProductResponse productServiceProductResponse1;
	private ProductServiceProductResponse productServiceProductResponse2;
	private ProductServiceProductResponse productServiceProductResponse3;
	
	private List<String> currencies;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		productPackageRequest = new ProductPackageRequest();
		productRequest1 = new ProductRequest("productId1");
		productRequest2 = new ProductRequest("productId2");
		productRequest3 = new ProductRequest("productId3");

		Set<ProductRequest> products = new HashSet<>();
		products.add(productRequest1);
		products.add(productRequest2);
		products.add(productRequest3);

		productPackageRequest.setName("Package name");
		productPackageRequest.setDescription("Package description");
		productPackageRequest.setProducts(products);

		productEntity1 = new ProductEntity("productId1");
		productEntity2 = new ProductEntity("productId2");
		productEntity3 = new ProductEntity("productId3");

		productEntities = new HashSet<>();
		productEntities.add(productEntity1);
		productEntities.add(productEntity2);
		productEntities.add(productEntity3);

		productPackageEntity = new ProductPackageEntity();
		productPackageEntity.setPackageId(1l);
		productPackageEntity.setName("Package name");
		productPackageEntity.setDescription("Package description");
		productPackageEntity.setProducts(productEntities);

		productResponse1 = new ProductResponse();
		productResponse1.setProductId("productId1");
		productResponse1.setName("Name1");
		productResponse1.setPrice(BigDecimal.valueOf(1.00));

		productResponse2 = new ProductResponse();
		productResponse2.setProductId("productId2");
		productResponse2.setName("Name2");
		productResponse2.setPrice(BigDecimal.valueOf(2.00));

		productResponse3 = new ProductResponse();
		productResponse3.setProductId("productId3");
		productResponse3.setName("Name3");
		productResponse3.setPrice(BigDecimal.valueOf(3.00));

		productServiceProductResponse1 = new ProductServiceProductResponse();
		productServiceProductResponse1.setId("productId1");
		productServiceProductResponse1.setName("Name1");
		productServiceProductResponse1.setUsdPrice(100l);
		
		productServiceProductResponse2 = new ProductServiceProductResponse();
		productServiceProductResponse2.setId("productId2");
		productServiceProductResponse2.setName("Name2");
		productServiceProductResponse2.setUsdPrice(200l);
		
		productServiceProductResponse3 = new ProductServiceProductResponse();
		productServiceProductResponse3.setId("productId3");
		productServiceProductResponse3.setName("Name3");
		productServiceProductResponse3.setUsdPrice(300l);
		
		String GBP = "GBP";
		String USD = "USD";
		currencies = new ArrayList<>();
		currencies.add(GBP);
		currencies.add(USD);
	}

	@Test
	public void savePackageSuccessTest() {

		// ProductServiceProductResponse productServiceProductResponse3 = new
		// ProductServiceProductResponse();
		// productServiceProductResponse3.setId("productId1");
		// productServiceProductResponse3.setName("Name1");
		// productServiceProductResponse3.setUsdPrice(100l);

		// when(productStoreTemplate.getForObject(any(String.class),
		// ProductServiceProductResponse.class)).thenReturn(productServiceProductResponse1,
		// productServiceProductResponse2, productServiceProductResponse3);

		when(productPackageRepository.save(any(ProductPackageEntity.class))).thenReturn(productPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);

		ProductPackageResponse productPackageResponse = productPackageService.savePackage(productPackageRequest);

		verify(productServiceClient, times(3)).getProductServiceProduct(anyString());
		verify(productPackageRepository).save(any(ProductPackageEntity.class));

		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.00).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}

	@Test(expected = FailureAfterPersistException.class)
	public void savePackageExceptionTest() {
		when(productPackageRepository.save(any(ProductPackageEntity.class))).thenReturn(productPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString()))
				.thenThrow(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT));

		productPackageService.savePackage(productPackageRequest);
	}
	
	@Test
	public void updatePackageTest() {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productPackageRepository.save(any(ProductPackageEntity.class))).thenReturn(productPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);

		ProductPackageResponse productPackageResponse = productPackageService.updatePackage(1l, productPackageRequest);

		verify(productServiceClient, times(3)).getProductServiceProduct(anyString());
		verify(productPackageRepository).save(any(ProductPackageEntity.class));

		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.00).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}
	
	@Test(expected = FailureAfterPersistException.class)
	public void updatePackageExceptionTest() {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productPackageRepository.save(any(ProductPackageEntity.class))).thenReturn(productPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString()))
				.thenThrow(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT));

		productPackageService.updatePackage(1l, productPackageRequest);
	}
	
	@Test
	public void findProductPackageByIdTest() {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);

		ProductPackageResponse productPackageResponse = productPackageService.findProductPackageById(1l, null);

		verify(productServiceClient, times(3)).getProductServiceProduct(anyString());
		verify(productPackageRepository).findByPackageId(anyLong());

		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.00).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}
	
	@Test
	public void findProductPackageByIdWithCurrencyTest() throws IOException {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);

		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);
		when(currencyServiceClient.getCurrencies()).thenReturn(currencies);
		when(currencyServiceClient.convert(anyString(), anyLong())).thenReturn(BigDecimal.valueOf(205).setScale(2, RoundingMode.CEILING));

		ProductPackageResponse productPackageResponse = productPackageService.findProductPackageById(1l, "GBP");

		verify(productServiceClient, times(3)).getProductServiceProduct(anyString());
		verify(productPackageRepository).findByPackageId(anyLong());

		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.15).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}

	@Test
	public void findProductPackageByIdWithUSDTest() throws IOException {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);
		when(currencyServiceClient.getCurrencies()).thenReturn(currencies);
		ProductPackageResponse productPackageResponse = productPackageService.findProductPackageById(1l, "USD");

		verify(productServiceClient, times(3)).getProductServiceProduct(anyString());
		verify(productPackageRepository).findByPackageId(anyLong());

		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.00).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}

	
	@Test(expected = ResponseStatusException.class)
	public void findProductPackageByIdBadCurrencyTest() throws IOException {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString()))
				.thenThrow(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT));
		when(currencyServiceClient.getCurrencies()).thenReturn(currencies);

		productPackageService.findProductPackageById(1l, "BAD");
	}
	
	@Test(expected = ResponseStatusException.class)
	public void findProductPackageByIdIOExceptionCurrencyTest() throws IOException {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(currencyServiceClient.getCurrencies()).thenThrow(new IOException());
		productPackageService.findProductPackageById(1l, "BAD");
	}
	
	@Test(expected = CompletionException.class)
	public void findProductPackageByIdExceptionTest() {
		Optional<ProductPackageEntity> OptionalproductPackageEntity = Optional.of(productPackageEntity);
		when(productPackageRepository.findByPackageId(anyLong())).thenReturn(OptionalproductPackageEntity);
		when(productServiceClient.getProductServiceProduct(anyString()))
				.thenThrow(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT));

		productPackageService.findProductPackageById(1l, null);
	}	
	
	@Test(expected = CompletionException.class)
	public void findAllPackagesExceptionTest() {
		List<ProductPackageEntity> productPackageEntityList = new ArrayList<>();
		productPackageEntityList.add(productPackageEntity);
		when(productPackageRepository.findAll()).thenReturn(productPackageEntityList);
		when(productServiceClient.getProductServiceProduct(anyString()))
				.thenThrow(new HttpClientErrorException(HttpStatus.GATEWAY_TIMEOUT));
		productPackageService.findAllPackages().join();
	}	

	
	@Test
	public void findAllPackagesTest() {
		List<ProductPackageEntity> productPackageEntityList = new ArrayList<>();
		productPackageEntityList.add(productPackageEntity);
		when(productPackageRepository.findAll()).thenReturn(productPackageEntityList);
		when(productServiceClient.getProductServiceProduct(anyString())).thenReturn(productServiceProductResponse1,
				productServiceProductResponse2, productServiceProductResponse3);
		
		List<ProductPackageResponse> productPackageResponseList = productPackageService.findAllPackages().join();
		ProductPackageResponse productPackageResponse = productPackageResponseList.get(0);
		
		assertThat(productPackageResponse.getPackageId(), is(1l));
		assertThat(productPackageResponse.getPrice(), is(BigDecimal.valueOf(6.00).setScale(2, RoundingMode.CEILING)));
		assertThat(productPackageResponse.getDescription(), is("Package description"));
		assertThat(productPackageResponse.getName(), is("Package name"));
		assertThat(productPackageResponse.getProducts().size(), is(3));
	}	
	
	@Test
	public void deletePackageByIdTest() {
		when(productPackageRepository.existsByPackageId(anyLong())).thenReturn(true);
		productPackageService.deletePackage(1l);
		verify(productPackageRepository).existsByPackageId(anyLong());
		verify(productPackageRepository).deleteByPackageId(anyLong());

	}
	
	@Test(expected = ResponseStatusException.class)
	public void deletePackageByIdExceptionTest() {
		when(productPackageRepository.existsByPackageId(anyLong())).thenReturn(false);
		productPackageService.deletePackage(1l);
	}
	
}
