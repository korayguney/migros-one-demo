package com.migros.migrosonedemo.utils;

import com.migros.migrosonedemo.exception.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public final class ValidationHelper {

    private ValidationHelper() {

    }

    public static void validate(Validator validator, String name, Object model) {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(model, name);

        ValidationUtils.invokeValidator(validator, model, result);

        if (result.hasErrors()) throw new ValidationException(result);
    }

    public static void mandatory(String field, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, field, "The field can not be empty or null!");
    }

    public static void min(String field, int val, int minValue, Errors errors) {
        if (val <= minValue) {
            errors.rejectValue(field, "Cannot be less then " + minValue );
        }
    }

    public static void regex(String field, String obj, Errors errors, Pattern pattern) {
        if (!pattern.matcher(obj).matches()) {
            errors.rejectValue(field, "Invalid regex for value");
        }
    }

    public static void nested(String field, Object obj, Validator validator, Errors errors) {
        if (obj != null) {
            errors.pushNestedPath(field);
            ValidationUtils.invokeValidator(validator, obj, errors);
            errors.popNestedPath();
        }
    }
}