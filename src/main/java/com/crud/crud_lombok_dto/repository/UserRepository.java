package com.crud.crud_lombok_dto.repository;

import com.crud.crud_lombok_dto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}