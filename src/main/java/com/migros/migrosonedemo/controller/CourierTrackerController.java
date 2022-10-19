package com.migros.migrosonedemo.controller;

import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.service.CourierTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourierTrackerController {

    private final CourierTrackerService trackerService;

    @GetMapping("/courier/{courierId}")
    public double calculateTotalTravelDistanceOfCourier(@PathVariable int courierId) {
        return trackerService.calculateTotalTravelDistanceOfCourier(courierId);
    }

    @PostMapping("/courier")
    public String trackCourier(@RequestBody CourierTracker courierTracker) {
        return trackerService.trackCourier(courierTracker);
    }
}
