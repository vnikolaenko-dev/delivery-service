package ru.don_polesie.back_end.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.don_polesie.back_end.dto.address.request.AddressDtoRequest;
import ru.don_polesie.back_end.dto.address.response.AddressDtoResponse;
import ru.don_polesie.back_end.model.user.Address;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDtoResponse toDto(Address address);

    List<AddressDtoResponse> toDtoList(List<Address> addresses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDtoResponse addressDto);

    // Метод для обновления сущности из DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDto(AddressDtoResponse addressDto, @MappingTarget Address address);

    Address toEntityFromAddressDtoRequest(AddressDtoRequest addressDTO);
}
