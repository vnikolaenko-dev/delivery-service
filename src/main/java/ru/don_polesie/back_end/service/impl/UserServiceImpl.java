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

    @Override
    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException(""));
    }

    @Override
    public User getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(""));
    }

}
