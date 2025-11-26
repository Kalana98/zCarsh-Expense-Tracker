package com.example.expense_tracker_backend.service.impl;

import com.example.expense_tracker_backend.dto.ExpenseDTO;
import com.example.expense_tracker_backend.entity.Expense;
import com.example.expense_tracker_backend.repository.ExpenseRepository;
import com.example.expense_tracker_backend.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private  ExpenseRepository expenseRepository;
    private final com.example.expense_tracker_backend.util.PdfGenerator pdfGenerator;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, com.example.expense_tracker_backend.util.PdfGenerator pdfGenerator) {
        this.expenseRepository = expenseRepository;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        Expense e = Expense.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .build();
        Expense saved = expenseRepository.save(e);
        dto.setId(saved.getId());
        return dto;
    }
    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        Expense e = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        e.setTitle(dto.getTitle());
        e.setAmount(dto.getAmount());
        e.setDate(dto.getDate());
        e.setCategory(dto.getCategory());
        e.setDescription(dto.getDescription());
        Expense saved = expenseRepository.save(e);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public ExpenseDTO getExpense(Long id) {
        Expense e = expenseRepository.findById(id).orElseThrow(()-> new
                RuntimeException("Expense not found"));
        return mapToDto(e);
    }

    @Override
    public List<ExpenseDTO> getExpensesForMonth(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        List<Expense> list =
                expenseRepository.findAllByDateBetweenOrderByDateDesc(start, end);
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalForMonth(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        BigDecimal sum = expenseRepository.sumAmountBetween(start, end);
        return sum == null ? BigDecimal.ZERO : sum;
    }

    @Override
    public ByteArrayInputStream generatePdfForMonth(int year, int month) throws
            Exception {
        List<ExpenseDTO> expenses = getExpensesForMonth(year, month);
        BigDecimal total = getTotalForMonth(year, month);
        return pdfGenerator.createPdf(expenses, year, month, total);
    }

    private ExpenseDTO mapToDto(Expense e) {
        return ExpenseDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .amount(e.getAmount())
                .date(e.getDate())
                .category(e.getCategory())
                .description(e.getDescription())
                .build();
    }
}
