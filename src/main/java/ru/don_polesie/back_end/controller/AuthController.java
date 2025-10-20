package ru.don_polesie.back_end.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.JwtRefreshRequest;
import ru.don_polesie.back_end.dto.auth.RegisterRequest;

@Tag(
        name = "Аутентификация",
        description = "API для регистрации, входа и обновления JWT токенов"
)
@RequestMapping("/auth")
public interface AuthController {

    @Operation(
            summary = "Вход в систему",
            description = "Аутентификация пользователя и получение JWT токенов (access + refresh)"
    )
    @PostMapping("/login")
    ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid JwtAuthRequest loginRequest);

    @Operation(
            summary = "Регистрация",
            description = "Создание нового аккаунта пользователя в системе"
    )
    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request);

    @Operation(
            summary = "Обновление токена",
            description = "Получение новой пары access/refresh токенов по действующему refresh токену"
    )
    @PostMapping("/refresh")
    ResponseEntity<JwtAuthResponse> refresh(@RequestBody @Valid JwtRefreshRequest request);
}
