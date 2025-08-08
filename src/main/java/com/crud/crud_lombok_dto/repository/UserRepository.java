package com.crud.crud_lombok_dto.repository;

import com.crud.crud_lombok_dto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmail( String email);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> findByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT(:name, '%')")
    List<User> findByNameStartsWith(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%',:name)")
    List<User> findByNameEndingWith(String name);

    @Query("SELECT u FROM User u ORDER BY name DESC")
    List<User> findAllByOrderByNameDesc();

    @Query("SELECT u FROM User u ORDER BY updatedAt DESC")
    List<User> findAllByOrderByUpdatedAtDesc();
}