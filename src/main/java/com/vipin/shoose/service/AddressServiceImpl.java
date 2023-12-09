package com.vipin.shoose.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vipin.shoose.dto.AddressDto;
import com.vipin.shoose.exception.CustomException;
import com.vipin.shoose.model.Address;
import com.vipin.shoose.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserService userService;
    @Override
    public void save(AddressDto addressDto) {
      try {
          Address address=new Address();
          address.setFullName(addressDto.getFullName());
          address.setPhoneNumber(addressDto.getPhoneNumber());
          address.setBuildingName(addressDto.getBuildingName());
          address.setStreetName(addressDto.getStreetName());
          address.setCity(addressDto.getCity());
          address.setState(addressDto.getState());
          address.setPostalCode(addressDto.getPostalCode());
          address.setUser(userService.getCurrentUser());
          addressRepository.save(address);
      }catch (Exception e){
          throw  new RuntimeException("address not saved");
      }

    }

    @Override
    public List<Address> getCurrentUserAddresses() {
        try {
            return addressRepository.findByUser(userService.getCurrentUser());
        }catch (Exception e){
            throw new CustomException("An error occurred");
        }


    }


    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public void editAddress(Long addressId,AddressDto addressDto) {
        try {
           Address address= addressRepository.findByAddressId(addressId);
            if(!Objects.equals(addressDto.getFullName(), "")){
                address.setFullName(addressDto.getFullName());
            }
            if (!Objects.equals(addressDto.getBuildingName(),"")){
                address.setBuildingName(addressDto.getBuildingName());
            }
            if (!Objects.equals(addressDto.getStreetName(),"")){
                address.setStreetName(addressDto.getStreetName());
            }
            if (!Objects.equals(addressDto.getCity(),"")){
                address.setCity(addressDto.getCity());
            }
            if (!Objects.equals(addressDto.getState(),"")){
                address.setState(addressDto.getState());
            }
            if (!Objects.equals(addressDto.getPostalCode(),"")){
                address.setPostalCode(addressDto.getPostalCode());
            }
            addressRepository.save(address);
        }catch (Exception e){
            throw new RuntimeException("address not found");
        }


    }

    @Override
    public List<AddressDto> getCurrentUserAddressDtos() throws JsonProcessingException {
        List<AddressDto> addressDtos=new ArrayList<>();
        ObjectMapper objectMapper=new ObjectMapper();
        List<Address>addresses= userService.getAddresses(userService.getCurrentUser());
        for(Address a:addresses){
            AddressDto addressDto=new AddressDto();
            addressDto.setAddressId(String.valueOf(a.getAddressId()));
            addressDto.setFullName(a.getFullName());
            addressDto.setPhoneNumber(a.getPhoneNumber());
            addressDto.setBuildingName(a.getBuildingName());
            addressDto.setStreetName(a.getStreetName());
            addressDto.setCity(a.getCity());
            addressDto.setState(a.getState());
            addressDto.setPostalCode(a.getPostalCode());
            objectMapper.writeValueAsString(addressDto);
            addressDtos.add(addressDto);
        }return addressDtos;
    }
}
