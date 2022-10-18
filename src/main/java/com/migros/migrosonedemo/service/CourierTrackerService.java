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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierTrackerService {

    private final StoreRepository storeRepository;
    private final CourierTrackerRepository trackerRepository;

    @Transactional
    public void trackCourier(CourierTracker courierTracker) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        getStores().stream()
                .filter(s -> DistanceCalculator.calculateDistanceInMeters(
                        s.getLocation().getLatitude(),
                        s.getLocation().getLongitude(),
                        courierTracker.getLocation().getLatitude(),
                        courierTracker.getLocation().getLongitude()) <= 100)
                .filter(s ->
                        Objects.isNull(trackerRepository.findEnteredDateTopByCourierIdAndStoreEntity(courierTracker.getCourier().getCourierId(), s)) ||
                                (Objects.nonNull(trackerRepository.findEnteredDateTopByCourierIdAndStoreEntity(courierTracker.getCourier().getCourierId(), s)) &&
                                        ChronoUnit.SECONDS.between(
                                                trackerRepository.findEnteredDateTopByCourierIdAndStoreEntity(courierTracker.getCourier().getCourierId(), s),
                                                currentDateTime) > 60))
                .forEach(s -> {
                    log.info(String.format("Courier with id: %d has entered to store : %s at %s",
                            courierTracker.getCourier().getCourierId(), s.getStoreName(), currentDateTime));
                    CourierTrackerEntity trackerEntity = prepareCourierTrackerEntity(courierTracker.getCourier().getCourierId(),
                            courierTracker.getLocation(), s, currentDateTime);
                    trackerRepository.save(trackerEntity);
                });
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
}
