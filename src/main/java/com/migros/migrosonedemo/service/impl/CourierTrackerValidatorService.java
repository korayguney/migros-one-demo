package com.migros.migrosonedemo.service.impl;

import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.service.ICourierTrackerService;
import com.migros.migrosonedemo.service.validation.CourierTrackerValidator;
import com.migros.migrosonedemo.utils.ValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierTrackerValidatorService implements ICourierTrackerService {
    private final CourierTrackerService courierTrackerService;
    private final CourierTrackerValidator courierTrackerValidator;

    @Override
    public String trackCourier(CourierTracker courierTracker) {
        ValidationHelper.validate(courierTrackerValidator, courierTracker.getClass().getSimpleName(), courierTracker);
        return courierTrackerService.trackCourier(courierTracker);
    }

    @Override
    public double calculateTotalTravelDistanceOfCourier(int courierId) {
        return courierTrackerService.calculateTotalTravelDistanceOfCourier(courierId);
    }
}
