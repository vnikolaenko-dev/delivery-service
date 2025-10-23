package ru.don_polesie.back_end.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.AddressDTO;

import java.util.List;

@Tag(
        name = "Адреса доставки",
        description = "API для управления адресами доставки пользователя"
)
@RequestMapping("/api/address")
public interface UserAddressController {
    @Operation(
            summary = "Получить мои адреса",
            description = "Возвращает список всех адресов доставки текущего пользователя"
    )
    @GetMapping("")
    ResponseEntity<List<AddressDTO>> getUserAddress();

    @Operation(
            summary = "Добавить адрес доставки",
            description = "Создает новый адрес доставки для текущего пользователя"
    )
    @PostMapping("/create")
    ResponseEntity<String> createUserAddress(@RequestBody AddressDTO address);

    @Operation(
            summary = "Удалить адрес доставки",
            description = "Удаляет адрес доставки по идентификатору"
    )
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteUserAddress(@PathVariable Long id);
}
