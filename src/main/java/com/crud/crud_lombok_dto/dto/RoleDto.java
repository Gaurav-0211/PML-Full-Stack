package com.crud.crud_lombok_dto.dto;

import com.crud.crud_lombok_dto.config.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private int id;
    private RoleType name;
}