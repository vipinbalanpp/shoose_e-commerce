package com.vipin.shoose.repository;

import com.vipin.shoose.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByProductName(String productName);

    Product findByProductId(Long id);

    @Query(value = "SELECT * FROM product WHERE is_enabled = 'true'",nativeQuery = true)
    List<Product> findByEnabled();
}
