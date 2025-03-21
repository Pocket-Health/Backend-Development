package ru.ffanjex.backenddevelopment.DTO.mapper;

import org.mapstruct.Mapper;
import ru.ffanjex.backenddevelopment.DTO.RegisterUserDTO;
import ru.ffanjex.backenddevelopment.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RegisterUserDTO toDTO(User user);

    User toEntity(RegisterUserDTO registerUserDTO);
}
