package ru.don_polesie.back_end.service.inf;

import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.model.User;

import java.util.List;

public interface UserAddressService {
    List<AddressDTO> getUserAddresses(User user);

    String save(AddressDTO address, User user);

    void delete(Long id, User user);
}
