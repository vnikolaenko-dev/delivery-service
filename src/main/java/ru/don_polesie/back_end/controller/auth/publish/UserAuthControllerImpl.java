package ru.don_polesie.back_end.controller.auth.publish;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.auth.response.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.request.JwtRefreshRequest;
import ru.don_polesie.back_end.service.auth.UserAuthService;

import javax.management.BadAttributeValueExpException;

@RestController
@AllArgsConstructor
@RequestMapping("/auth/user")
@Log4j2
public class UserAuthControllerImpl {
    private final UserAuthService userAuthService;

    @Operation(
            summary = "Отправить пользователю 4-х значный код на номер телефона"
    )
    @PostMapping("/get-password")
    public ResponseEntity<JwtAuthResponse> getPassword(@RequestParam String phoneNumber) throws BadAttributeValueExpException {
        userAuthService.sendTemporaryPassword(phoneNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(
            summary = "Проверить временный пароль",
            description = "Аутентификация пользователя и получение JWT токенов (access + refresh)"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> checkPassword(@RequestParam String phoneNumber, @RequestParam String password) throws BadAttributeValueExpException {
        JwtAuthResponse jwtAuthResponse = userAuthService.checkTemporaryPassword(phoneNumber, password);
        log.info("{} enter to system ", phoneNumber);
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
        JwtAuthResponse jwtAuthResponse = userAuthService.refresh(request.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}
