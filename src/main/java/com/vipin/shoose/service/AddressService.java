package com.vipin.shoose.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vipin.shoose.dto.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AddressService {
    void save(AddressDto addressDto);

    Object getCurrentUserAddresses();

    void deleteAddress(Long addressId);

    void editAddress(Long addressId,AddressDto addressDto);

    List<AddressDto> getCurrentUserAddressDtos() throws JsonProcessingException;
}
