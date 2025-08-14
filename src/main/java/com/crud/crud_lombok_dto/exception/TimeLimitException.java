package com.crud.crud_lombok_dto.exception;

public class TimeLimitException extends RuntimeException{
    private String message;

    public TimeLimitException (){}

    public TimeLimitException(String message){
        super(message);
        this.message = message;
    }
}
