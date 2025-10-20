package ru.don_polesie.back_end.service;


import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.dto.auth.RegisterRequest;

public interface AuthService {

    JwtAuthResponse login(JwtAuthRequest loginRequest);

    JwtAuthResponse refresh(String refreshToken);

    void save(RegisterRequest request);

}
