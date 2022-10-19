package com.migros.migrosonedemo.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static java.text.MessageFormat.format;

public class ValidationException extends RuntimeException {
    final Errors errors;

    public ValidationException(Errors errors) {
        super(message(errors));
        this.errors = errors;
    }

    private static String message(Errors errors) {
        String fields = getFields(errors);
        return "Validation errors for fields: " + fields;
    }

    static String message(String title, String detail) {
        return format("{0}, {1}", title, detail);
    }

    private static String getFields(Errors errors) {
        List<String> fieldNames = new ArrayList<>();
        for (FieldError e : errors.getFieldErrors())
            fieldNames.add(e.getField());

        return StringUtils.join(fieldNames, ",");
    }

    public Errors getErrors() {
        return errors;
    }
}
