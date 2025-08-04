package com.crud.crud_lombok_dto.service;

import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    boolean validateUser(String email, String password);

    UserResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    List<UserDto> getAllUserByName(String name);

    List<UserDto> getAllUserByUpdatedDate();

    List<UserDto> getAllNameStartWith(String name);


}
