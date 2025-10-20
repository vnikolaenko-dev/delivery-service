package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.UserDTO;
import ru.don_polesie.back_end.model.Role;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.UserRepository;
import ru.don_polesie.back_end.service.AdminService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // если нужен пароль

    @Override
    public Page<UserDTO> findUsersPage(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        return userRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<UserDTO> findWorkersPage(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        return userRepository.findByRoles_Name("WORKER", pageable)
                .map(this::toDTO);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id.longValue());
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.name);
        user.setEmail(dto.email);
        user.setPhoneNumber(dto.phoneNumber);
        user.setRoles(dto.roles);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // при необходимости
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId().intValue();
        dto.name = user.getUsername();
        dto.email = user.getEmail();
        dto.phoneNumber = user.getPhoneNumber();
        dto.roles = user.getRoles();
        return dto;
    }
}

