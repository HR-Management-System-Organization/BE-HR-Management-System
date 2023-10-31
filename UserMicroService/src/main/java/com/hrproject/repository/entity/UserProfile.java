package com.hrproject.repository.entity;

import com.hrproject.repository.enums.EGender;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
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

    private String companyId;

    private String username;

    private String email;

    private String password;

    private String phone;

    private int totalAnnualLeave = 14;

    private int parentalLeave;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String address;

    private String avatar;

    private String about;

    private String name;

    private String surName;

    private LocalDate birthDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.PENDING;
}