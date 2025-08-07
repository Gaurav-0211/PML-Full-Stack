package com.crud.crud_lombok_dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseResponse {
        private String message;
        private String success;
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String path;

        public BaseResponse(String message, String success, int status, String error, String path) {
            this.message = message;
            this.success = success;
            this.status = status;
            this.error = error;
            this.path = path;
            this.timestamp = LocalDateTime.now();
        }
}
