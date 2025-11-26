package com.example.expense_tracker_backend.service;

import com.example.expense_tracker_backend.dto.ExpenseDTO;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ExpenseService {
    ExpenseDTO createExpense(ExpenseDTO dto);
    ExpenseDTO updateExpense(Long id, ExpenseDTO dto);
    void deleteExpense(Long id);
    ExpenseDTO getExpense(Long id);
    List<ExpenseDTO> getExpensesForMonth(int year, int month);
    java.math.BigDecimal getTotalForMonth(int year, int month);
    ByteArrayInputStream generatePdfForMonth(int year, int month) throws Exception;
}
