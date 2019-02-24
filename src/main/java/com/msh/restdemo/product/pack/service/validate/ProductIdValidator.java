package com.msh.restdemo.product.pack.service.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.msh.restdemo.product.pack.service.clients.ProductServiceClient;

public class ProductIdValidator implements ConstraintValidator<ProductIdCheck, String> {

	private final Logger logger = LoggerFactory.getLogger(ProductIdValidator.class);

	@Autowired
	private ProductServiceClient productClient;

	@Override
	public boolean isValid(String productIdentifier, ConstraintValidatorContext context) {
		try {
			return productIdentifier.equals(productClient.getProductServiceProduct(productIdentifier).getId());
		} catch (final HttpClientErrorException ex) {
			HttpStatus status = ex.getStatusCode();
			if (status == HttpStatus.NOT_FOUND) {
				logger.info("Unable to locate product id in product service {}", productIdentifier);
				return false;
			} else {
				throw ex;
			}
		} 
	}

}
