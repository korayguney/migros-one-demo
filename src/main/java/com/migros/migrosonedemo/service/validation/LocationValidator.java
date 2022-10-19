package com.migros.migrosonedemo.service.validation;

import com.migros.migrosonedemo.model.dto.Location;
import com.migros.migrosonedemo.utils.ValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Service
public class LocationValidator implements Validator {
    private static final String DOUBLE_VALUE_REGEX = "[+-]?\\d*\\.?\\d+";
    private final Pattern regexPattern;

    public LocationValidator() {
        this.regexPattern = Pattern.compile(DOUBLE_VALUE_REGEX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Location.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Location location = (Location) target;
        ValidationHelper.mandatory("latitude", errors);
        ValidationHelper.mandatory("longitude", errors);
        ValidationHelper.regex("latitude", String.valueOf(location.getLatitude()), errors, this.regexPattern);
        ValidationHelper.regex("longitude", String.valueOf(location.getLongitude()), errors, this.regexPattern);
    }
}
