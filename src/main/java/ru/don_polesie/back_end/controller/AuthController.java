package ru.don_polesie.back_end.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.JwtRefreshRequest;
@RequestMapping("/api/admin/auth")
public interface AuthController {


    @PostMapping("/login")
    ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid JwtAuthRequest loginRequest);

    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody @Valid JwtAuthRequest request);

    @PostMapping("/refresh")
    ResponseEntity<JwtAuthResponse> refresh(@RequestBody @Valid JwtRefreshRequest request);
}
