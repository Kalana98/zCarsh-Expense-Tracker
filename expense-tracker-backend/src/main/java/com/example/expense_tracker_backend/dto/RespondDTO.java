package com.example.expense_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component

public class RespondDTO {

    private String code;
    private String message;
    private Object content;

    public static RespondDTO of(String code, String message, Object content) {
        return new RespondDTO(code, message, content);
    }
}
