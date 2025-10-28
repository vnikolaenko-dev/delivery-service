package ru.don_polesie.back_end.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.security.admin.JwtTokenProvider;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JWTGeneratorService jwtGeneratorService;

    /**
     * Аутентификация пользователя и выдача JWT токенов
     *
     * @param loginRequest запрос с данными для входа (логин и пароль)
     * @return ответ с access и refresh токенами
     */

    public JwtAuthResponse login(JwtAuthRequest loginRequest) {
        return jwtGeneratorService.generateJWT(loginRequest.getPhoneNumber(), loginRequest.getPassword());
    }

    /**
     * Обновление access токена по refresh токену
     *
     * @param refreshToken refresh токен
     * @return новый набор access и refresh токенов
     */

    public JwtAuthResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}