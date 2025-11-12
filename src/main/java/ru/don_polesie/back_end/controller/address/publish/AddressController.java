package ru.don_polesie.back_end.controller.address.publish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.user.AddressDTO;
import ru.don_polesie.back_end.model.user.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.userOnly.UserAddressService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Адреса доставки",
        description = "API для управления адресами доставки пользователя"
)
@RequestMapping("/api/address")
public class AddressController {
    private final UserAddressService userAddressService;
    private final SecurityUtils securityUtils;

    @Operation(
            summary = "Получить адреса пользователя",
            description = "Возвращает список всех адресов доставки текущего пользователя"
    )
    @GetMapping
    public ResponseEntity<List<AddressDTO>> findAll() {
        User user = securityUtils.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(userAddressService.getUserAddresses(user));
    }

    @Operation(
            summary = "Добавить адрес доставки",
            description = "Создает новый адрес доставки для текущего пользователя"
    )
    @PostMapping()
    public ResponseEntity<String> createUserAddress(@RequestBody AddressDTO address) {
        System.out.println(address);
        User user = securityUtils.getCurrentUser();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAddressService.save(address, user));
    }

    @Operation(
            summary = "Удалить адрес доставки",
            description = "Удаляет адрес доставки по идентификатору"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable @Min(0) Long id) {
        User user = securityUtils.getCurrentUser();
        userAddressService.delete(id, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
