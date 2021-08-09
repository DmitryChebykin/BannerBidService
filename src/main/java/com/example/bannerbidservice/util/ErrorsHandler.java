package com.example.bannerbidservice.util;

import com.example.bannerbidservice.controller.dto.ErrorMessages;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;

public class ErrorsHandler {
    public static void setValidationErrors(ErrorMessages errorMessages, BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().stream().filter(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()) != null).forEach(fieldError -> {
            throw new IllegalStateException("Duplicate key");
        });
        errorMessages.setErrorsMap(map);
    }
}