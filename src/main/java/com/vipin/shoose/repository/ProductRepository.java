package com.vipin.shoose.repository;

import com.vipin.shoose.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByProductName(String productName);

    Product findByProductId(Long id);
}
