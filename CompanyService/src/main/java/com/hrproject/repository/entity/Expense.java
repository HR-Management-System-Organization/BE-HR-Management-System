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
@Table(name = "tblExpense")
public class Expense extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    private Long userId;
    private String name;
    private String surname;
    private Long companyId;
    private String expenseType;
    private String currency;
    private String demandDate;
    private LocalDate billDate;
    private String paymentMethod;
    private Double amount;
    private Double netAmount;
    private Double tax;
    private String taxZone;
    private String description;
    private String billPhoto;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EExpenseStatus eExpenseStatus = EExpenseStatus.PENDING;

    @Column(name = "expense_pdf_id")
    private Long expensePdfId;
}
