package com.crud.crud_lombok_dto.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoSuchUserExistException extends RuntimeException {

    private String message;
    public NoSuchUserExistException(){};

    public NoSuchUserExistException(String message){
        super(message);
        this.message = message;
    }
}
