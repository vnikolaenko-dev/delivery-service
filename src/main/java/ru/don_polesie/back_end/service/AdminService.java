package ru.don_polesie.back_end.service;

import org.springframework.data.domain.Page;
import ru.don_polesie.back_end.dto.UserDTO;

public interface AdminService {

    Page<UserDTO> findUsersPage(Integer pageNumber);

    Page<UserDTO> findWorkersPage(Integer pageNumber);

    void deleteUser(Long id);

    void createUser(UserDTO userDTO);
}

