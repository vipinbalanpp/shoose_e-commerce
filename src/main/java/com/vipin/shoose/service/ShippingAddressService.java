package com.vipin.shoose.service;

import com.vipin.shoose.model.OrderInfo;
import com.vipin.shoose.model.ShippingAddress;

public interface ShippingAddressService {
    ShippingAddress createOrderShippingAddress(Long addressId);
}
