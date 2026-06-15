package com.mall.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mall.address.dto.AddressDto;
import com.mall.address.dto.AddressRequest;
import com.mall.address.service.AddressService;
import com.mall.auth.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                 @Valid @RequestBody AddressRequest request) {
        AddressDto dto = addressService.addAddress(currentUser.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                    @PathVariable Long addressId,
                                                    @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(currentUser.getEmail(), addressId, request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                              @PathVariable Long addressId) {
        addressService.deleteAddress(currentUser.getEmail(), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(addressService.getAddresses(currentUser.getEmail()));
    }
}
