package com.migros.migrosonedemo.service.validation;

import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class CourierTrackerValidator implements Validator {
    @Autowired
    CourierValidator courierValidator;

    @Autowired
    LocationValidator locationValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return CourierTracker.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourierTracker courierTracker = (CourierTracker) target;
        ValidationHelper.nested("courier", courierTracker.getCourier(), courierValidator, errors);
        ValidationHelper.nested("location", courierTracker.getLocation(), locationValidator, errors);
    }
}
