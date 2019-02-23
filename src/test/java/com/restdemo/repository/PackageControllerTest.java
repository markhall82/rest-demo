package com.restdemo.repository;

import static org.hamcrest.MatcherAssert.assertThat;  
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.msh.restdemo.controller.PackageController;
import com.msh.restdemo.domain.entities.ProductPackageEntity;
import com.msh.restdemo.domain.response.ProductPackageResponse;
import com.msh.restdemo.product.pack.service.ProductPackageService;
import com.msh.restdemo.product.pack.service.clients.CurrencyServiceClient;
import com.msh.restdemo.product.pack.service.clients.ProductServiceClient;
import com.msh.restdemo.repository.ProductPackageRepository;

public class PackageControllerTest {

    @InjectMocks
    private PackageController packageController;

    @Mock
    private ProductPackageService productPackageService;
 

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
    }

    @Test
    public void testGetUserById() {
    	ProductPackageResponse p =  new ProductPackageResponse();
        p.setPackageId(1l);
        when(productPackageService.findProductPackageById(1l, null)).thenReturn(p);

        ProductPackageResponse productPackageResponse = packageController.showPackage(1l, null);

        verify(productPackageService).findProductPackageById(1l, null);
        
        assertThat(productPackageResponse.getPackageId(), is(1l));
    }
}
