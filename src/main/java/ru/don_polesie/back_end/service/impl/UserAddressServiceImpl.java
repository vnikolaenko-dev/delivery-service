package ru.don_polesie.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.don_polesie.back_end.dto.AddressDTO;
import ru.don_polesie.back_end.mapper.AddressMapper;
import ru.don_polesie.back_end.mapper.OrderMapper;
import ru.don_polesie.back_end.model.Address;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.AddressRepository;
import ru.don_polesie.back_end.service.UserAddressService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Получает список адресов пользователя
     *
     * @param user пользователь, для которого запрашиваются адреса
     * @return список адресов в формате DTO
     */
    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        return addressRepository.findAllByUser(user)
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    /**
     * Сохраняет новый адрес для пользователя
     *
     * @param addressDTO данные адреса для сохранения
     * @param user пользователь, для которого сохраняется адрес
     * @return строковое представление сохраненного адреса
     */
    @Override
    public String save(AddressDTO addressDTO, User user) {
        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(user);
        addressRepository.save(address);
        return address.toString();
    }

    /**
     * Удаляет адрес пользователя по идентификатору
     *
     * @param id идентификатор адреса для удаления
     * @param user пользователь, для проверки прав доступа
     * @throws IllegalArgumentException если адрес с указанным id не найден
     */
    @Override
    public void delete(Long id, User user) {
        if (!addressRepository.existsById(id)) {
            throw new IllegalArgumentException(String.format("Address %n not found.", id));
        }
        addressRepository.deleteById(id);
    }
}