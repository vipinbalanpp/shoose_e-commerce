package com.vipin.shoose.repository;

import com.vipin.shoose.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
  User findByEmail(String email);

  User findByUsername(String username);

    User findByUserId(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
