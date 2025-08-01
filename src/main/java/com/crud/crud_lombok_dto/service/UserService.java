package com.crud.crud_lombok_dto.service;

import com.crud.crud_lombok_dto.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);


}
