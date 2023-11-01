package com.vipin.shoose.repository;

import com.vipin.shoose.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = "SELECT * FROM product_image WHERE product_id = :productId", nativeQuery = true)
    List<ProductImage> findByProductId(@Param("productId") Long productId);

    List<ProductImage> findByColor(String color);
    @Query(value = "SELECT * FROM product_image WHERE product_id = :productId AND color = :color", nativeQuery = true)
    List<ProductImage> findByProductColor(Long productId, String color);
}
