package com.msh.restdemo.product.pack.service;


import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.msh.restdemo.domain.request.ProductPackageRequest;

import com.msh.restdemo.domain.response.ProductPackageResponse;


public interface ProductPackageService {

	ProductPackageResponse findProductPackageById(Long id, String currency);

	CompletableFuture<List<ProductPackageResponse>> findAllPackages();

	ProductPackageResponse updatePackage(Long id, ProductPackageRequest productPackage);

	ProductPackageResponse savePackage(ProductPackageRequest productPackageEntity);

	void deletePackage(Long id);

	
}
