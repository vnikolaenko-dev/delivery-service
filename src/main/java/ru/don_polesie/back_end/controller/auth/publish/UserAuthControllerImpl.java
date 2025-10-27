package ru.don_polesie.back_end.controller.auth.publish;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.JwtRefreshRequest;
import ru.don_polesie.back_end.service.impl.auth.UserAuthService;
import ru.don_polesie.back_end.service.inf.AuthService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/user")
public class UserAuthControllerImpl {
    private final UserAuthService userAuthService;
    private final AuthService authServiceImpl;

    @PostMapping("/get-password")
    public ResponseEntity<JwtAuthResponse> getPassword(@RequestParam String phoneNumber) {
        userAuthService.sendTemporaryPassword(phoneNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> checkPassword(@RequestParam String phoneNumber, @RequestParam String password) {
        JwtAuthResponse jwtAuthResponse = userAuthService.checkTemporaryPassword(phoneNumber, password);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }

    @Operation(
            summary = "Обновление токена",
            description = "Получение новой пары access/refresh токенов по действующему refresh токену"
    )
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(JwtRefreshRequest request) {
        JwtAuthResponse jwtAuthResponse = authServiceImpl.refresh(request.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}
