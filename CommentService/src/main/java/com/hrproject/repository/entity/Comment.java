package com.hrproject.repository.entity;

import com.hrproject.repository.enums.ECommentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblComment")
public class Comment extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String comment;
    private Long authId;
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ECommentStatus eCommentStatus = ECommentStatus.PENDING;

    private String email;
    private String password;
    private String roles;
}
