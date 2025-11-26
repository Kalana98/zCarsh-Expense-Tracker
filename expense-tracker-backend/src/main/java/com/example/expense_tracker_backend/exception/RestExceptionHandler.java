package com.example.expense_tracker_backend.exception;
import com.example.expense_tracker_backend.dto.RespondDTO;
import com.example.expense_tracker_backend.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespondDTO>
    handleValidation(MethodArgumentNotValidException ex) {
        return
                ResponseEntity.badRequest().body(RespondDTO.of(VarList.RSP_SOMETHING_IS_MISSING,
                        "Validation failed", null));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespondDTO> handleAll(Exception ex) {
        return
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RespondDTO.of(VarList.RSP_UNHANDLED_ERROR, ex.getMessage(), null));
    }
}