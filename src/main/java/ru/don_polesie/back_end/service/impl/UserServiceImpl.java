package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.UserRepository;
import ru.don_polesie.back_end.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Находит пользователя по имени пользователя
     *
     * @param username имя пользователя для поиска
     * @return найденный пользователь
     * @throws ObjectNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(""));
    }

    /**
     * Находит пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     * @throws ObjectNotFoundException если пользователь с указанным id не найден
     */
    @Override
    public User getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(""));
    }
}