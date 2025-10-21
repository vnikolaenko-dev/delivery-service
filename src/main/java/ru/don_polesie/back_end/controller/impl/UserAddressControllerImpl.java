package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.UserAddressController;
import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.mapper.AddressMapper;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.impl.UserAddressServiceImpl;
import ru.don_polesie.back_end.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAddressControllerImpl implements UserAddressController {
    private final UserAddressServiceImpl userAddressService;
    private final SecurityUtils securityUtils;

    @Override
    public ResponseEntity<List<AddressDTO>> getUserAddress() {
        User user = securityUtils.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(userAddressService.getUserAddresses(user));
    }

    @Override
    public ResponseEntity<String> createUserAddress(AddressDTO address) {
        User user = securityUtils.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAddressService.save(address, user));
    }

    @Override
    public ResponseEntity<Void> deleteUserAddress(Long id) {
        User user = securityUtils.getCurrentUser();
        userAddressService.delete(id, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
