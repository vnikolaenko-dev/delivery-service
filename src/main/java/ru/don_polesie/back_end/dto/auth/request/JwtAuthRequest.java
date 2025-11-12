package ru.don_polesie.back_end.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;
}
