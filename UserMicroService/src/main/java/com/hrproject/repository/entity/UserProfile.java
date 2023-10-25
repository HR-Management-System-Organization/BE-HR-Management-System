package com.hrproject.repository.entity;

import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authId;

    private String username;

    private String email;
    private String password;;

    private String phone;

    private ERole role;


    private String address;

    private String avatar;

    private String about;

    private String name;

    private String surName;

    private LocalDate birthDate;

    @Builder.Default
    private EStatus status = EStatus.PENDING;
}