package ru.don_polesie.back_end.service;


import ru.don_polesie.back_end.model.User;

public interface UserService {
    User getByPhoneNumber(String phoneNumber);

    User getById(Long userId);

}
