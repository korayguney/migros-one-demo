package com.migros.migrosonedemo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class StoreEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String storeName;

    @Embedded
    private LocationEntity location;

    public StoreEntity(String storeName, LocationEntity location) {
        this.storeName = storeName;
        this.location = location;
    }
}
