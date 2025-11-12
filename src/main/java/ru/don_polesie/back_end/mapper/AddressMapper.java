package ru.don_polesie.back_end.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.don_polesie.back_end.dto.user.AddressDTO;
import ru.don_polesie.back_end.model.user.Address;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO toDto(Address address);

    List<AddressDTO> toDtoList(List<Address> addresses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO addressDto);

    // Метод для обновления сущности из DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDto(AddressDTO addressDto, @MappingTarget Address address);
}
