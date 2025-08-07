package com.crud.crud_lombok_dto.exception;

import com.crud.crud_lombok_dto.config.AppConstants;
import com.crud.crud_lombok_dto.dto.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchUserExistException.class)
    public ResponseEntity<BaseResponse> handleNoSuchUserExistException(
            NoSuchUserExistException ex, HttpServletRequest request) {

        BaseResponse apiResponse = new BaseResponse(
                ex.getMessage(),
                "FAILED",
                AppConstants.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(AppConstants.NOT_FOUND));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<BaseResponse> handleUserAlreadyExistException(
            UserAlreadyExistException ex, HttpServletRequest request) {

        BaseResponse apiResponse = new BaseResponse(
                ex.getMessage(),
                "FAILED",
                AppConstants.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(AppConstants.CONFLICT));
    }


    @ExceptionHandler(NoUserExist.class)
    public ResponseEntity<BaseResponse> handleNoUserExistException(NoUserExist ex, HttpServletRequest request) {
        BaseResponse apiResponse = new BaseResponse(
                ex.getMessage(),
                "FAILED",
                AppConstants.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(AppConstants.NOT_FOUND));
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        BaseResponse apiResponse = new BaseResponse(
                "Request method not supported",
                "FAILED",
                AppConstants.METHOD_NOT_ALLOWED,
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(AppConstants.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        BaseResponse apiResponse = new BaseResponse(
                errorMessages,
                "FAILED",
                AppConstants.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(AppConstants.BAD_REQUEST));
    }

}
