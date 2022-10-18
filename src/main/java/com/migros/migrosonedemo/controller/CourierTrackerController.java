package com.migros.migrosonedemo.controller;

import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.service.CourierTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CourierTrackerController {

    private final CourierTrackerService trackerService;

    @PostMapping("/courier")
    public void trackCourier(@RequestBody CourierTracker courierTracker) {
        trackerService.trackCourier(courierTracker);
    }
}
