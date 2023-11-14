package com.hrproject.repository;

import com.hrproject.repository.entity.Expense;
import com.hrproject.repository.entity.ExpensePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IExpensePdfRepository extends JpaRepository<ExpensePdf,Long> {




}
