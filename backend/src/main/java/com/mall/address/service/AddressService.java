package com.mall.address.service;

import java.util.List;

import com.mall.address.dto.AddressDto;
import com.mall.address.dto.AddressRequest;

public interface AddressService {
    AddressDto addAddress(String userEmail, AddressRequest request);
    AddressDto updateAddress(String userEmail, Long addressId, AddressRequest request);
    void deleteAddress(String userEmail, Long addressId);
    List<AddressDto> getAddresses(String userEmail);
}
