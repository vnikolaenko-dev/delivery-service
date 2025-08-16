package ru.don_polesie.back_end.service;


import ru.don_polesie.back_end.dto.auth.JwtAuthRequest;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;

public interface AuthService {

    JwtAuthResponse login(JwtAuthRequest loginRequest);

    JwtAuthResponse refresh(String refreshToken);

    void save(JwtAuthRequest request);

}
