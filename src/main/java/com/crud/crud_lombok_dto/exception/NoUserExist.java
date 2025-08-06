package com.crud.crud_lombok_dto.exception;

public class NoUserExist extends RuntimeException{

    public NoUserExist(){};

    public NoUserExist(String message){
        super(message);
    }
}
