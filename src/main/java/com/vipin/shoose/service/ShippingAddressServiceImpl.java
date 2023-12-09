package com.vipin.shoose.service;

import com.vipin.shoose.model.Address;
import com.vipin.shoose.model.OrderInfo;
import com.vipin.shoose.model.ShippingAddress;
import com.vipin.shoose.repository.AddressRepository;
import com.vipin.shoose.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService{
    @Autowired
    ShippingAddressRepository shippingAddressRepository;
    @Autowired
    AddressRepository addressRepository;
    @Override
    public ShippingAddress  createOrderShippingAddress(Long addressId) {
        System.out.println("shipping addresss method");
        Address address=addressRepository.findByAddressId(addressId);
        ShippingAddress shippingAddress=new ShippingAddress();
        shippingAddress.setFullName(address.getFullName());
        shippingAddress.setPhoneNumber(address.getPhoneNumber());
        shippingAddress.setBuildingName(address.getBuildingName());
        shippingAddress.setStreetName(address.getStreetName());
        shippingAddress.setCity(address.getCity());
        shippingAddress.setState(address.getState());
        shippingAddress.setPostalCode(address.getPostalCode());
        shippingAddressRepository.save(shippingAddress);
        return shippingAddress;
    }
}
