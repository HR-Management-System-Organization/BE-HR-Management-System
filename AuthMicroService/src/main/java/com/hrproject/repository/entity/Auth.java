package com.hrproject.repository.entity;


import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
public class Auth extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String taxNumber;
    private String email;
    private String activationCode;
    private String companyName;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERole role = ERole.GUEST;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status = EStatus.PENDING;
    private Double permission;//ToDO yöneticiye izin talep sayfası ve izin istek
}
