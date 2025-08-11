package com.crud.crud_lombok_dto.repository;

import com.crud.crud_lombok_dto.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
