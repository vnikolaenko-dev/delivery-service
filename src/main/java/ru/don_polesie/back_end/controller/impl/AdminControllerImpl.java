package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.controller.AdminController;
import ru.don_polesie.back_end.dto.UserDTO;
import ru.don_polesie.back_end.service.AdminService;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> findUsersPage(@RequestParam Integer pageNumber) {
        return ResponseEntity.ok(adminService.findUsersPage(pageNumber));
    }

    @Override
    @GetMapping("/workers")
    public ResponseEntity<Page<UserDTO>> findWorkersPage(@RequestParam Integer pageNumber) {
        return ResponseEntity.ok(adminService.findWorkersPage(pageNumber));
    }

    @Override
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    @Override
    @PostMapping("/user")
    public ResponseEntity<String> CreateUser(@RequestBody UserDTO userDTO) {
        UserDTO created = adminService.createUser(userDTO);
        return ResponseEntity.ok("User created with id " + created.id);
    }
}

