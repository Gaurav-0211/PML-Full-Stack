package com.crud.crud_lombok_dto.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(){};

    public UserAlreadyExistException(String message){
        super(message);
    }
}
