package com.msh.restdemo.product.pack.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.msh.restdemo.domain.entities.ProductPackageEntity;
import com.msh.restdemo.domain.product.service.response.ProductServiceProductResponse;
import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.response.ProductResponse;
import com.msh.restdemo.exception.FailureAfterPersistException;
import com.msh.restdemo.domain.response.ProductPackageResponse;
import com.msh.restdemo.repository.ProductPackageRepository;
import com.msh.restdemo.product.pack.service.clients.CurrencyServiceClient;
import com.msh.restdemo.product.pack.service.clients.ProductServiceClient;

public interface ProductPackageService {

	ProductPackageResponse findProductPackageById(Long id, String currency);

	CompletableFuture<List<ProductPackageResponse>> findAllPackages();

	ProductPackageResponse updatePackage(Long id, ProductPackageRequest productPackage);

	ProductPackageResponse savePackage(ProductPackageRequest productPackageEntity);

	void deletePackage(Long id);

	@Service("productPackageService")
	public class Default implements ProductPackageService {

		private static final String INVALID_CURRENCY = "Invalid currency";

		private static final String PACKAGE_NOT_FOUND = "Package not found";

		private final Logger logger = LoggerFactory.getLogger(ProductPackageService.class);

		@Autowired
		private ProductPackageRepository productPackageRepository;

		@Autowired
		private ProductServiceClient productClient;

		@Autowired
		private CurrencyServiceClient currencyClient;

		@Override
		public CompletableFuture<List<ProductPackageResponse>> findAllPackages() {
			Iterable<ProductPackageEntity> allPackages = productPackageRepository.findAll();
			List<ProductPackageResponse> productPackageResponseList = new ArrayList<>();
			allPackages.forEach(entity -> productPackageResponseList.add(ProductPackageResponse.from(entity)));

			List<CompletableFuture<ProductPackageResponse>> productPackageFutures = productPackageResponseList.stream()
					.map(this::getProductPackageAsync).collect(Collectors.toList());

			CompletableFuture<Void> allFutures = CompletableFuture
					.allOf(productPackageFutures.toArray(new CompletableFuture[productPackageFutures.size()]));

			return allFutures.thenApply(v -> productPackageFutures.stream()
					.map(CompletableFuture<ProductPackageResponse>::join).collect(Collectors.toList()));
		}

		@Override
		public ProductPackageResponse findProductPackageById(Long id, String currency) {
			currency = "USD".equals(currency) ? null : currency;
			try {
				if (currency != null && !currencyClient.getCurrencies().contains(currency)) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CURRENCY);
				}
			} catch (IOException ex) {
				logAndRethrowAsResponseStatusException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			ProductPackageResponse productPackageResponse = ProductPackageResponse
					.from(productPackageRepository.findByPackageId(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PACKAGE_NOT_FOUND)));
			return populateProducts(productPackageResponse, currency);
		}

		@Override
		public ProductPackageResponse savePackage(ProductPackageRequest productPackage) {
			ProductPackageEntity toSave = ProductPackageEntity.from(productPackage);
			ProductPackageResponse productPackageResponse = ProductPackageResponse
					.from(productPackageRepository.save(toSave));
			try {
				return populateProducts(productPackageResponse, null);
			} catch(CompletionException e){
				logger.error("Error saving package {}", e);
				throw new FailureAfterPersistException(productPackageResponse.getPackageId());
			}		
		}

		@Override
		public ProductPackageResponse updatePackage(Long packageId, ProductPackageRequest productPackage) {
			ProductPackageEntity productPackageEntity = productPackageRepository.findByPackageId(packageId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PACKAGE_NOT_FOUND));
			productPackageEntity.setEntityFrom(productPackage);
			ProductPackageResponse productPackageResponse = ProductPackageResponse
					.from(productPackageRepository.save(productPackageEntity));
			try {
				return populateProducts(productPackageResponse, null);
			} catch(CompletionException e){
				throw new FailureAfterPersistException(productPackageResponse.getPackageId());
			}
		}

		@Override
		public void deletePackage(Long id) {
			if (!productPackageRepository.existsByPackageId(id)) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, PACKAGE_NOT_FOUND);
			}
			productPackageRepository.deleteByPackageId(id);
		}

		private ProductPackageResponse populateProducts(ProductPackageResponse productPackageResponse,
				String currency) {

			populateProductsParallel(productPackageResponse, currency);

			return productPackageResponse;
		}

		private void populateProductsParallel(ProductPackageResponse productPackage, String currency) {
			Set<ProductResponse> productsWithIdOnly = productPackage.getProducts();
			List<CompletableFuture<ProductResponse>> productFutures = productsWithIdOnly.stream()
					.map(p -> getProductAsync(p.getProductId(), currency)).collect(Collectors.toList());

			CompletableFuture<Void> allFutures = CompletableFuture
					.allOf(productFutures.toArray(new CompletableFuture[productFutures.size()]));
			CompletableFuture<Set<ProductResponse>> allPageContentsFuture = allFutures.thenApply(v -> productFutures
					.stream().map(CompletableFuture<ProductResponse>::join).collect(Collectors.toSet()));
			productPackage.setProducts(allPageContentsFuture.join());

		}

		private ProductResponse getProductAndConveryCurrency(String productId, String currency) {
			ProductServiceProductResponse productServiceProductResponse = productClient.getProductServiceProduct(productId);
			ProductResponse productResponse = ProductResponse.from(productServiceProductResponse);
			if (Strings.isNotEmpty(currency)) {
				try {
					productResponse
							.setPrice(currencyClient.convert(currency, productServiceProductResponse.getUsdPrice())
									.divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.CEILING));
				} catch (IOException ex) {
					logAndRethrowAsResponseStatusException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return productResponse;
		}

		private void logAndRethrowAsResponseStatusException(IOException ex, HttpStatus httpStatus) {
			String correlationId = UUID.randomUUID().toString();
			logger.error("APP_CORRLATION_ID: {}", correlationId, ex);
			throw new ResponseStatusException(httpStatus,
					String.format("Error correlation Id: %s", correlationId));
		}
		
		private CompletableFuture<ProductResponse> getProductAsync(String productId, String currency) {
			return CompletableFuture.supplyAsync(() -> getProductAndConveryCurrency(productId, currency));
		}

		private CompletableFuture<ProductPackageResponse> getProductPackageAsync(
				ProductPackageResponse productPackageResponse) {
			return CompletableFuture.supplyAsync(() -> populateProducts(productPackageResponse, null));
		}

	}
}
