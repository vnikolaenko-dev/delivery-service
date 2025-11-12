package ru.don_polesie.back_end.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.user.UserDtoResponse;
import ru.don_polesie.back_end.service.staffOnly.AdminService;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Администрирование",
        description = "API для управления пользователями и административных функций"
)
@RequestMapping("/api/admin")
public class AdminUserManageController {

    private final AdminService adminService;

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает постраничный список всех зарегистрированных пользователей системы"
    )
    @GetMapping("/users")
    public ResponseEntity<Page<UserDtoResponse>> findUsersPage(@RequestParam @Min(0) Integer pageNumber) {
        Page<UserDtoResponse> usersPage = adminService.findUsersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(usersPage);
    }

    @Operation(
            summary = "Получить список работников",
            description = "Возвращает постраничный список всех сотрудников с правами работника"
    )
    @GetMapping("/workers")
    public ResponseEntity<Page<UserDtoResponse>> findWorkersPage(@RequestParam @Min(0) Integer pageNumber) {
        Page<UserDtoResponse> workersPage = adminService.findWorkersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(workersPage);
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Создает пользователя с заданными ролями"
    )
    @PostMapping("/user/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDtoResponse userDtoResponse) {
        adminService.createUser(userDtoResponse);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет пользователя"
    )
    @PostMapping("/user/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserDtoResponse userDtoResponse) {
        adminService.createUser(userDtoResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Полностью удаляет пользователя из системы по его идентификатору"
    )
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(0) Long id) {
        adminService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

