package com.example.exeptions;

import java.util.Map;

public class ValidationExeption extends RuntimeException {
    private final Map<String, String> errors;
    public ValidationExeption(Map<String, String> errors) {
        super("Проверьте заполнение формы");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
