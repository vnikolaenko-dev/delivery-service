package ru.don_polesie.back_end.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
