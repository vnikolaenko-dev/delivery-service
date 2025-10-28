package ru.don_polesie.back_end.service.staffOnly;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.UserDTO;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
    // Константы для пагинации и ролей
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String WORKER_ROLE_NAME = "WORKER";
    private static final String USER_ROLE_NAME = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получает страницу с пользователями (роль USER)
     *
     * @param pageNumber номер страницы (начинается с 1)
     * @return страница с пользователями в формате DTO
     */

    public Page<UserDTO> findUsersPage(Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        Page<User> usersPage = userRepository.findByRolesName(USER_ROLE_NAME, pageable);
        return usersPage.map(this::toDTO);
    }

    /**
     * Получает страницу с работниками (роль WORKER)
     *
     * @param pageNumber номер страницы (начинается с 1)
     * @return страница с работниками в формате DTO
     */

    public Page<UserDTO> findWorkersPage(Integer pageNumber) {
        Pageable pageable = createDefaultPageable(pageNumber);
        Page<User> workersPage = userRepository.findByRolesName(WORKER_ROLE_NAME, pageable);
        return workersPage.map(this::toDTO);
    }

    /**
     * Удаляет пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     */

    public void deleteUser(Long id) {
        userRepository.deleteById(id.longValue());
    }

    /**
     * Создает нового пользователя
     *
     * @param userDTO данные пользователя для создания
     */

    public void createUser(UserDTO userDTO) {
        User user = createUserFromDTO(userDTO);
        userRepository.save(user);
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    /**
     * Создает объект пагинации с настройками по умолчанию
     *
     * @param pageNumber номер страницы (начинается с 1, преобразуется в 0-based)
     * @return настроенный объект Pageable
     */
    private Pageable createDefaultPageable(Integer pageNumber) {
        int page = pageNumber != null ? Math.max(0, pageNumber - 1) : 0;
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_FIELD).descending());
    }

    /**
     * Преобразует сущность User в DTO
     *
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId().intValue();
        dto.name = user.getName();
        dto.surname = user.getSurname();
        dto.email = user.getEmail();
        dto.phoneNumber = user.getPhoneNumber();
        dto.roles = user.getRoles();
        return dto;
    }

    /**
     * Создает сущность User из DTO с шифрованием пароля
     *
     * @param userDTO DTO с данными пользователя
     * @return сущность User с зашифрованным паролем
     */
    private User createUserFromDTO(UserDTO userDTO) {
        String password = userDTO.getPassword();
        return User.builder()
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(passwordEncoder.encode(password))
                .roles(userDTO.getRoles())
                .build();
    }
}