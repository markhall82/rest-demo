package com.msh.restdemo.controller;

import com.msh.restdemo.domain.request.ProductPackageRequest;
import com.msh.restdemo.domain.response.ProductPackageResponse;
import com.msh.restdemo.product.pack.service.ProductPackageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = PackageController.REQUEST_PATH_API_PACKAGES)
public class PackageController {

    public static final String REQUEST_PATH_API_PACKAGES = "${controller.request.path.api.packages}";
	private final Logger logger = LoggerFactory.getLogger(PackageController.class);
	public static final String REQUEST_PATH_API_PACKAGES_INDIVIDUAL_PACKAGE = "${controller.request.path.api.packages.individual.package}";
    
	@Autowired
	private ProductPackageService productPackageService;

	@ApiOperation(value = "View a list of available packages", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved resource"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Internal server error, contact the service desk") })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CompletableFuture<List<ProductPackageResponse>> list() {
		logger.debug("Fetching package list");
		return productPackageService.findAllPackages();
	}

	@ApiOperation(value = "Search a package with an ID", response = Package.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved resource"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Internal server error, contact the service desk") })
	@GetMapping(value = REQUEST_PATH_API_PACKAGES_INDIVIDUAL_PACKAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductPackageResponse showPackage(@PathVariable Long id,
			@RequestParam(name = "currency", required = false) String currency) {
		logger.debug("Fetching package by id {} with currency {}", id , currency);
		return productPackageService.findProductPackageById(id, currency);
	}

	@ApiOperation(value = "Create a package")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created and retrieved resource"),
			@ApiResponse(code = 201, message = "Resource creation successful"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Internal server error, contact the service desk") })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductPackageResponse> savePackage(
			@RequestBody @Valid ProductPackageRequest productPackage) {
		logger.debug("Creating a new package");
		ProductPackageResponse saved = productPackageService.savePackage(productPackage);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update a package")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created and retrieved resource"),
			@ApiResponse(code = 202, message = "Resource update successful"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Internal server error, contact the service desk") })
	@PutMapping(value = REQUEST_PATH_API_PACKAGES_INDIVIDUAL_PACKAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductPackageResponse> updatePackage(@PathVariable Long id,
			@Valid @RequestBody ProductPackageRequest productPackage) {
		logger.debug("Updating package {}", id);
		ProductPackageResponse saved = productPackageService.updatePackage(id, productPackage);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete a package")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully deleted resource"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 500, message = "Internal server error, contact the service desk") })
	@DeleteMapping(value = REQUEST_PATH_API_PACKAGES_INDIVIDUAL_PACKAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@PathVariable Long id) {
		logger.debug("Deleting package {}", id);
		productPackageService.deletePackage(id);
		return new ResponseEntity<>("Package deleted successfully", HttpStatus.OK);
	}

}