package com.migros.migrosonedemo.service.validation;

import com.migros.migrosonedemo.model.dto.Courier;
import com.migros.migrosonedemo.utils.ValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class CourierValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Courier.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Courier courier = (Courier) target;
        ValidationHelper.mandatory("courierId", errors);
        ValidationHelper.min("courierId", courier.getCourierId(), 0, errors);
    }
}
