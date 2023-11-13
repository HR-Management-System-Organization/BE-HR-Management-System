package com.hrproject.repository.entity;


import com.hrproject.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@SuperBuilder //bir sınifta nesne uretmeyi saglar
@Data // get set metodlarini otomatik tanimlar
@NoArgsConstructor //Bos constructor
@AllArgsConstructor //dolu constructor
@ToString // tostring

@Entity

public class Avanstelebi extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;

    private Long managerid;

    private String username;

    private Double avanstalebi;
    private Double maas;



    private LocalDate avanstarihi;
    @Enumerated(EnumType.STRING)

    private EStatus status;

    private String nedeni;
}
