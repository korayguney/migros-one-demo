package com.migros.migrosonedemo.service;

import com.migros.migrosonedemo.model.CourierTrackerEntity;
import com.migros.migrosonedemo.model.LocationEntity;
import com.migros.migrosonedemo.model.StoreEntity;
import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.model.dto.Location;
import com.migros.migrosonedemo.repository.CourierTrackerRepository;
import com.migros.migrosonedemo.repository.StoreRepository;
import com.migros.migrosonedemo.utils.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierTrackerService {

    private final StoreRepository storeRepository;
    private final CourierTrackerRepository trackerRepository;

    @Transactional
    public String trackCourier(CourierTracker courierTracker) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        AtomicReference<String> logMessage = new AtomicReference<>();
        getStores().stream()
                .filter(s -> DistanceCalculator.calculateDistanceInMeters(
                        s.getLocation().getLatitude(),
                        s.getLocation().getLongitude(),
                        courierTracker.getLocation().getLatitude(),
                        courierTracker.getLocation().getLongitude()) <= 100)
                .filter(s -> isNotReenteredInsideOneMinute(courierTracker, currentDateTime, s, logMessage))
                .forEach(s -> {
                    if (!StringUtils.hasText(logMessage.get())) {
                        logMessage.set(String.format("Courier with id: %d has entered to store area: %s at %s",
                                courierTracker.getCourier().getCourierId(), s.getStoreName(), currentDateTime));
                        log.info(logMessage.get());
                        CourierTrackerEntity trackerEntity = prepareCourierTrackerEntity(courierTracker.getCourier().getCourierId(),
                                courierTracker.getLocation(), s, currentDateTime);
                        trackerRepository.save(trackerEntity);
                    }
                });
        return StringUtils.hasText(logMessage.get()) ? logMessage.get()
                : "Courier with id: " + courierTracker.getCourier().getCourierId() + " is far away from store areas!";
    }

    private boolean isNotReenteredInsideOneMinute(CourierTracker courierTracker, LocalDateTime currentDateTime, StoreEntity s, AtomicReference<String> logMessage) {
        CourierTrackerEntity courierTrackerEntity = trackerRepository.findTopByCourierIdAndStoreEntityOrderByEnteredDateDesc(courierTracker.getCourier().getCourierId(), s);
        if (Objects.nonNull(courierTrackerEntity) && ChronoUnit.SECONDS.between(courierTrackerEntity.getEnteredDate(), currentDateTime) < 60) {
            logMessage.set(String.format("Courier with id: %d has entered to store area: %s inside 1 minute!",
                    courierTracker.getCourier().getCourierId(), s.getStoreName()));
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private CourierTrackerEntity prepareCourierTrackerEntity(int courierId, Location location, StoreEntity store, LocalDateTime currentDateTime) {
        LocationEntity locationEntity = new LocationEntity(location.getLatitude(), location.getLongitude());
        return CourierTrackerEntity.builder()
                .courierId(courierId)
                .storeEntity(store)
                .enteredDate(currentDateTime)
                .location(locationEntity)
                .build();
    }

    @Cacheable(value = "storeList", key = "'storeList'")
    @Transactional(readOnly = true)
    public List<StoreEntity> getStores() {
        return storeRepository.findAll();
    }

    public double calculateTotalTravelDistanceOfCourier(int courierId) {
        List<CourierTrackerEntity> courierTrackerList = trackerRepository.findByCourierId(courierId);
        //return courierTrackerEntityList.stream().map(courierTrackerEntity -> courierTrackerEntity.getLocation())
        //        .reduce(0.0, (c1, c2) -> c1 + DistanceCalculator.calculateDistanceInMeters(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude()), Double::sum);
        double totalDistance = 0.0;
        for (int i = 0; i < courierTrackerList.size(); i++) {
            if(i+1 < courierTrackerList.size()) {
                totalDistance += DistanceCalculator.calculateDistanceInMeters(
                        courierTrackerList.get(i).getLocation().getLatitude(),
                        courierTrackerList.get(i).getLocation().getLongitude(),
                        courierTrackerList.get(i + 1).getLocation().getLatitude(),
                        courierTrackerList.get(i + 1).getLocation().getLongitude());
            }
        }
        return totalDistance;
    }
}
