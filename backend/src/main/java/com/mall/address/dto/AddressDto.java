package com.mall.address.dto;

public record AddressDto(Long id, String label, String street, String city, String state, String country, String postalCode, boolean defaultAddress) {
}
