package com.crud.crud_lombok_dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidOtpException extends RuntimeException {
    private String message;
    public InvalidOtpException(){}

    public InvalidOtpException(String message) {
        super();
        this.message = message;
    }

}
