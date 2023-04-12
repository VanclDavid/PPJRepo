package com.meteo.meteo.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.meteo.meteo.Models.MeasurementEntity;

@Repository
public interface MeasurementRepository extends CrudRepository<MeasurementEntity, Integer> {
    MeasurementEntity findBySaved(LocalDateTime saved);

    List<MeasurementEntity> findAll();
}
