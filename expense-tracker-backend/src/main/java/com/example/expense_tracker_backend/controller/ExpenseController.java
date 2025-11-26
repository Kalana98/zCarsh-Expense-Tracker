package com.example.expense_tracker_backend.controller;

import com.example.expense_tracker_backend.dto.ExpenseDTO;
import com.example.expense_tracker_backend.dto.RespondDTO;
import com.example.expense_tracker_backend.service.ExpenseService;
import com.example.expense_tracker_backend.util.VarList;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/createExpense")
    public ResponseEntity<RespondDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        try{
            ExpenseDTO createdExpense = expenseService.createExpense(expenseDTO);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS,
                    "Expense created", createdExpense));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @PutMapping("/updateExpense")
    public ResponseEntity<RespondDTO> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        try{
            ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS, "Expense updated", updatedExpense));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/filterExpenses")
    public ResponseEntity<RespondDTO>
    getExpensesForMonth(@RequestParam(required = false) Integer year,
                        @RequestParam(required = false) Integer month) {
        try {
            LocalDate now = LocalDate.now();
            int y = (year == null) ? now.getYear() : year;
            int m = (month == null) ? now.getMonthValue() : month;
            List<ExpenseDTO> list = expenseService.getExpensesForMonth(y, m);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS,
                    "Expenses fetched", list));
        } catch (Exception e) {
            return
                    ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, "Failed to fetch expenses: " + e.getMessage(), null));
        }
    }

    @GetMapping("/getExpenseById/{id}")
    public ResponseEntity<RespondDTO> getExpense(@PathVariable Long id) {
        try {
            ExpenseDTO dto = expenseService.getExpense(id);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS,
                    "Expense fetched", dto));
        } catch (RuntimeException r) {
            return
                    ResponseEntity.status(404).body(RespondDTO.of(VarList.RSP_NO_DATA_FOUND,
                            r.getMessage(), null));
        } catch (Exception e) {
            return
                    ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, "Failed to fetch expense: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RespondDTO> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS,
                    "Expense deleted", null));
        } catch (Exception e) {
            return
                    ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, "Failed to delete expense: " + e.getMessage(), null));
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<RespondDTO> getTotalForMonth(@RequestParam(required =
                                                               false) Integer year,
                                                       @RequestParam(required =
                                                               false) Integer month) {
        try {
            LocalDate now = LocalDate.now();
            int y = (year == null) ? now.getYear() : year;
            int m = (month == null) ? now.getMonthValue() : month;
            java.math.BigDecimal total = expenseService.getTotalForMonth(y, m);
            Map<String, Object> map = new HashMap<>();
            map.put("year", y);
            map.put("month", m);
            map.put("total", total);
            return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS, "Total fetched", map));
        } catch (Exception e) {
            return
                    ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, "Failed to fetch total: " + e.getMessage(), null));
        }
    }

    @GetMapping("/months")
    public ResponseEntity<RespondDTO> getMonthsList() {
        // return last 12 months list where only current month is clickable (perrequirement)
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> months = new ArrayList<>();
        YearMonth currentYm = YearMonth.of(now.getYear(), now.getMonthValue());
        for (int i = 11; i >= 0; i--) {
            YearMonth ym = currentYm.minusMonths(i);
            Map<String, Object> m = new HashMap<>();
            m.put("year", ym.getYear());
            m.put("month", ym.getMonthValue());
            m.put("label", ym.getMonth().name() + " " + ym.getYear());
            // Past months not clickable — only current month clickable
            boolean clickable = ym.equals(currentYm);
            m.put("clickable", clickable);
            months.add(m);
        }
        return ResponseEntity.ok(RespondDTO.of(VarList.RSP_SUCCESS, "Months list", months));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<?> exportPdf(@RequestParam Integer year,
                                       @RequestParam Integer month) {
        // Only allow if requested month is the current month (per requirement)
        LocalDate now = LocalDate.now();
        if (year != now.getYear() || month != now.getMonthValue()) {
            return
                    ResponseEntity.status(403).body(RespondDTO.of(VarList.RSP_FAIL, "Only current month export allowed", null));
        }
        try {
            ByteArrayInputStream bis = expenseService.generatePdfForMonth(year,
                    month);
            InputStreamResource resource = new InputStreamResource(bis);
            String filename = "expense-report-" + year + "-" +
                    String.format("%02d", month) + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                                    .contentType(MediaType.APPLICATION_PDF)
                                    .body(resource);
        } catch (Exception e) {
            return
                    ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_ERROR, "Failed to generate PDF: " + e.getMessage(), null));
        }
    }

}
