package com.vipin.shoose.repository;

import com.vipin.shoose.model.Address;
import com.vipin.shoose.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User currentUser);

    Address findByAddressId(Long addressId);
}
