package com.crud.crud_lombok_dto.exception;

import com.crud.crud_lombok_dto.config.AppConstants;
import com.crud.crud_lombok_dto.dto.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SignatureException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchUserExistException.class)
    public ResponseEntity<Response> handleNoSuchUserExistException(NoSuchUserExistException ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.NOT_FOUND,
                "Request not found"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.NOT_FOUND));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Response> handleUserAlreadyExistException(UserAlreadyExistException ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.CONFLICT,
                "User Already Exist with this Id"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.CONFLICT));
    }

    @ExceptionHandler(NoUserExist.class)
    public ResponseEntity<Response> handleNoUserExistException(NoUserExist ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.NOT_FOUND,
                "User Not Found With this Id"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.NOT_FOUND));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                "Request method not supported",
                null,
                AppConstants.METHOD_NOT_ALLOWED,
                "Unsuccessful Request"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Response response = Response.buildResponse(
                "FAILED",
                errorMessages,
                null,
                AppConstants.BAD_REQUEST,
                "Data Insufficient For the Request"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalException(Exception ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.BAD_REQUEST,
                "Illegal Argument Value"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.BAD_REQUEST));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Response> InvalidPasswordException(InvalidPasswordException ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.SC_UNAUTHORIZED,
                "Entered Old Password is Incorrect"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.SC_UNAUTHORIZED));
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<Response> InvalidOtpException(InvalidOtpException ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                AppConstants.NOT_FOUND,
                "Entered Otp is Incorrect"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.NOT_FOUND));
    }

}
