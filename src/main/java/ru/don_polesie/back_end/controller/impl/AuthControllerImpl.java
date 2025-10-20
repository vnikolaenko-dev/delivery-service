package ru.don_polesie.back_end.controller.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.AuthController;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.JwtRefreshRequest;
import ru.don_polesie.back_end.dto.auth.RegisterRequest;
import ru.don_polesie.back_end.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authServiceImpl;

    @Override
    public ResponseEntity<JwtAuthResponse> login(JwtAuthRequest loginRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.login(loginRequest));
    }

    @Override
    public ResponseEntity<Void> register(RegisterRequest request) {
        authServiceImpl.save(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<JwtAuthResponse> refresh(JwtRefreshRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authServiceImpl.refresh(request.getRefreshToken()));
    }
}
