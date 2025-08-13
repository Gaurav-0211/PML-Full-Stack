package com.crud.crud_lombok_dto.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MailEntity {

    @NotEmpty(message = "Recipient Name is mandatory It can't be empty")
    private String recipient;

    @NotEmpty(message = "Body cannot be empty")
    private String body;

    @NotEmpty(message = "please enter the subject")
    private String subject;

}
