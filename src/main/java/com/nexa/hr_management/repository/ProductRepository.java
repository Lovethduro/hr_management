package com.nexa.hr_management.repository;

import com.nexa.hr_management.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom queries can be added here
    List<Product> findByPriceLessThan(BigDecimal price);
}