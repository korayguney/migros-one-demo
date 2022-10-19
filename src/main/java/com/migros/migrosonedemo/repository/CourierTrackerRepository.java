package com.migros.migrosonedemo.repository;

import com.migros.migrosonedemo.model.CourierTrackerEntity;
import com.migros.migrosonedemo.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierTrackerRepository extends JpaRepository<CourierTrackerEntity, Long> {
    CourierTrackerEntity findTopByCourierIdAndStoreEntityOrderByEnteredDateDesc(int courierId, StoreEntity storeEntity);
    List<CourierTrackerEntity> findByCourierId(int courierId);
}
