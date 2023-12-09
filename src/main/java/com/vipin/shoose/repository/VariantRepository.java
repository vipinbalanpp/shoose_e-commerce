package com.vipin.shoose.repository;

import com.vipin.shoose.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    @Query(value = "SELECT * FROM variant WHERE product_id = :productId", nativeQuery = true)
    List<Variant> findByProductId(@Param("productId") Long productId);
    @Query(value = "SELECT DISTINCT color FROM variant WHERE product_id = :productId", nativeQuery = true)
    List<String> findColorsByProductId(@Param("productId") Long productId);

    Variant findByVariantId(Long variantId);
    @Query(value = "SELECT  size FROM variant WHERE product_id = :productId AND color = :selectedColor", nativeQuery = true)
    List<Long> findAllSizesByColor(Long productId, String selectedColor);
    @Query(value = "SELECT  * FROM variant WHERE product_id = :productId AND color = :color AND size = :size", nativeQuery = true)
    Variant findByProductIdColorAndSize(Long productId, String color, Long size);
}

