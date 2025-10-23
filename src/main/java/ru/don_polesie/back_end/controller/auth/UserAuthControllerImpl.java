package ru.don_polesie.back_end.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.service.impl.auth.UserAuthService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/user")
public class UserAuthControllerImpl {
    private final UserAuthService userAuthService;

    @PostMapping("/get-password")
    public ResponseEntity<JwtAuthResponse> getPassword(@RequestParam String phoneNumber) {
        userAuthService.sendTemporaryPassword(phoneNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> checkPassword(@RequestParam String phoneNumber, @RequestParam String password) {
        System.out.println(phoneNumber + " " + password);
        JwtAuthResponse jwtAuthResponse = userAuthService.checkTemporaryPassword(phoneNumber, password);
        System.out.println(jwtAuthResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}
