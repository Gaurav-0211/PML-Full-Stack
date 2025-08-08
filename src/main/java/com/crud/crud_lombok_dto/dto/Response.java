package com.crud.crud_lombok_dto.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 7104462920542626419L;

    private String status;

    @JsonProperty("status_code")
    private int statusCode;

    private String message;

    private Object data;

    private String response_message;

    private LocalDateTime timestamp;

    public static Response buildResponse(String status,  String message, Object data,int statusCode, String response_message) {
        Response response = new Response();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        response.setStatusCode(statusCode);
        response.setResponse_message(response_message);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
