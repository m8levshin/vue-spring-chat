package ru.moolls.webchat.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorDto {
    private Map<String, String> errors;

    public ErrorDto() {
        errors = new HashMap<>();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addError(String key, String error) {
        errors.put(key, error);
    }

    public void clearErrors() {
        errors.clear();
    }
}
