package com.example.expense_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExpenseDTO {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate date;
    private String category;
    private String description;

}
