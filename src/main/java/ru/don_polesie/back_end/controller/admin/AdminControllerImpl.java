package ru.don_polesie.back_end.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.UserDTO;
import ru.don_polesie.back_end.service.AdminService;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public ResponseEntity<Page<UserDTO>> findUsersPage(@RequestParam Integer pageNumber) {
        Page<UserDTO> usersPage = adminService.findUsersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(usersPage);
    }

    @Override
    public ResponseEntity<Page<UserDTO>> findWorkersPage(@RequestParam Integer pageNumber) {
        Page<UserDTO> workersPage = adminService.findWorkersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(workersPage);
    }

    @Override
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        adminService.createUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

