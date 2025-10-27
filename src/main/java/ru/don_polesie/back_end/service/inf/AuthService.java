package ru.don_polesie.back_end.service.inf;


import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;

public interface AuthService {

    JwtAuthResponse login(JwtAuthRequest loginRequest);

    JwtAuthResponse refresh(String refreshToken);


}
