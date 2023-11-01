package com.hrproject.repository;

import com.hrproject.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Long> {
    Boolean existsByCompanyNameIgnoreCase(String companyName);

//    Boolean existsById(Long id);

    Optional<Company> findByCompanyName(String companyName);

    Boolean existsByTaxNumber(String taxNumber);

//    Boolean existByCompanyName(String companyName);

//    Optional<Company> findById(Long id);

}
