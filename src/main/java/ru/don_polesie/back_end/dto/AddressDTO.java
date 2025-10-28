package ru.don_polesie.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {
    private Long id;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotNull
    private Integer houseNumber;

    private Integer apartmentNumber;

    private Integer floor;
}
