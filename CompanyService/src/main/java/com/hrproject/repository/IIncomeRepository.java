package com.hrproject.repository;

import com.hrproject.repository.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIncomeRepository extends JpaRepository<Income,Long> {




}
