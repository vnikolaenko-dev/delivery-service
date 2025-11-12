package ru.don_polesie.back_end.service.staffOnly;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.model.user.User;
import ru.don_polesie.back_end.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffService {

    private final UserRepository userRepository;

    /**
     * Находит пользователя по имени пользователя
     *
     * @param phoneNumber номер пользователя для поиска
     * @return найденный пользователь
     * @throws ObjectNotFoundException если пользователь с указанным именем не найден
     */

    public User getByPhoneNumber(String phoneNumber) {
        return userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с номером телефона " + phoneNumber + " не найден"
                ));
    }


    /**
     * Находит пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     * @throws ObjectNotFoundException если пользователь с указанным id не найден
     */

    public User getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(""));
    }
}