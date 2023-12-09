package com.vipin.shoose.repository;

import com.vipin.shoose.model.Cart;
import com.vipin.shoose.model.Variant;
import org.hibernate.NonUniqueResultException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.crypto.spec.PSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    Cart findByUserId(@Param("userId") Long userId);



}
