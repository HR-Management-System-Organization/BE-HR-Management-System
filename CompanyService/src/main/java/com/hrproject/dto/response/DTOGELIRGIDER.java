package com.hrproject.dto.response;

import com.hrproject.repository.entity.Expense;
import com.hrproject.repository.entity.Income;
import lombok.*;

import java.util.List;

@Builder //bir sınifta nesne uretmeyi saglar
@Data // get set metodlarini otomatik tanimlar
@NoArgsConstructor //Bos constructor
@AllArgsConstructor //dolu constructor
@ToString // tostring


public class DTOGELIRGIDER {
    private Double montlytotalincome;
    private Double totalincome;
    private Double totalincome1;
    private Double totalincome2;
    private Double totalincome3;
    private Double totalincome4;
    private Double montlytotalexpense;
    private Double totalexpense;
    private Double totalexpense1;
    private Double totalexpense2;
    private Double totalexpense3;
    private Double totalexpense4;
    private List<Income> incomes;
    private List<Income> income1;
    private List<Income> income2;
    private List<Income> income3;
    private List<Income> income4;
    private List<Expense> expense1;
    private List<Expense> expense2;
    private List<Expense> expense3;
    private List<Expense> expense4;
    private List<Expense> expense5;
    private List<Expense> expenses;


}
