package com.vipin.shoose.service;

import com.vipin.shoose.dto.AddressDto;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    void save(AddressDto addressDto);

    Object getCurrentUserAddresses();

    void deleteAddress(Long addressId);

    void editAddress(Long addressId,AddressDto addressDto);
}
