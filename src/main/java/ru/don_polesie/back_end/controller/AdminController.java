package ru.don_polesie.back_end.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.UserDTO;

@Tag(
        name = "Администрирование",
        description = "API для управления пользователями и административных функций"
)
@RequestMapping("/api/admin")
public interface AdminController {
    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает постраничный список всех зарегистрированных пользователей системы"
    )
    @GetMapping("/users")
    ResponseEntity<Page<UserDTO>> findUsersPage(@RequestParam @Min(value = 1) Integer pageNumber);

    @Operation(
            summary = "Получить список работников",
            description = "Возвращает постраничный список всех сотрудников с правами работника"
    )
    @GetMapping("/workers")
    ResponseEntity<Page<UserDTO>> findWorkersPage(@RequestParam @Min(value = 1) Integer pageNumber);

    @Operation(
            summary = "Удалить пользователя",
            description = "Полностью удаляет пользователя из системы по его идентификатору"
    )
    @DeleteMapping("/delete/{id}}")
    ResponseEntity<String> deleteUser(@PathVariable Integer id);

    @Operation(
            summary = "Создать пользователя",
            description = "Создает нового пользователя с указанными данными и ролями"
    )
    @GetMapping("/create}")
    ResponseEntity<String> CreateUser(UserDTO userDTO);


}
