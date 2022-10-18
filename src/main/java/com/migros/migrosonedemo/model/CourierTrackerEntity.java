package com.migros.migrosonedemo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CourierTrackerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_tracker_entity_gen")
    @SequenceGenerator(name = "courier_tracker_entity_gen", sequenceName = "courier_tracker_entity_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int courierId;
    private LocalDateTime enteredDate;

    @Embedded
    private LocationEntity location;

    @OneToOne
    private StoreEntity storeEntity;
}
