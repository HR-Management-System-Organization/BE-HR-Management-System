package com.hrproject.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Arrays;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_expense_pdf")
public class ExpensePdf extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "pdf_content", columnDefinition = "bytea")
    private byte[] pdfContent;

    @Column(name = "name")
    private String name;

    // Diğer özellikler eklenebilir (örneğin: açıklama, tarih, vb.)

    @Override
    public String toString() {
        return "ExpensePdf{" +
                "id=" + id +
                ", pdfContent=" + Arrays.toString(pdfContent) +
                ", name='" + name + '\'' +
                // Diğer özellikleri ekleyin
                '}';
    }
}
