package com.hrproject.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseModel implements Serializable {


    private Long sayi;
    private int maas;

    private Double expense;
    private String about;
    private String name;
    private String surname;
    private Long company;

}
