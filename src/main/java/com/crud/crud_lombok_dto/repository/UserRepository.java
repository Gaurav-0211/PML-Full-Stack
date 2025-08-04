package com.crud.crud_lombok_dto.repository;

import com.crud.crud_lombok_dto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    List<User> findByNameStartsWith(String name);

    List<User> findByNameEndingWith(String name);

    List<User> findAllByOrderByNameDesc();

    List<User> findAllByOrderByUpdatedAtDesc();
}