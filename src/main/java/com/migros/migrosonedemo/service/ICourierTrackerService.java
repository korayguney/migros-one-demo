package com.migros.migrosonedemo.service;

import com.migros.migrosonedemo.model.dto.CourierTracker;

public interface ICourierTrackerService {
    String trackCourier(CourierTracker courierTracker);
    double calculateTotalTravelDistanceOfCourier(int courierId);
}
