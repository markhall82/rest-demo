package com.msh.restdemo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.msh.restdemo.domain.entities.ProductPackageEntity;

@Repository
public interface ProductPackageRepository extends CrudRepository<ProductPackageEntity, Integer> {
	
    Optional<ProductPackageEntity> findByPackageId(Long packageId);
    
    boolean existsByPackageId(Long packageId);
    
    @Transactional
    void deleteByPackageId(Long packageId);
	
}