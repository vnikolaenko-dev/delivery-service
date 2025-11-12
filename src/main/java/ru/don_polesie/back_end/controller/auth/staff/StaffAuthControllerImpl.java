package ru.don_polesie.back_end.controller.auth.staff;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.auth.request.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.response.JwtAuthResponse;
import ru.don_polesie.back_end.service.auth.StaffAuthService;

@Tag(
        name = "Аутентификация",
        description = "API для регистрации, входа и обновления JWT токенов"
)
@RequestMapping("/auth/staff")
@RestController
@RequiredArgsConstructor
public class StaffAuthControllerImpl {
    private final StaffAuthService authService;

    @Operation(
            summary = "Вход в систему",
            description = "Аутентификация пользователя и получение JWT токенов (access + refresh)"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest loginRequest) {
        JwtAuthResponse jwtAuthResponse = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}
