package ru.don_polesie.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.don_polesie.back_end.model.user.Address;
import ru.don_polesie.back_end.model.user.User;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);

    void deleteByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);
}
