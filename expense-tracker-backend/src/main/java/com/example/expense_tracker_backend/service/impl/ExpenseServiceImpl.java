package com.example.expense_tracker_backend.service.impl;

import com.example.expense_tracker_backend.dto.ExpenseDTO;
import com.example.expense_tracker_backend.repository.ExpenseRepository;
import com.example.expense_tracker_backend.service.ExpenseService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

public class ExpenseServiceImpl implements ExpenseService {

    private  ExpenseRepository expenseRepository;
    private final com.example.expense_tracker_backend.util.PdfGenerator pdfGenerator;

    public ExpenseServiceImpl(com.example.expense_tracker_backend.util.PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        return null;
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        return null;
    }

    @Override
    public void deleteExpense(Long id) {

    }

    @Override
    public ExpenseDTO getExpense(Long id) {
        return null;
    }

    @Override
    public List<ExpenseDTO> getExpensesForMonth(int year, int month) {
        return List.of();
    }

    @Override
    public BigDecimal getTotalForMonth(int year, int month) {
        return null;
    }

    @Override
    public ByteArrayInputStream generatePdfForMonth(int year, int month) throws Exception {
        return null;
    }
}
