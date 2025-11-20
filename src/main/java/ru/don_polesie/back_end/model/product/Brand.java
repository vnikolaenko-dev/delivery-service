package ru.don_polesie.back_end.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Brand {

    // Константы для валидации
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 100;
    private static final String NAME_REGEX = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.&]+$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = NAME_MAX_LENGTH)
    @NotBlank(message = "Название категории обязательно")
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH,
            message = "Название категории должно содержать от {min} до {max} символов")
    @Pattern(regexp = NAME_REGEX,
            message = "Название категории может содержать только буквы, цифры, пробелы и символы - . &")
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    public Brand(String name) {
        this.name = name;
    }
}
