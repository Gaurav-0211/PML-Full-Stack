package com.crud.crud_lombok_dto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    // Below two ids for sending otp
    private String verificationCode;

    LocalDateTime  verificationCodeExpiryTime;

    // Below two is to track login attempts and lock login
    private int failedLoginAttempts;

    private LocalDateTime accountLockedUntil;

    // Below three is for sending and validating link to forgot password while sending a link on mail
    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    private LocalDateTime lastResetLinkSentAt;


}
