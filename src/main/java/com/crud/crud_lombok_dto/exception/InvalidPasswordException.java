package com.crud.crud_lombok_dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidPasswordException extends RuntimeException {
    private String message;
    public InvalidPasswordException(){};

    public InvalidPasswordException(String message){
        super(message);
        this.message = message;
    }
}
