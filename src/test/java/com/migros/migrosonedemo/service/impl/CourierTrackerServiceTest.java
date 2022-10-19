package com.migros.migrosonedemo.service.impl;

import com.migros.migrosonedemo.model.CourierTrackerEntity;
import com.migros.migrosonedemo.model.LocationEntity;
import com.migros.migrosonedemo.model.StoreEntity;
import com.migros.migrosonedemo.model.dto.Courier;
import com.migros.migrosonedemo.model.dto.CourierTracker;
import com.migros.migrosonedemo.model.dto.Location;
import com.migros.migrosonedemo.repository.CourierTrackerRepository;
import com.migros.migrosonedemo.repository.StoreRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CourierTrackerServiceTest {

    @InjectMocks
    CourierTrackerService courierTrackerService;

    @Mock
    CourierTrackerRepository courierTrackerRepository;

    @Mock
    StoreRepository storeRepository;

    @Test
    void testTrackCourierIfCourierisReenteredInsideOneMinute() {
        // given
        List<StoreEntity> stores = prepareStoreEntities();
        CourierTrackerEntity courierTrackerEntity = prepareCourierTrackerEntity(null);
        CourierTracker courierTracker = prepareCourierTracker();

        when(storeRepository.findAll()).thenReturn(stores);
        when(courierTrackerRepository.findTopByCourierIdAndStoreEntityOrderByEnteredDateDesc(anyInt(), any())).thenReturn(courierTrackerEntity);

        // when
        String actual = courierTrackerService.trackCourier(courierTracker);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(actual, "Courier with id: 1 has entered to store area: Ataşehir MMM Migros inside 1 minute!")
        );
    }

    @Test
    void testTrackCourierIfCourierisNotReenteredInsideOneMinute() {
        // given
        List<StoreEntity> stores = prepareStoreEntities();
        CourierTrackerEntity courierTrackerEntity = prepareCourierTrackerEntity(null);
        courierTrackerEntity.setEnteredDate(LocalDateTime.now().minusMinutes(10));
        CourierTracker courierTracker = prepareCourierTracker();

        when(storeRepository.findAll()).thenReturn(stores);
        when(courierTrackerRepository.findTopByCourierIdAndStoreEntityOrderByEnteredDateDesc(anyInt(), any())).thenReturn(courierTrackerEntity);
        when(courierTrackerRepository.save(any())).thenReturn(courierTrackerEntity);

        // when
        String actual = courierTrackerService.trackCourier(courierTracker);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertTrue(actual.contains("Courier with id: 1 has entered to store area: Ataşehir MMM Migros at"))
        );
    }

    @Test
    void testTrackCourierIfCourierisNotNearOfAnyStore() {
        // given
        List<StoreEntity> stores = prepareStoreEntities();
        CourierTrackerEntity courierTrackerEntity = prepareCourierTrackerEntity(null);
        CourierTracker courierTracker = prepareCourierTracker();
        courierTracker.setLocation(new Location(41.9923307, 27.1244229));

        when(storeRepository.findAll()).thenReturn(stores);
        when(courierTrackerRepository.findTopByCourierIdAndStoreEntityOrderByEnteredDateDesc(anyInt(), any())).thenReturn(courierTrackerEntity);

        // when
        String actual = courierTrackerService.trackCourier(courierTracker);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(actual, "Courier with id: 1 is far away from store areas!")
        );
    }

    @Test
    void getStoresTest() {
        // given
        List<StoreEntity> stores = prepareStoreEntities();
        when(storeRepository.findAll()).thenReturn(stores);

        // when
        List<StoreEntity> actual = courierTrackerService.getStores();

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(actual.size(), 2)
        );
    }

    @Test
    void calculateTotalTravelDistanceOfCourierTest() {
        // given
        List<CourierTrackerEntity> courierTrackerList = prepareCourierTrackerEntities();
        when(courierTrackerRepository.findByCourierId(anyInt())).thenReturn(courierTrackerList);

        // when
        double actual = courierTrackerService.calculateTotalTravelDistanceOfCourier(1);

        // then
        assertEquals(actual, 981.6581963196004);
    }

    private List<CourierTrackerEntity> prepareCourierTrackerEntities() {
        CourierTrackerEntity courierTrackerEntity1 = prepareCourierTrackerEntity(new LocationEntity(40.9923307, 29.1244229));
        CourierTrackerEntity courierTrackerEntity2 = prepareCourierTrackerEntity(new LocationEntity(40.986106, 29.1161293));
        return Lists.list(courierTrackerEntity1, courierTrackerEntity2);
    }

    private StoreEntity prepareStoreEntity(String storeName, LocationEntity location) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreName(storeName);
        storeEntity.setLocation(new LocationEntity(location.getLatitude(), location.getLongitude()));
        return storeEntity;
    }

    private List<StoreEntity> prepareStoreEntities() {
        StoreEntity storeEntity1 = prepareStoreEntity("Ataşehir MMM Migros", new LocationEntity(40.9923307, 29.1244229));
        StoreEntity storeEntity2 = prepareStoreEntity("Novada MMM Migros", new LocationEntity(40.986106, 29.1161293));
        return Lists.list(storeEntity1, storeEntity2);
    }

    private CourierTrackerEntity prepareCourierTrackerEntity(LocationEntity locationEntity) {
        CourierTrackerEntity courierTrackerEntity = new CourierTrackerEntity();
        courierTrackerEntity.setCourierId(1);
        courierTrackerEntity.setStoreEntity(prepareStoreEntity("Ataşehir MMM Migros", new LocationEntity(40.9923307, 29.1244229)));
        courierTrackerEntity.setEnteredDate(LocalDateTime.now());
        courierTrackerEntity.setLocation((Objects.isNull(locationEntity))
                ? new LocationEntity(40.9923307, 29.1244229)
                : new LocationEntity(locationEntity.getLatitude(), locationEntity.getLongitude()));
        return courierTrackerEntity;
    }

    private CourierTracker prepareCourierTracker() {
        CourierTracker courierTracker = new CourierTracker();
        courierTracker.setCourier(new Courier(1));
        courierTracker.setLocation(new Location(40.9923307, 29.1244229));
        return courierTracker;
    }


}