package com.crud.crud_lombok_dto.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MailEntity {
    private String recipient;
    private String body;
    private String subject;
}
