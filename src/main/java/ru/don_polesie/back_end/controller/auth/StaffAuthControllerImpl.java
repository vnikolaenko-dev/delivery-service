package ru.don_polesie.back_end.controller.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.JwtRefreshRequest;
import ru.don_polesie.back_end.dto.auth.RegisterRequest;
import ru.don_polesie.back_end.service.AuthService;

@RestController
@RequiredArgsConstructor
public class StaffAuthControllerImpl implements StaffAuthController {
    private final AuthService authServiceImpl;

    @Override
    public ResponseEntity<JwtAuthResponse> login(JwtAuthRequest loginRequest) {
        JwtAuthResponse jwtAuthResponse = authServiceImpl.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }

    @Override
    public ResponseEntity<JwtAuthResponse> refresh(JwtRefreshRequest request) {
        JwtAuthResponse jwtAuthResponse = authServiceImpl.refresh(request.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}
