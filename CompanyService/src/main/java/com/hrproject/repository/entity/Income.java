package com.hrproject.repository.entity;

import com.hrproject.repository.enums.EExpenseStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tblincome")
public class Income extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String incomeType;
    private String currency;
    private String aciklama;
    private LocalDate billDate;
    private String paymentMethod;
    private Double amount;
    private Double netAmount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EExpenseStatus eIncomeStatus = EExpenseStatus.PENDING;
}
