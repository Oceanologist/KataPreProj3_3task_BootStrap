package ru.kata.spring.boot_security.demo.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "")
    UserRequestDTO toDto(User user);

    User toEntity(UserResponseDTO userResponseDTO);
    @Mapping(target = "")
    User toEntityFromResponse(UserResponseDTO userResponseDTO);


}
