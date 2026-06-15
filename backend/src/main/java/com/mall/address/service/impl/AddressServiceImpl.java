package com.mall.address.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.address.dto.AddressDto;
import com.mall.address.dto.AddressRequest;
import com.mall.address.entity.Address;
import com.mall.address.repository.AddressRepository;
import com.mall.address.service.AddressService;
import com.mall.auth.entity.User;
import com.mall.auth.exception.UserNotFoundException;
import com.mall.auth.repository.UserRepository;
import com.mall.address.exception.AddressNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public AddressDto addAddress(String userEmail, AddressRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.defaultAddress()) {
            addressRepository.findByUserAndDefaultAddressTrue(user)
                    .ifPresent(existing -> {
                        existing.setDefaultAddress(false);
                        addressRepository.save(existing);
                    });
        }

        Address address = new Address();
        address.setUser(user);
        address.setLabel(request.label());
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setState(request.state());
        address.setCountry(request.country());
        address.setPostalCode(request.postalCode());
        address.setDefaultAddress(request.defaultAddress());
        return toDto(addressRepository.save(address));
    }

    @Override
    public AddressDto updateAddress(String userEmail, Long addressId, AddressRequest request) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (request.defaultAddress()) {
            addressRepository.findByUserAndDefaultAddressTrue(user)
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(addressId)) {
                            existing.setDefaultAddress(false);
                            addressRepository.save(existing);
                        }
                    });
        }

        address.setLabel(request.label());
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setState(request.state());
        address.setCountry(request.country());
        address.setPostalCode(request.postalCode());
        address.setDefaultAddress(request.defaultAddress());
        return toDto(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(String userEmail, Long addressId) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        addressRepository.delete(address);
    }

    @Override
    public List<AddressDto> getAddresses(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return addressRepository.findByUserOrderByDefaultAddressDescCreatedAtDesc(user).stream().map(this::toDto).toList();
    }

    private AddressDto toDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getLabel(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.isDefaultAddress());
    }
}
