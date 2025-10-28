package ru.don_polesie.back_end.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.UserDTO;
import ru.don_polesie.back_end.service.staffOnly.AdminService;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Администрирование",
        description = "API для управления пользователями и административных функций"
)
@RequestMapping("/api/admin")
public class AdminControllerImpl {

    private final AdminService adminService;

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает постраничный список всех зарегистрированных пользователей системы"
    )
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> findUsersPage(@RequestParam Integer pageNumber) {
        Page<UserDTO> usersPage = adminService.findUsersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(usersPage);
    }

    @Operation(
            summary = "Получить список работников",
            description = "Возвращает постраничный список всех сотрудников с правами работника"
    )
    @GetMapping("/workers")
    public ResponseEntity<Page<UserDTO>> findWorkersPage(@RequestParam Integer pageNumber) {
        Page<UserDTO> workersPage = adminService.findWorkersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(workersPage);
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Создает пользователя с заданными ролями"
    )
    @PostMapping("/user/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        adminService.createUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Полностью удаляет пользователя из системы по его идентификатору"
    )
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

