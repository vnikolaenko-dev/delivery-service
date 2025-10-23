package ru.don_polesie.back_end.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponse {
    private Long id;
    private String phoneNumber;
    private String password;
    private String accessToken;
    private String refreshToken;
}
