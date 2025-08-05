package com.crud.crud_lombok_dto.service;

import com.crud.crud_lombok_dto.dto.EmailEntity1;
import com.crud.crud_lombok_dto.dto.MailEntity;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(long id);
    boolean validateUser(String email, String password);

    UserResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    List<UserDto> getAllUserByName(String name);

    List<UserDto> getAllUserByUpdatedDate();

    List<UserDto> getAllNameStartWith(String name);

    List<UserDto> getAllNameEndWith(String name);

    List<UserDto> getAllNameDesc();

    public void sendEmail(MailEntity mailEntity);

    public void sendMailToAllUsers(EmailEntity1 emailEntity1);


}
