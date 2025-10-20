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

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        return addressRepository.findAllByUser(user)
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public String save(AddressDTO addressDTO, User user) {
        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(user);
        addressRepository.save(address);
        return address.toString();
    }

    @Override
    public boolean delete(int id, User user) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
