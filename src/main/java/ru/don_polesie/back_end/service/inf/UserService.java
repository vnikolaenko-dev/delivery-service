package ru.don_polesie.back_end.service.inf;


import ru.don_polesie.back_end.model.User;

public interface UserService {
    User getByPhoneNumber(String phoneNumber);

    User getById(Long userId);

}
