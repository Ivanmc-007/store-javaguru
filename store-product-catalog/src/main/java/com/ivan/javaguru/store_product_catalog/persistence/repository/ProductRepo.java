package com.ivan.javaguru.store_product_catalog.persistence.repository;

import com.ivan.javaguru.store_product_catalog.persistence.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
