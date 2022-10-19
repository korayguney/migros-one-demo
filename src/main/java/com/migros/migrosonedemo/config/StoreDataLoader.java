package com.migros.migrosonedemo.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.migrosonedemo.model.LocationEntity;
import com.migros.migrosonedemo.model.StoreEntity;
import com.migros.migrosonedemo.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class StoreDataLoader implements CommandLineRunner {

    @Value("classpath:data/stores.json")
    Resource resourceFile;

    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        JsonNode storeNodes = objectMapper.readTree(resourceFile.getFile());
        storeNodes.elements().forEachRemaining(n -> {
            StoreEntity storeEntity = prepareStoreEntity(
                    n.get("name").asText(),
                    n.get("lat").asDouble(),
                    n.get("lng").asDouble());
            storeRepository.save(storeEntity);
        });

    }

    public StoreEntity prepareStoreEntity(String storeName, double lat, double lng) {
        LocationEntity location = new LocationEntity(lat, lng);
        return new StoreEntity(storeName, location);
    }
}
