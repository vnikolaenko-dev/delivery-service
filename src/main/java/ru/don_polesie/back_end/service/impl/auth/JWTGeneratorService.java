package ru.don_polesie.back_end.service.impl.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.security.admin.JwtTokenProvider;
import ru.don_polesie.back_end.service.impl.StaffServiceImpl;

@Service
@RequiredArgsConstructor
public class JWTGeneratorService {
    private final AuthenticationManager authenticationManager;
    private final StaffServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;


    public JwtAuthResponse generateJWT(String number, String password) {
        var jwtResponse = new JwtAuthResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        number, password)
        );

        // Получение данных пользователя после успешной аутентификации
        var user = userServiceImpl.getByPhoneNumber(number);

        // Формирование ответа с токенами
        jwtResponse.setId(user.getId());
        jwtResponse.setPhoneNumber(number);
        jwtResponse.setPassword(password);
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), number, user.getRoles())
        );
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                user.getId(), number)
        );
        return jwtResponse;
    }
}
