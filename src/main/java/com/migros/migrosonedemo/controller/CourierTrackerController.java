package com.migros.migrosonedemo.controller;

import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.service.impl.CourierTrackerValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourierTrackerController {

    private final CourierTrackerValidatorService validatorService;

    @GetMapping("/courier/{courierId}")
    public double calculateTotalTravelDistanceOfCourier(@PathVariable int courierId) {
        return validatorService.calculateTotalTravelDistanceOfCourier(courierId);
    }

    @PostMapping("/courier")
    public String trackCourier(@RequestBody CourierTracker courierTracker) {
        return validatorService.trackCourier(courierTracker);
    }
}
