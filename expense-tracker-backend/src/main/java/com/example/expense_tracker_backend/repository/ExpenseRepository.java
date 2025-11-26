package com.example.expense_tracker_backend.repository;

import com.example.expense_tracker_backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);


    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.date BETWEEN :start AND :end")
    java.math.BigDecimal sumAmountBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
