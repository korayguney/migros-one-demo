package com.migros.migrosonedemo.repository;

import com.migros.migrosonedemo.model.CourierTrackerEntity;
import com.migros.migrosonedemo.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CourierTrackerRepository extends JpaRepository<CourierTrackerEntity, Long> {
    @Query("SELECT c.enteredDate FROM CourierTrackerEntity c WHERE c.courierId= :courierId AND c.storeEntity= :storeEntity")
    LocalDateTime findEnteredDateTopByCourierIdAndStoreEntity(int courierId, StoreEntity storeEntity);
}
